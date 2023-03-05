package com.mycompany.myapp.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.RdFPathTable;
import com.mycompany.myapp.repository.RdFPathTableRepository;
import com.mycompany.myapp.repository.search.RdFPathTableSearchRepository;
import com.mycompany.myapp.service.dto.RdFPathTableDTO;
import com.mycompany.myapp.service.mapper.RdFPathTableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RdFPathTable}.
 */
@Service
@Transactional
public class RdFPathTableService {

    private final Logger log = LoggerFactory.getLogger(RdFPathTableService.class);

    private final RdFPathTableRepository rdFPathTableRepository;

    private final RdFPathTableMapper rdFPathTableMapper;

    private final RdFPathTableSearchRepository rdFPathTableSearchRepository;

    public RdFPathTableService(
        RdFPathTableRepository rdFPathTableRepository,
        RdFPathTableMapper rdFPathTableMapper,
        RdFPathTableSearchRepository rdFPathTableSearchRepository
    ) {
        this.rdFPathTableRepository = rdFPathTableRepository;
        this.rdFPathTableMapper = rdFPathTableMapper;
        this.rdFPathTableSearchRepository = rdFPathTableSearchRepository;
    }

    /**
     * Save a rdFPathTable.
     *
     * @param rdFPathTableDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RdFPathTableDTO> save(RdFPathTableDTO rdFPathTableDTO) {
        log.debug("Request to save RdFPathTable : {}", rdFPathTableDTO);
        return rdFPathTableRepository
            .save(rdFPathTableMapper.toEntity(rdFPathTableDTO))
            .flatMap(rdFPathTableSearchRepository::save)
            .map(rdFPathTableMapper::toDto);
    }

    /**
     * Update a rdFPathTable.
     *
     * @param rdFPathTableDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RdFPathTableDTO> update(RdFPathTableDTO rdFPathTableDTO) {
        log.debug("Request to update RdFPathTable : {}", rdFPathTableDTO);
        return rdFPathTableRepository
            .save(rdFPathTableMapper.toEntity(rdFPathTableDTO))
            .flatMap(rdFPathTableSearchRepository::save)
            .map(rdFPathTableMapper::toDto);
    }

    /**
     * Partially update a rdFPathTable.
     *
     * @param rdFPathTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RdFPathTableDTO> partialUpdate(RdFPathTableDTO rdFPathTableDTO) {
        log.debug("Request to partially update RdFPathTable : {}", rdFPathTableDTO);

        return rdFPathTableRepository
            .findById(rdFPathTableDTO.getId())
            .map(existingRdFPathTable -> {
                rdFPathTableMapper.partialUpdate(existingRdFPathTable, rdFPathTableDTO);

                return existingRdFPathTable;
            })
            .flatMap(rdFPathTableRepository::save)
            .flatMap(savedRdFPathTable -> {
                rdFPathTableSearchRepository.save(savedRdFPathTable);

                return Mono.just(savedRdFPathTable);
            })
            .map(rdFPathTableMapper::toDto);
    }

    /**
     * Get all the rdFPathTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RdFPathTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RdFPathTables");
        return rdFPathTableRepository.findAllBy(pageable).map(rdFPathTableMapper::toDto);
    }

    /**
     * Returns the number of rdFPathTables available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return rdFPathTableRepository.count();
    }

    /**
     * Returns the number of rdFPathTables available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return rdFPathTableSearchRepository.count();
    }

    /**
     * Get one rdFPathTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<RdFPathTableDTO> findOne(Long id) {
        log.debug("Request to get RdFPathTable : {}", id);
        return rdFPathTableRepository.findById(id).map(rdFPathTableMapper::toDto);
    }

    /**
     * Delete the rdFPathTable by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete RdFPathTable : {}", id);
        return rdFPathTableRepository.deleteById(id).then(rdFPathTableSearchRepository.deleteById(id));
    }

    /**
     * Search for the rdFPathTable corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RdFPathTableDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RdFPathTables for query {}", query);
        return rdFPathTableSearchRepository.search(query, pageable).map(rdFPathTableMapper::toDto);
    }
}
