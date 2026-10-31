package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CorrelacionCambioIncidenteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CorrelacionCambioIncidente getCorrelacionCambioIncidenteSample1() {
        return new CorrelacionCambioIncidente().id(1L).nota("nota1");
    }

    public static CorrelacionCambioIncidente getCorrelacionCambioIncidenteSample2() {
        return new CorrelacionCambioIncidente().id(2L).nota("nota2");
    }

    public static CorrelacionCambioIncidente getCorrelacionCambioIncidenteRandomSampleGenerator() {
        return new CorrelacionCambioIncidente().id(longCount.incrementAndGet()).nota(UUID.randomUUID().toString());
    }
}
