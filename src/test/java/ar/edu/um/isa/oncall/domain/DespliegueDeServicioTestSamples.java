package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DespliegueDeServicioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DespliegueDeServicio getDespliegueDeServicioSample1() {
        return new DespliegueDeServicio().id(1L).version("version1");
    }

    public static DespliegueDeServicio getDespliegueDeServicioSample2() {
        return new DespliegueDeServicio().id(2L).version("version2");
    }

    public static DespliegueDeServicio getDespliegueDeServicioRandomSampleGenerator() {
        return new DespliegueDeServicio().id(longCount.incrementAndGet()).version(UUID.randomUUID().toString());
    }
}
