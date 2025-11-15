package co.com.bootcamp.r2dbc;

import co.com.bootcamp.model.bootcamp.gateways.InscripcionRepository;
import co.com.bootcamp.model.bootcamp.inscripcion.Inscripcion;
import co.com.bootcamp.r2dbc.entities.InscripcionEntity;
import co.com.bootcamp.r2dbc.helper.InscripcionEntityMapper;
import co.com.bootcamp.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class InscripcionRepositoryAdapter extends ReactiveAdapterOperations<
        Inscripcion,
        InscripcionEntity,
        Long,
        InscripcionReactiveRepository
> implements InscripcionRepository {

    private final InscripcionEntityMapper inscripcionEntityMapper;

    public InscripcionRepositoryAdapter(InscripcionReactiveRepository repository,
                                       ObjectMapper reactiveMapper,
                                       InscripcionEntityMapper entityMapper) {
        super(repository, reactiveMapper, entityMapper::toDomain);
        this.inscripcionEntityMapper = entityMapper;
    }

    @Override
    protected InscripcionEntity toData(Inscripcion entity) {
        return inscripcionEntityMapper.toEntity(entity);
    }

    @Override
    public Mono<Inscripcion> registrarInscripcion(Inscripcion inscripcion) {
        return super.save(inscripcion)
                .flatMap(saved -> repository.findById(saved.getId())
                        .map(inscripcionEntityMapper::toDomain));
    }

    @Override
    public Mono<Long> contarInscripcionesActivasPorPersona(Long personaId) {
        return repository.countByPersonaId(personaId);
    }

    @Override
    public Flux<Long> obtenerBootcampIdsDeInscripcionesActivas(Long personaId) {
        return repository.findBootcampIdsByPersonaId(personaId);
    }
}
