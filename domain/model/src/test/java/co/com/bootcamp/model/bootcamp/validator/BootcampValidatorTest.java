package co.com.bootcamp.model.bootcamp.validator;

import co.com.bootcamp.model.bootcamp.exception.CapacidadesInvalidasException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BootcampValidatorTest {

    @Test
    void validarCantidadCapacidades_CuandoListaVacia_DeberiaLanzarExcepcion() {
        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(Collections.emptyList()))
                .expectErrorMatches(throwable -> throwable instanceof CapacidadesInvalidasException
                        && throwable.getMessage().contains("debe tener al menos"))
                .verify();
    }

    @Test
    void validarCantidadCapacidades_CuandoListaNula_DeberiaLanzarExcepcion() {
        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(null))
                .expectErrorMatches(throwable -> throwable instanceof CapacidadesInvalidasException
                        && throwable.getMessage().contains("debe tener al menos"))
                .verify();
    }

    @Test
    void validarCantidadCapacidades_CuandoUnaCapacidad_DeberiaRetornarMonoVacio() {
        List<Long> capacidadesIds = Collections.singletonList(1L);

        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(capacidadesIds))
                .verifyComplete();
    }

    @Test
    void validarCantidadCapacidades_CuandoDosCapacidades_DeberiaRetornarMonoVacio() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L);

        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(capacidadesIds))
                .verifyComplete();
    }

    @Test
    void validarCantidadCapacidades_CuandoTresCapacidades_DeberiaRetornarMonoVacio() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 3L);

        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(capacidadesIds))
                .verifyComplete();
    }

    @Test
    void validarCantidadCapacidades_CuandoCuatroCapacidades_DeberiaRetornarMonoVacio() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 3L, 4L);

        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(capacidadesIds))
                .verifyComplete();
    }

    @Test
    void validarCantidadCapacidades_CuandoMasDeCuatroCapacidades_DeberiaLanzarExcepcion() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);

        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(capacidadesIds))
                .expectErrorMatches(throwable -> throwable instanceof CapacidadesInvalidasException
                        && throwable.getMessage().contains("no puede tener más de"))
                .verify();
    }

    @Test
    void validarCantidadCapacidades_CuandoCincoCapacidades_DeberiaLanzarExcepcion() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);

        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(capacidadesIds))
                .expectError(CapacidadesInvalidasException.class)
                .verify();
    }

    @Test
    void validarCantidadCapacidades_CuandoMuchasCapacidades_DeberiaLanzarExcepcion() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);

        StepVerifier.create(BootcampValidator.validarCantidadCapacidades(capacidadesIds))
                .expectError(CapacidadesInvalidasException.class)
                .verify();
    }
}

