# Decisiones · Extensión 6 (Gestión de cambios) · Regla D

**Extensión elegida:** 6, Gestión de cambios. **Regla:** D, ejecución de la cadena de escalamiento. La D depende de la C (para escalar hay que saber a quién), así que implementé de la C lo que la D necesita.

## Modelo

- **`Cambio`** (con su `autor`, un `User`), **`DespliegueDeServicio`** (un cambio que llega a un `Servicio`) y **`CorrelacionCambioIncidente`** (un `Incidente` sospechado de un despliegue; origen `AUTOMATICA`/`MANUAL`, estado `SUGERIDA`/`CONFIRMADA`/`DESCARTADA`).
- Correlaciono el incidente con el **despliegue** y no con el cambio: lo que rompe un servicio es que el cambio _llegue_ a ese servicio, y un mismo cambio puede desplegarse en varios.
- Para la regla D sumé **`EjecucionDeEscalamiento`**: una fila por escalón ejecutado. Es la memoria del proceso y su auditoría. También agregué `Incidente.silenciadoHasta` para el corte "silenciado" y `TurnoDeGuardia.rol` (primario/secundario) para la regla C.

## Supuestos en la regla D

1. **`esperaMinutos` es la espera _después_ de un paso**, como dice el JDL ("cuánto se espera antes de subir un escalón"). Por eso el paso 1 sale apenas se detecta el incidente.
2. **La espera se cuenta desde la ejecución real.** Si el proceso estuvo caído, al volver ejecuta un paso y espera; no dispara de golpe los acumulados.
3. **Quién despierta al proceso:** un `@Scheduled` cada 30 s (`oncall.escalamiento.intervalo`). Descarté una cola: en un monolito es más infraestructura para el mismo resultado. Se puede disparar a mano con `POST /api/escalamiento/ejecutar`.
4. **Idempotencia:** antes de decidir, bloqueo la fila del incidente (`SELECT … FOR UPDATE`) y leo los escalones ya registrados. Si dos corridas coinciden, la segunda espera y ve lo que hizo la primera. Reconocer usa el mismo lock: o se notificó antes del reconocimiento, o no se notifica.
5. **Cortes:**
   - **Resuelto o cerrado**: corta.
   - **Reconocido**: corta. Mitigado también, porque alguien lo está atendiendo.
   - **Silenciado**: es una _pausa_ de hasta 24 h. Al vencer, la cadena sigue donde estaba.
   - **Agotado**: ver el punto 6.
6. **Se acaban los niveles:** `repetirVeces = N` son N vueltas adicionales. Después de la espera del último paso, registro `AGOTADO` en la línea de tiempo y no notifico más. No invento un nivel extra: una política que se agota está mal dimensionada, y eso lo resuelve una persona.
7. **A quién avisa un paso:** a su destinatario directo o, si no tiene, al primario de guardia de su rotación (regla C). Si tiene las dos cosas, manda el directo.
   - Sin cobertura, sin destinatario o con el usuario desactivado, el paso queda `SIN_DESTINATARIO` y la cadena **sigue sin esperar**: no tiene sentido esperar que reconozca alguien a quien no se le avisó.
8. **Qué política aplica:** la del servicio más crítico del incidente. Si no hay ninguna, lo registro una sola vez, y si después se carga una, la cadena arranca.
   - Avanzo por `orden` y no por posición: si alguien edita la política en medio de un incidente, la cadena sigue desde su nivel.
9. **La notificación** queda en estado `PENDIENTE`. El envío real no es parte de la regla: la regla decide _a quién y cuándo_.

## Supuestos en la extensión

1. **Nivel 0:** si el incidente tiene un cambio sospechoso, el primer aviso va a su autor, antes del paso 1, con el canal y la espera del paso 1. Si no reconoce, sigue la política normal. Va **solo en la primera vuelta**: en las repeticiones el autor ya fue avisado y la responsabilidad es de la guardia.
2. **Correlación automática:** en la primera corrida, cada despliegue sobre un servicio del incidente en los **60 minutos previos a la detección** (configurable) queda como `AUTOMATICA`/`SUGERIDA`.
   - Un despliegue en el mismo instante de la detección entra. Uno posterior no, porque no pudo causarlo: suele ser el fix.
3. **Varios sospechosos:** gana el despliegue más cercano a la detección, porque es lo último que se tocó antes de que se rompiera.
4. **Una correlación `DESCARTADA` no cuenta y no se vuelve a sugerir:** si una persona la descartó, la regla la respeta. Una **manual** cuenta aunque esté fuera de la ventana, porque la ventana es solo el criterio de la automática.
5. **Autor desactivado:** el nivel 0 queda sin destinatario y sale el paso 1 en la misma corrida.
6. **Sin política no hay nivel 0**, porque no hay paso 1 del que tomar canal y espera.
7. **Si el autor también está de guardia, recibe dos avisos** (nivel 0 y paso 1). No los deduplico: son dos roles distintos.
8. **Una correlación manual cargada con la cadena ya en marcha no la reinicia.** Queda como dato para el postmortem.

## Supuestos en la regla C (lo que usa la D)

- **Turnos semiabiertos `[desde, hasta)`:** en el relevo de las 09:00 manda el turno que empieza.
- **Solapes y reemplazos:** un reemplazo pisa al turno regular. Entre dos solapados (uno termina 09:00, otro empieza 08:30) gana el que empezó último, porque quien entra ya tomó la guardia.
- **Primario y secundario:** se resuelve cada rol por separado. Si no hay primario, atiende el secundario: mejor despertar al respaldo que a nadie.
- **Un hueco no es una excepción:** la respuesta es `SIN_COBERTURA`, y la D saltea ese paso.
- **Zonas horarias:** los turnos son `Instant`, así que el horario de verano no afecta la resolución. La zona de la rotación solo se usa para mostrar la hora local.

## Cómo lo verifiqué

| Test                                                         | Qué comprueba                                                                                                                                                                                                                                                                                 |
| ------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `EscalamientoServiceTest` (27, sin base)                     | Avance por niveles; idempotencia (correr dos veces el mismo instante notifica una vez); caída del proceso sin ráfaga; espera 0; hueco en la guardia; usuario inactivo; cada corte; agotado; repeticiones; política editada; nivel 0 (al autor, después el paso 1, solo en la primera vuelta). |
| `CorrelacionDeCambiosServiceTest` (8)                        | Ventana de 60 min; el más cercano a la detección; no duplicar; descartada; manual fuera de ventana.                                                                                                                                                                                           |
| `GuardiaVigenteServiceTest` (15)                             | Relevo, solape, reemplazo, primario/secundario, hueco, rotación inactiva, cambio de hora.                                                                                                                                                                                                     |
| `EscalamientoResourceIT` (10, PostgreSQL con Testcontainers) | De punta a punta: dos corridas seguidas dejan una sola notificación; reconocer y silenciar cortan; un despliegue reciente lleva el primer aviso a su autor y deja la correlación; uno viejo no.                                                                                               |

Los tests controlan el tiempo pasando el instante como parámetro: la regla no lee el reloj, así que se puede "adelantar" media hora sin esperar. La suite completa (`./mvnw verify`) pasa, incluidos los tests generados de las entidades nuevas.

**Límites conocidos:** la resolución temporal es el intervalo del reloj (30 s). Fuera del alcance del TP también implementé la evaluación de tiempos contra los SLOs (regla B, por incidente).
