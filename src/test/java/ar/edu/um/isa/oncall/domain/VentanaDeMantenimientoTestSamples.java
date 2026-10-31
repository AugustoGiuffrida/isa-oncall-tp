package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VentanaDeMantenimientoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static VentanaDeMantenimiento getVentanaDeMantenimientoSample1() {
        return new VentanaDeMantenimiento().id(1L).nombre("nombre1").descripcion("descripcion1").zonaHoraria("zonaHoraria1");
    }

    public static VentanaDeMantenimiento getVentanaDeMantenimientoSample2() {
        return new VentanaDeMantenimiento().id(2L).nombre("nombre2").descripcion("descripcion2").zonaHoraria("zonaHoraria2");
    }

    public static VentanaDeMantenimiento getVentanaDeMantenimientoRandomSampleGenerator() {
        return new VentanaDeMantenimiento()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .zonaHoraria(UUID.randomUUID().toString());
    }
}
