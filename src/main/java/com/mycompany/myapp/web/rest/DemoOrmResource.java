package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DemoOrmRepository;
import com.mycompany.myapp.service.DemoOrmService;
import com.mycompany.myapp.service.dto.DemoOrmDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.DemoOrm}.
 */
@RestController
@RequestMapping("/api")
public class DemoOrmResource {

    private final Logger log = LoggerFactory.getLogger(DemoOrmResource.class);

    private static final String ENTITY_NAME = "demoOrm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemoOrmService demoOrmService;

    private final DemoOrmRepository demoOrmRepository;

    public DemoOrmResource(DemoOrmService demoOrmService, DemoOrmRepository demoOrmRepository) {
        this.demoOrmService = demoOrmService;
        this.demoOrmRepository = demoOrmRepository;
    }

    /**
     * {@code POST  /demo-orms} : Create a new demoOrm.
     *
     * @param demoOrmDTO the demoOrmDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demoOrmDTO, or with status {@code 400 (Bad Request)} if the demoOrm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demo-orms")
    public Mono<ResponseEntity<DemoOrmDTO>> createDemoOrm(@Valid @RequestBody DemoOrmDTO demoOrmDTO) throws URISyntaxException {
        log.debug("REST request to save DemoOrm : {}", demoOrmDTO);
        if (demoOrmDTO.getId() != null) {
            throw new BadRequestAlertException("A new demoOrm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return demoOrmService
            .save(demoOrmDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/demo-orms/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /demo-orms/:id} : Updates an existing demoOrm.
     *
     * @param id the id of the demoOrmDTO to save.
     * @param demoOrmDTO the demoOrmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demoOrmDTO,
     * or with status {@code 400 (Bad Request)} if the demoOrmDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demoOrmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demo-orms/{id}")
    public Mono<ResponseEntity<DemoOrmDTO>> updateDemoOrm(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemoOrmDTO demoOrmDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DemoOrm : {}, {}", id, demoOrmDTO);
        if (demoOrmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demoOrmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return demoOrmRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return demoOrmService
                    .update(demoOrmDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /demo-orms/:id} : Partial updates given fields of an existing demoOrm, field will ignore if it is null
     *
     * @param id the id of the demoOrmDTO to save.
     * @param demoOrmDTO the demoOrmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demoOrmDTO,
     * or with status {@code 400 (Bad Request)} if the demoOrmDTO is not valid,
     * or with status {@code 404 (Not Found)} if the demoOrmDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the demoOrmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demo-orms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DemoOrmDTO>> partialUpdateDemoOrm(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemoOrmDTO demoOrmDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemoOrm partially : {}, {}", id, demoOrmDTO);
        if (demoOrmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demoOrmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return demoOrmRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DemoOrmDTO> result = demoOrmService.partialUpdate(demoOrmDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /demo-orms} : get all the demoOrms.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demoOrms in body.
     */
    @GetMapping("/demo-orms")
    public Mono<ResponseEntity<List<DemoOrmDTO>>> getAllDemoOrms(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of DemoOrms");
        return demoOrmService
            .countAll()
            .zipWith(demoOrmService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /demo-orms/:id} : get the "id" demoOrm.
     *
     * @param id the id of the demoOrmDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demoOrmDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demo-orms/{id}")
    public Mono<ResponseEntity<DemoOrmDTO>> getDemoOrm(@PathVariable Long id) {
        log.debug("REST request to get DemoOrm : {}", id);
        Mono<DemoOrmDTO> demoOrmDTO = demoOrmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demoOrmDTO);
    }

    /**
     * {@code DELETE  /demo-orms/:id} : delete the "id" demoOrm.
     *
     * @param id the id of the demoOrmDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demo-orms/{id}")
    public Mono<ResponseEntity<Void>> deleteDemoOrm(@PathVariable Long id) {
        log.debug("REST request to delete DemoOrm : {}", id);
        return demoOrmService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /_search/demo-orms?query=:query} : search for the demoOrm corresponding
     * to the query.
     *
     * @param query the query of the demoOrm search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search/demo-orms")
    public Mono<ResponseEntity<Flux<DemoOrmDTO>>> searchDemoOrms(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of DemoOrms for query {}", query);
        return demoOrmService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(demoOrmService.search(query, pageable)));
    }
}
