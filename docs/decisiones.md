# Decisiones · Extensión 5 (Ventanas de mantenimiento) · Regla C

**Extensión elegida:** 5, Ventanas de mantenimiento. **Regla:** C, resolver un estado en un instante dado. La vuelta de tuerca dice que una ventana recurrente es el mismo problema temporal que una rotación, así que resolví las dos preguntas con el mismo criterio: "¿quién está de guardia en _t_?" y "¿está este servicio en mantenimiento en _t_?".

## Modelo

- **`VentanaDeMantenimiento`**: la primera ocurrencia (`desde`/`hasta`), una `recurrencia` (`UNICA`, `DIARIA` o `SEMANAL`), la `zonaHoraria`, un `repetirHasta` opcional y si está `activa`.
- **`ServicioEnVentana`**: qué servicios cubre cada ventana. Una ventana puede cubrir varios servicios y un servicio puede estar en varias ventanas.
- **`ExcepcionDeVentana`**: cancela (`CANCELADA`) o mueve (`REPROGRAMADA`, con `nuevoDesde`/`nuevoHasta`) la ocurrencia de una `fecha` concreta.
- **`Alerta.suprimidaPor`**: la ventana que suprimió la alerta. Engancha la extensión al modelo base y deja rastro: cuando alguien pregunte por qué una alerta no abrió incidente, la respuesta está en la alerta.

## Supuestos en la regla C para ventanas

1. **Una ventana se define por su primera ocurrencia más una repetición**, en lugar de "día de la semana + hora". Sirve igual para ventanas únicas, diarias y semanales, y cada ocurrencia se calcula a partir de la primera.
2. **La repetición es en hora local de la zona de la ventana.** "Los martes de 2 a 4" sigue siendo de 2 a 4 después del cambio de hora, aunque en UTC sea otro instante. Si una hora local no existe ese día (salto de primavera), se corre hacia adelante lo que dura el salto. Si existe dos veces (atraso de otoño), se toma la que tiene el mismo offset que la primera ocurrencia: una ventana cargada en la segunda 01:15 sigue siendo la segunda 01:15, y no se corre una hora.
   - Repetir sumando 7 × 24 h en UTC hubiera corrido la ventana una hora en cada cambio de hora, y el mantenimiento real lo programa una persona mirando el reloj local.
3. **Cada ocurrencia es un intervalo semiabierto `[desde, hasta)`**, igual que los turnos de guardia: a las 04:00 la ventana de 2 a 4 ya terminó. Una ventana puede cruzar la medianoche (de 23:00 a 01:00).
4. **`repetirHasta` es la última fecha local en que _empieza_ una ocurrencia.** Vacío significa que la ventana se repite sin fin.
5. **Excepciones:** afectan la ocurrencia que empezaba en su `fecha` local. `CANCELADA` la anula y `REPROGRAMADA` la reemplaza por el nuevo horario, que puede caer otro día.
   - Una excepción sobre una fecha en la que la ventana no ocurre no tiene efecto: no se puede cancelar ni mover algo que no existía.
   - **Una `REPROGRAMADA` sin `nuevoDesde`/`nuevoHasta` válidos cancela la ocurrencia y no la mueve a ningún lado.** Es una decisión: ante un dato incompleto, prefiero que el servicio quede _sin_ mantenimiento y las alertas abran incidentes, antes que suprimir alertas de una caída real. En guardias, despertar a alguien de más es un error mucho más barato que no despertarlo. No lo valido al cargar porque eso implica tocar el ABM generado.
6. **Si varias ocurrencias cubren el instante, manda la que termina más tarde**, sean de ventanas distintas o de la misma (una ventana que dura más que su período, o una reprogramación que pisa otra ocurrencia): es la que define hasta cuándo el servicio está en mantenimiento.
7. **Datos inválidos no rompen la regla:** una ventana con `hasta ≤ desde` no cubre nada, una zona inválida se toma como UTC y una ventana inactiva no cuenta.

## Qué pasa con las alertas

8. **Si ya hay un incidente abierto para ese fingerprint, la alerta se le pega como siempre**, aunque el servicio esté en mantenimiento. La ventana sirve para que el trabajo planificado no abra incidentes _nuevos_, no para esconder uno que ya existía y alguien está atendiendo.
9. **Si no hay incidente abierto y el servicio está en una ventana, la alerta se suprime:** se guarda como procesada, sin incidente y con `suprimidaPor`, y la ingesta responde `ALERTA_SUPRIMIDA`. No se descarta, para que quede el registro de la señal.
10. **Se decide por `recibidaEn`**, el instante de la alerta, no por cuándo se procesa. Al terminar la ventana, la misma señal vuelve a abrir incidente.

## Supuestos en la regla C para rotaciones

- **Turnos semiabiertos `[desde, hasta)`:** en el relevo de las 09:00 manda el turno que empieza.
- **Solapes y reemplazos:** un reemplazo pisa al turno regular. Entre dos solapados (uno termina 09:00, otro empieza 08:30) gana el que empezó último, porque quien entra ya tomó la guardia.
- **Primario y secundario:** se resuelve cada rol por separado. Si no hay primario, atiende el secundario: mejor despertar al respaldo que a nadie.
- **Un hueco no es una excepción:** la respuesta es `SIN_COBERTURA`.
- **Zonas horarias:** a diferencia de las ventanas, los turnos son explícitos (cada uno con su `desde`/`hasta` en `Instant`), así que el horario de verano no afecta la resolución. La zona solo se usa para mostrar la hora local.

## Cómo lo verifiqué

| Test                                                                                           | Qué comprueba                                                                                                                                                                                                                                                                                                                                                                                              |
| ---------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `MantenimientoVigenteServiceTest` (15, sin base)                                               | Ventana única y sus bordes; semanal y diaria; `repetirHasta`; ventana que cruza la medianoche; horario de verano (una semanal de 1 a 3 en Nueva York sigue en hora local el día del salto y la semana siguiente; una única en la hora que se repite en otoño no se corre); cancelada; reprogramada; excepción sobre una fecha sin ocurrencia; ocurrencias solapadas de una misma ventana; varias ventanas. |
| `DeduplicacionDeAlertasServiceTest` (20)                                                       | Los 18 casos de la regla A siguen pasando, más: en ventana se suprime; un incidente abierto gana a la ventana.                                                                                                                                                                                                                                                                                             |
| `GuardiaVigenteServiceTest` (15)                                                               | Relevo, solape, reemplazo, primario/secundario, hueco, rotación inactiva, cambio de hora.                                                                                                                                                                                                                                                                                                                  |
| `IngestaDeAlertasResourceIT`, `MantenimientoVigenteResourceIT` (PostgreSQL con Testcontainers) | De punta a punta: una alerta en ventana no abre incidente y queda con `suprimidaPor`; terminada la ventana, la misma señal sí lo abre; una semanal con una ocurrencia cancelada; una ventana inactiva no cuenta.                                                                                                                                                                                           |

La regla no lee el reloj: recibe el instante como parámetro, así que los tests prueban cualquier fecha (incluido el día del cambio de hora) sin esperar. La suite completa (`./mvnw verify`) pasa, incluidos los tests generados de las entidades nuevas.

**Endpoints:** `GET /api/servicios/{id}/mantenimiento?en=<instante>` y `GET /api/rotaciones/{id}/guardia?en=<instante>`. La ingesta (`POST /api/alertas/ingesta`) responde `ALERTA_SUPRIMIDA` cuando corresponde.
