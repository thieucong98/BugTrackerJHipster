package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RdFPathTable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RdFPathTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RdFPathTableRepository extends ReactiveCrudRepository<RdFPathTable, Long>, RdFPathTableRepositoryInternal {
    Flux<RdFPathTable> findAllBy(Pageable pageable);

    @Override
    <S extends RdFPathTable> Mono<S> save(S entity);

    @Override
    Flux<RdFPathTable> findAll();

    @Override
    Mono<RdFPathTable> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RdFPathTableRepositoryInternal {
    <S extends RdFPathTable> Mono<S> save(S entity);

    Flux<RdFPathTable> findAllBy(Pageable pageable);

    Flux<RdFPathTable> findAll();

    Mono<RdFPathTable> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RdFPathTable> findAllBy(Pageable pageable, Criteria criteria);

}
