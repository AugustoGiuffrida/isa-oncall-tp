package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CambioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Cambio getCambioSample1() {
        return new Cambio().id(1L).titulo("titulo1").descripcion("descripcion1").ticketUrl("ticketUrl1");
    }

    public static Cambio getCambioSample2() {
        return new Cambio().id(2L).titulo("titulo2").descripcion("descripcion2").ticketUrl("ticketUrl2");
    }

    public static Cambio getCambioRandomSampleGenerator() {
        return new Cambio()
            .id(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .ticketUrl(UUID.randomUUID().toString());
    }
}
