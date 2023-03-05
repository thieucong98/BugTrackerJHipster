package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RdFPathTable;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.RdFPathTableRepository;
import com.mycompany.myapp.repository.search.RdFPathTableSearchRepository;
import com.mycompany.myapp.service.dto.RdFPathTableDTO;
import com.mycompany.myapp.service.mapper.RdFPathTableMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link RdFPathTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RdFPathTableResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENTS_XSLT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENTS_XSLT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rd-f-path-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rd-f-path-tables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RdFPathTableRepository rdFPathTableRepository;

    @Autowired
    private RdFPathTableMapper rdFPathTableMapper;

    @Autowired
    private RdFPathTableSearchRepository rdFPathTableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private RdFPathTable rdFPathTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RdFPathTable createEntity(EntityManager em) {
        RdFPathTable rdFPathTable = new RdFPathTable()
            .path(DEFAULT_PATH)
            .contentsXslt(DEFAULT_CONTENTS_XSLT)
            .description(DEFAULT_DESCRIPTION);
        return rdFPathTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RdFPathTable createUpdatedEntity(EntityManager em) {
        RdFPathTable rdFPathTable = new RdFPathTable()
            .path(UPDATED_PATH)
            .contentsXslt(UPDATED_CONTENTS_XSLT)
            .description(UPDATED_DESCRIPTION);
        return rdFPathTable;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RdFPathTable.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        rdFPathTableSearchRepository.deleteAll().block();
        assertThat(rdFPathTableSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        rdFPathTable = createEntity(em);
    }

    @Test
    void createRdFPathTable() throws Exception {
        int databaseSizeBeforeCreate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        // Create the RdFPathTable
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        RdFPathTable testRdFPathTable = rdFPathTableList.get(rdFPathTableList.size() - 1);
        assertThat(testRdFPathTable.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testRdFPathTable.getContentsXslt()).isEqualTo(DEFAULT_CONTENTS_XSLT);
        assertThat(testRdFPathTable.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createRdFPathTableWithExistingId() throws Exception {
        // Create the RdFPathTable with an existing ID
        rdFPathTable.setId(1L);
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);

        int databaseSizeBeforeCreate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllRdFPathTables() {
        // Initialize the database
        rdFPathTableRepository.save(rdFPathTable).block();

        // Get all the rdFPathTableList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(rdFPathTable.getId().intValue()))
            .jsonPath("$.[*].path")
            .value(hasItem(DEFAULT_PATH))
            .jsonPath("$.[*].contentsXslt")
            .value(hasItem(DEFAULT_CONTENTS_XSLT))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getRdFPathTable() {
        // Initialize the database
        rdFPathTableRepository.save(rdFPathTable).block();

        // Get the rdFPathTable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, rdFPathTable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(rdFPathTable.getId().intValue()))
            .jsonPath("$.path")
            .value(is(DEFAULT_PATH))
            .jsonPath("$.contentsXslt")
            .value(is(DEFAULT_CONTENTS_XSLT))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingRdFPathTable() {
        // Get the rdFPathTable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRdFPathTable() throws Exception {
        // Initialize the database
        rdFPathTableRepository.save(rdFPathTable).block();

        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();
        rdFPathTableSearchRepository.save(rdFPathTable).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());

        // Update the rdFPathTable
        RdFPathTable updatedRdFPathTable = rdFPathTableRepository.findById(rdFPathTable.getId()).block();
        updatedRdFPathTable.path(UPDATED_PATH).contentsXslt(UPDATED_CONTENTS_XSLT).description(UPDATED_DESCRIPTION);
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(updatedRdFPathTable);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rdFPathTableDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        RdFPathTable testRdFPathTable = rdFPathTableList.get(rdFPathTableList.size() - 1);
        assertThat(testRdFPathTable.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testRdFPathTable.getContentsXslt()).isEqualTo(UPDATED_CONTENTS_XSLT);
        assertThat(testRdFPathTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<RdFPathTable> rdFPathTableSearchList = IterableUtils.toList(
                    rdFPathTableSearchRepository.findAll().collectList().block()
                );
                RdFPathTable testRdFPathTableSearch = rdFPathTableSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testRdFPathTableSearch.getPath()).isEqualTo(UPDATED_PATH);
                assertThat(testRdFPathTableSearch.getContentsXslt()).isEqualTo(UPDATED_CONTENTS_XSLT);
                assertThat(testRdFPathTableSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
            });
    }

    @Test
    void putNonExistingRdFPathTable() throws Exception {
        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        rdFPathTable.setId(count.incrementAndGet());

        // Create the RdFPathTable
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rdFPathTableDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchRdFPathTable() throws Exception {
        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        rdFPathTable.setId(count.incrementAndGet());

        // Create the RdFPathTable
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamRdFPathTable() throws Exception {
        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        rdFPathTable.setId(count.incrementAndGet());

        // Create the RdFPathTable
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateRdFPathTableWithPatch() throws Exception {
        // Initialize the database
        rdFPathTableRepository.save(rdFPathTable).block();

        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();

        // Update the rdFPathTable using partial update
        RdFPathTable partialUpdatedRdFPathTable = new RdFPathTable();
        partialUpdatedRdFPathTable.setId(rdFPathTable.getId());

        partialUpdatedRdFPathTable.contentsXslt(UPDATED_CONTENTS_XSLT).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRdFPathTable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRdFPathTable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        RdFPathTable testRdFPathTable = rdFPathTableList.get(rdFPathTableList.size() - 1);
        assertThat(testRdFPathTable.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testRdFPathTable.getContentsXslt()).isEqualTo(UPDATED_CONTENTS_XSLT);
        assertThat(testRdFPathTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateRdFPathTableWithPatch() throws Exception {
        // Initialize the database
        rdFPathTableRepository.save(rdFPathTable).block();

        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();

        // Update the rdFPathTable using partial update
        RdFPathTable partialUpdatedRdFPathTable = new RdFPathTable();
        partialUpdatedRdFPathTable.setId(rdFPathTable.getId());

        partialUpdatedRdFPathTable.path(UPDATED_PATH).contentsXslt(UPDATED_CONTENTS_XSLT).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRdFPathTable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRdFPathTable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        RdFPathTable testRdFPathTable = rdFPathTableList.get(rdFPathTableList.size() - 1);
        assertThat(testRdFPathTable.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testRdFPathTable.getContentsXslt()).isEqualTo(UPDATED_CONTENTS_XSLT);
        assertThat(testRdFPathTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingRdFPathTable() throws Exception {
        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        rdFPathTable.setId(count.incrementAndGet());

        // Create the RdFPathTable
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, rdFPathTableDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchRdFPathTable() throws Exception {
        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        rdFPathTable.setId(count.incrementAndGet());

        // Create the RdFPathTable
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamRdFPathTable() throws Exception {
        int databaseSizeBeforeUpdate = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        rdFPathTable.setId(count.incrementAndGet());

        // Create the RdFPathTable
        RdFPathTableDTO rdFPathTableDTO = rdFPathTableMapper.toDto(rdFPathTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFPathTableDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RdFPathTable in the database
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteRdFPathTable() {
        // Initialize the database
        rdFPathTableRepository.save(rdFPathTable).block();
        rdFPathTableRepository.save(rdFPathTable).block();
        rdFPathTableSearchRepository.save(rdFPathTable).block();

        int databaseSizeBeforeDelete = rdFPathTableRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the rdFPathTable
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, rdFPathTable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RdFPathTable> rdFPathTableList = rdFPathTableRepository.findAll().collectList().block();
        assertThat(rdFPathTableList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFPathTableSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchRdFPathTable() {
        // Initialize the database
        rdFPathTable = rdFPathTableRepository.save(rdFPathTable).block();
        rdFPathTableSearchRepository.save(rdFPathTable).block();

        // Search the rdFPathTable
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + rdFPathTable.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(rdFPathTable.getId().intValue()))
            .jsonPath("$.[*].path")
            .value(hasItem(DEFAULT_PATH))
            .jsonPath("$.[*].contentsXslt")
            .value(hasItem(DEFAULT_CONTENTS_XSLT))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }
}
