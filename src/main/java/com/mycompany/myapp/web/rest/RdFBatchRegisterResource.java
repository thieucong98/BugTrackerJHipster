package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.RdFBatchRegisterRepository;
import com.mycompany.myapp.service.RdFBatchRegisterService;
import com.mycompany.myapp.service.dto.RdFBatchRegisterDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RdFBatchRegister}.
 */
@RestController
@RequestMapping("/api")
public class RdFBatchRegisterResource {

    private final Logger log = LoggerFactory.getLogger(RdFBatchRegisterResource.class);

    private static final String ENTITY_NAME = "rdFBatchRegister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RdFBatchRegisterService rdFBatchRegisterService;

    private final RdFBatchRegisterRepository rdFBatchRegisterRepository;

    public RdFBatchRegisterResource(
        RdFBatchRegisterService rdFBatchRegisterService,
        RdFBatchRegisterRepository rdFBatchRegisterRepository
    ) {
        this.rdFBatchRegisterService = rdFBatchRegisterService;
        this.rdFBatchRegisterRepository = rdFBatchRegisterRepository;
    }

    /**
     * {@code POST  /rd-f-batch-registers} : Create a new rdFBatchRegister.
     *
     * @param rdFBatchRegisterDTO the rdFBatchRegisterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rdFBatchRegisterDTO, or with status {@code 400 (Bad Request)} if the rdFBatchRegister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rd-f-batch-registers")
    public Mono<ResponseEntity<RdFBatchRegisterDTO>> createRdFBatchRegister(@Valid @RequestBody RdFBatchRegisterDTO rdFBatchRegisterDTO)
        throws URISyntaxException {
        log.debug("REST request to save RdFBatchRegister : {}", rdFBatchRegisterDTO);
        if (rdFBatchRegisterDTO.getId() != null) {
            throw new BadRequestAlertException("A new rdFBatchRegister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return rdFBatchRegisterService
            .save(rdFBatchRegisterDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/rd-f-batch-registers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /rd-f-batch-registers/:id} : Updates an existing rdFBatchRegister.
     *
     * @param id the id of the rdFBatchRegisterDTO to save.
     * @param rdFBatchRegisterDTO the rdFBatchRegisterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rdFBatchRegisterDTO,
     * or with status {@code 400 (Bad Request)} if the rdFBatchRegisterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rdFBatchRegisterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rd-f-batch-registers/{id}")
    public Mono<ResponseEntity<RdFBatchRegisterDTO>> updateRdFBatchRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RdFBatchRegisterDTO rdFBatchRegisterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RdFBatchRegister : {}, {}", id, rdFBatchRegisterDTO);
        if (rdFBatchRegisterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rdFBatchRegisterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rdFBatchRegisterRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return rdFBatchRegisterService
                    .update(rdFBatchRegisterDTO)
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
     * {@code PATCH  /rd-f-batch-registers/:id} : Partial updates given fields of an existing rdFBatchRegister, field will ignore if it is null
     *
     * @param id the id of the rdFBatchRegisterDTO to save.
     * @param rdFBatchRegisterDTO the rdFBatchRegisterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rdFBatchRegisterDTO,
     * or with status {@code 400 (Bad Request)} if the rdFBatchRegisterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rdFBatchRegisterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rdFBatchRegisterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rd-f-batch-registers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RdFBatchRegisterDTO>> partialUpdateRdFBatchRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RdFBatchRegisterDTO rdFBatchRegisterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RdFBatchRegister partially : {}, {}", id, rdFBatchRegisterDTO);
        if (rdFBatchRegisterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rdFBatchRegisterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rdFBatchRegisterRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RdFBatchRegisterDTO> result = rdFBatchRegisterService.partialUpdate(rdFBatchRegisterDTO);

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
     * {@code GET  /rd-f-batch-registers} : get all the rdFBatchRegisters.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rdFBatchRegisters in body.
     */
    @GetMapping("/rd-f-batch-registers")
    public Mono<ResponseEntity<List<RdFBatchRegisterDTO>>> getAllRdFBatchRegisters(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of RdFBatchRegisters");
        return rdFBatchRegisterService
            .countAll()
            .zipWith(rdFBatchRegisterService.findAll(pageable).collectList())
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
     * {@code GET  /rd-f-batch-registers/:id} : get the "id" rdFBatchRegister.
     *
     * @param id the id of the rdFBatchRegisterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rdFBatchRegisterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rd-f-batch-registers/{id}")
    public Mono<ResponseEntity<RdFBatchRegisterDTO>> getRdFBatchRegister(@PathVariable Long id) {
        log.debug("REST request to get RdFBatchRegister : {}", id);
        Mono<RdFBatchRegisterDTO> rdFBatchRegisterDTO = rdFBatchRegisterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rdFBatchRegisterDTO);
    }

    /**
     * {@code DELETE  /rd-f-batch-registers/:id} : delete the "id" rdFBatchRegister.
     *
     * @param id the id of the rdFBatchRegisterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rd-f-batch-registers/{id}")
    public Mono<ResponseEntity<Void>> deleteRdFBatchRegister(@PathVariable Long id) {
        log.debug("REST request to delete RdFBatchRegister : {}", id);
        return rdFBatchRegisterService
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
     * {@code SEARCH  /_search/rd-f-batch-registers?query=:query} : search for the rdFBatchRegister corresponding
     * to the query.
     *
     * @param query the query of the rdFBatchRegister search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search/rd-f-batch-registers")
    public Mono<ResponseEntity<Flux<RdFBatchRegisterDTO>>> searchRdFBatchRegisters(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of RdFBatchRegisters for query {}", query);
        return rdFBatchRegisterService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(rdFBatchRegisterService.search(query, pageable)));
    }
}
