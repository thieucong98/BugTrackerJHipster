package com.mycompany.myapp.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.RdFBatchRegister;
import com.mycompany.myapp.repository.RdFBatchRegisterRepository;
import com.mycompany.myapp.repository.search.RdFBatchRegisterSearchRepository;
import com.mycompany.myapp.service.dto.RdFBatchRegisterDTO;
import com.mycompany.myapp.service.mapper.RdFBatchRegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RdFBatchRegister}.
 */
@Service
@Transactional
public class RdFBatchRegisterService {

    private final Logger log = LoggerFactory.getLogger(RdFBatchRegisterService.class);

    private final RdFBatchRegisterRepository rdFBatchRegisterRepository;

    private final RdFBatchRegisterMapper rdFBatchRegisterMapper;

    private final RdFBatchRegisterSearchRepository rdFBatchRegisterSearchRepository;

    public RdFBatchRegisterService(
        RdFBatchRegisterRepository rdFBatchRegisterRepository,
        RdFBatchRegisterMapper rdFBatchRegisterMapper,
        RdFBatchRegisterSearchRepository rdFBatchRegisterSearchRepository
    ) {
        this.rdFBatchRegisterRepository = rdFBatchRegisterRepository;
        this.rdFBatchRegisterMapper = rdFBatchRegisterMapper;
        this.rdFBatchRegisterSearchRepository = rdFBatchRegisterSearchRepository;
    }

    /**
     * Save a rdFBatchRegister.
     *
     * @param rdFBatchRegisterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RdFBatchRegisterDTO> save(RdFBatchRegisterDTO rdFBatchRegisterDTO) {
        log.debug("Request to save RdFBatchRegister : {}", rdFBatchRegisterDTO);
        return rdFBatchRegisterRepository
            .save(rdFBatchRegisterMapper.toEntity(rdFBatchRegisterDTO))
            .flatMap(rdFBatchRegisterSearchRepository::save)
            .map(rdFBatchRegisterMapper::toDto);
    }

    /**
     * Update a rdFBatchRegister.
     *
     * @param rdFBatchRegisterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RdFBatchRegisterDTO> update(RdFBatchRegisterDTO rdFBatchRegisterDTO) {
        log.debug("Request to update RdFBatchRegister : {}", rdFBatchRegisterDTO);
        return rdFBatchRegisterRepository
            .save(rdFBatchRegisterMapper.toEntity(rdFBatchRegisterDTO))
            .flatMap(rdFBatchRegisterSearchRepository::save)
            .map(rdFBatchRegisterMapper::toDto);
    }

    /**
     * Partially update a rdFBatchRegister.
     *
     * @param rdFBatchRegisterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RdFBatchRegisterDTO> partialUpdate(RdFBatchRegisterDTO rdFBatchRegisterDTO) {
        log.debug("Request to partially update RdFBatchRegister : {}", rdFBatchRegisterDTO);

        return rdFBatchRegisterRepository
            .findById(rdFBatchRegisterDTO.getId())
            .map(existingRdFBatchRegister -> {
                rdFBatchRegisterMapper.partialUpdate(existingRdFBatchRegister, rdFBatchRegisterDTO);

                return existingRdFBatchRegister;
            })
            .flatMap(rdFBatchRegisterRepository::save)
            .flatMap(savedRdFBatchRegister -> {
                rdFBatchRegisterSearchRepository.save(savedRdFBatchRegister);

                return Mono.just(savedRdFBatchRegister);
            })
            .map(rdFBatchRegisterMapper::toDto);
    }

    /**
     * Get all the rdFBatchRegisters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RdFBatchRegisterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RdFBatchRegisters");
        return rdFBatchRegisterRepository.findAllBy(pageable).map(rdFBatchRegisterMapper::toDto);
    }

    /**
     * Returns the number of rdFBatchRegisters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return rdFBatchRegisterRepository.count();
    }

    /**
     * Returns the number of rdFBatchRegisters available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return rdFBatchRegisterSearchRepository.count();
    }

    /**
     * Get one rdFBatchRegister by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<RdFBatchRegisterDTO> findOne(Long id) {
        log.debug("Request to get RdFBatchRegister : {}", id);
        return rdFBatchRegisterRepository.findById(id).map(rdFBatchRegisterMapper::toDto);
    }

    /**
     * Delete the rdFBatchRegister by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete RdFBatchRegister : {}", id);
        return rdFBatchRegisterRepository.deleteById(id).then(rdFBatchRegisterSearchRepository.deleteById(id));
    }

    /**
     * Search for the rdFBatchRegister corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RdFBatchRegisterDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RdFBatchRegisters for query {}", query);
        return rdFBatchRegisterSearchRepository.search(query, pageable).map(rdFBatchRegisterMapper::toDto);
    }
}
