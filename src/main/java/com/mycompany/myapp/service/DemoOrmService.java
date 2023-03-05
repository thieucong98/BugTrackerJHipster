package com.mycompany.myapp.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.DemoOrm;
import com.mycompany.myapp.repository.DemoOrmRepository;
import com.mycompany.myapp.repository.search.DemoOrmSearchRepository;
import com.mycompany.myapp.service.dto.DemoOrmDTO;
import com.mycompany.myapp.service.mapper.DemoOrmMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DemoOrm}.
 */
@Service
@Transactional
public class DemoOrmService {

    private final Logger log = LoggerFactory.getLogger(DemoOrmService.class);

    private final DemoOrmRepository demoOrmRepository;

    private final DemoOrmMapper demoOrmMapper;

    private final DemoOrmSearchRepository demoOrmSearchRepository;

    public DemoOrmService(
        DemoOrmRepository demoOrmRepository,
        DemoOrmMapper demoOrmMapper,
        DemoOrmSearchRepository demoOrmSearchRepository
    ) {
        this.demoOrmRepository = demoOrmRepository;
        this.demoOrmMapper = demoOrmMapper;
        this.demoOrmSearchRepository = demoOrmSearchRepository;
    }

    /**
     * Save a demoOrm.
     *
     * @param demoOrmDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DemoOrmDTO> save(DemoOrmDTO demoOrmDTO) {
        log.debug("Request to save DemoOrm : {}", demoOrmDTO);
        return demoOrmRepository.save(demoOrmMapper.toEntity(demoOrmDTO)).flatMap(demoOrmSearchRepository::save).map(demoOrmMapper::toDto);
    }

    /**
     * Update a demoOrm.
     *
     * @param demoOrmDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DemoOrmDTO> update(DemoOrmDTO demoOrmDTO) {
        log.debug("Request to update DemoOrm : {}", demoOrmDTO);
        return demoOrmRepository.save(demoOrmMapper.toEntity(demoOrmDTO)).flatMap(demoOrmSearchRepository::save).map(demoOrmMapper::toDto);
    }

    /**
     * Partially update a demoOrm.
     *
     * @param demoOrmDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DemoOrmDTO> partialUpdate(DemoOrmDTO demoOrmDTO) {
        log.debug("Request to partially update DemoOrm : {}", demoOrmDTO);

        return demoOrmRepository
            .findById(demoOrmDTO.getId())
            .map(existingDemoOrm -> {
                demoOrmMapper.partialUpdate(existingDemoOrm, demoOrmDTO);

                return existingDemoOrm;
            })
            .flatMap(demoOrmRepository::save)
            .flatMap(savedDemoOrm -> {
                demoOrmSearchRepository.save(savedDemoOrm);

                return Mono.just(savedDemoOrm);
            })
            .map(demoOrmMapper::toDto);
    }

    /**
     * Get all the demoOrms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DemoOrmDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DemoOrms");
        return demoOrmRepository.findAllBy(pageable).map(demoOrmMapper::toDto);
    }

    /**
     * Returns the number of demoOrms available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return demoOrmRepository.count();
    }

    /**
     * Returns the number of demoOrms available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return demoOrmSearchRepository.count();
    }

    /**
     * Get one demoOrm by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DemoOrmDTO> findOne(Long id) {
        log.debug("Request to get DemoOrm : {}", id);
        return demoOrmRepository.findById(id).map(demoOrmMapper::toDto);
    }

    /**
     * Delete the demoOrm by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DemoOrm : {}", id);
        return demoOrmRepository.deleteById(id).then(demoOrmSearchRepository.deleteById(id));
    }

    /**
     * Search for the demoOrm corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DemoOrmDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DemoOrms for query {}", query);
        return demoOrmSearchRepository.search(query, pageable).map(demoOrmMapper::toDto);
    }
}
