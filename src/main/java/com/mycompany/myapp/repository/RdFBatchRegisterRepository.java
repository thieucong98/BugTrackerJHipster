package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RdFBatchRegister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RdFBatchRegister entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RdFBatchRegisterRepository extends ReactiveCrudRepository<RdFBatchRegister, Long>, RdFBatchRegisterRepositoryInternal {
    Flux<RdFBatchRegister> findAllBy(Pageable pageable);

    @Override
    <S extends RdFBatchRegister> Mono<S> save(S entity);

    @Override
    Flux<RdFBatchRegister> findAll();

    @Override
    Mono<RdFBatchRegister> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RdFBatchRegisterRepositoryInternal {
    <S extends RdFBatchRegister> Mono<S> save(S entity);

    Flux<RdFBatchRegister> findAllBy(Pageable pageable);

    Flux<RdFBatchRegister> findAll();

    Mono<RdFBatchRegister> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RdFBatchRegister> findAllBy(Pageable pageable, Criteria criteria);

}
