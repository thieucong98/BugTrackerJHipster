package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.RdFPathTableRepository;
import com.mycompany.myapp.service.RdFPathTableService;
import com.mycompany.myapp.service.dto.RdFPathTableDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RdFPathTable}.
 */
@RestController
@RequestMapping("/api")
public class RdFPathTableResource {

    private final Logger log = LoggerFactory.getLogger(RdFPathTableResource.class);

    private static final String ENTITY_NAME = "rdFPathTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RdFPathTableService rdFPathTableService;

    private final RdFPathTableRepository rdFPathTableRepository;

    public RdFPathTableResource(RdFPathTableService rdFPathTableService, RdFPathTableRepository rdFPathTableRepository) {
        this.rdFPathTableService = rdFPathTableService;
        this.rdFPathTableRepository = rdFPathTableRepository;
    }

    /**
     * {@code POST  /rd-f-path-tables} : Create a new rdFPathTable.
     *
     * @param rdFPathTableDTO the rdFPathTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rdFPathTableDTO, or with status {@code 400 (Bad Request)} if the rdFPathTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rd-f-path-tables")
    public Mono<ResponseEntity<RdFPathTableDTO>> createRdFPathTable(@Valid @RequestBody RdFPathTableDTO rdFPathTableDTO)
        throws URISyntaxException {
        log.debug("REST request to save RdFPathTable : {}", rdFPathTableDTO);
        if (rdFPathTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new rdFPathTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return rdFPathTableService
            .save(rdFPathTableDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/rd-f-path-tables/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /rd-f-path-tables/:id} : Updates an existing rdFPathTable.
     *
     * @param id the id of the rdFPathTableDTO to save.
     * @param rdFPathTableDTO the rdFPathTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rdFPathTableDTO,
     * or with status {@code 400 (Bad Request)} if the rdFPathTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rdFPathTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rd-f-path-tables/{id}")
    public Mono<ResponseEntity<RdFPathTableDTO>> updateRdFPathTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RdFPathTableDTO rdFPathTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RdFPathTable : {}, {}", id, rdFPathTableDTO);
        if (rdFPathTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rdFPathTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rdFPathTableRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return rdFPathTableService
                    .update(rdFPathTableDTO)
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
     * {@code PATCH  /rd-f-path-tables/:id} : Partial updates given fields of an existing rdFPathTable, field will ignore if it is null
     *
     * @param id the id of the rdFPathTableDTO to save.
     * @param rdFPathTableDTO the rdFPathTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rdFPathTableDTO,
     * or with status {@code 400 (Bad Request)} if the rdFPathTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rdFPathTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rdFPathTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rd-f-path-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RdFPathTableDTO>> partialUpdateRdFPathTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RdFPathTableDTO rdFPathTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RdFPathTable partially : {}, {}", id, rdFPathTableDTO);
        if (rdFPathTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rdFPathTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rdFPathTableRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RdFPathTableDTO> result = rdFPathTableService.partialUpdate(rdFPathTableDTO);

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
     * {@code GET  /rd-f-path-tables} : get all the rdFPathTables.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rdFPathTables in body.
     */
    @GetMapping("/rd-f-path-tables")
    public Mono<ResponseEntity<List<RdFPathTableDTO>>> getAllRdFPathTables(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of RdFPathTables");
        return rdFPathTableService
            .countAll()
            .zipWith(rdFPathTableService.findAll(pageable).collectList())
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
     * {@code GET  /rd-f-path-tables/:id} : get the "id" rdFPathTable.
     *
     * @param id the id of the rdFPathTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rdFPathTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rd-f-path-tables/{id}")
    public Mono<ResponseEntity<RdFPathTableDTO>> getRdFPathTable(@PathVariable Long id) {
        log.debug("REST request to get RdFPathTable : {}", id);
        Mono<RdFPathTableDTO> rdFPathTableDTO = rdFPathTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rdFPathTableDTO);
    }

    /**
     * {@code DELETE  /rd-f-path-tables/:id} : delete the "id" rdFPathTable.
     *
     * @param id the id of the rdFPathTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rd-f-path-tables/{id}")
    public Mono<ResponseEntity<Void>> deleteRdFPathTable(@PathVariable Long id) {
        log.debug("REST request to delete RdFPathTable : {}", id);
        return rdFPathTableService
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
     * {@code SEARCH  /_search/rd-f-path-tables?query=:query} : search for the rdFPathTable corresponding
     * to the query.
     *
     * @param query the query of the rdFPathTable search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search/rd-f-path-tables")
    public Mono<ResponseEntity<Flux<RdFPathTableDTO>>> searchRdFPathTables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of RdFPathTables for query {}", query);
        return rdFPathTableService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(rdFPathTableService.search(query, pageable)));
    }
}
