package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExcepcionDeVentanaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ExcepcionDeVentana getExcepcionDeVentanaSample1() {
        return new ExcepcionDeVentana().id(1L).motivo("motivo1");
    }

    public static ExcepcionDeVentana getExcepcionDeVentanaSample2() {
        return new ExcepcionDeVentana().id(2L).motivo("motivo2");
    }

    public static ExcepcionDeVentana getExcepcionDeVentanaRandomSampleGenerator() {
        return new ExcepcionDeVentana().id(longCount.incrementAndGet()).motivo(UUID.randomUUID().toString());
    }
}
