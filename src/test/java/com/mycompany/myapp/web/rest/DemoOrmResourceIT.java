package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DemoOrm;
import com.mycompany.myapp.repository.DemoOrmRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.search.DemoOrmSearchRepository;
import com.mycompany.myapp.service.dto.DemoOrmDTO;
import com.mycompany.myapp.service.mapper.DemoOrmMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DemoOrmResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DemoOrmResourceIT {

    private static final String DEFAULT_CODE_ID = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CODE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_VALUE_JA = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_VALUE_JA = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_VALUE_EN = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_VALUE_EN = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_VALUE_PAIR = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_VALUE_PAIR = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_CODE_ID = "AAAAA";
    private static final String UPDATED_PARENT_CODE_ID = "BBBBB";

    private static final String DEFAULT_PARENT_ITEM_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_ITEM_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_ITEM_KEY_BACKUP = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_ITEM_KEY_BACKUP = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_ITEM_KEY_NEW = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_ITEM_KEY_NEW = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/demo-orms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/demo-orms";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemoOrmRepository demoOrmRepository;

    @Autowired
    private DemoOrmMapper demoOrmMapper;

    @Autowired
    private DemoOrmSearchRepository demoOrmSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DemoOrm demoOrm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemoOrm createEntity(EntityManager em) {
        DemoOrm demoOrm = new DemoOrm()
            .codeId(DEFAULT_CODE_ID)
            .codeName(DEFAULT_CODE_NAME)
            .itemKey(DEFAULT_ITEM_KEY)
            .itemValueJa(DEFAULT_ITEM_VALUE_JA)
            .itemValueEn(DEFAULT_ITEM_VALUE_EN)
            .itemValuePair(DEFAULT_ITEM_VALUE_PAIR)
            .parentCodeId(DEFAULT_PARENT_CODE_ID)
            .parentItemKey(DEFAULT_PARENT_ITEM_KEY)
            .parentItemKeyBackup(DEFAULT_PARENT_ITEM_KEY_BACKUP)
            .parentItemKeyNew(DEFAULT_PARENT_ITEM_KEY_NEW)
            .createdTimestamp(DEFAULT_CREATED_TIMESTAMP)
            .updatedTimestamp(DEFAULT_UPDATED_TIMESTAMP);
        return demoOrm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemoOrm createUpdatedEntity(EntityManager em) {
        DemoOrm demoOrm = new DemoOrm()
            .codeId(UPDATED_CODE_ID)
            .codeName(UPDATED_CODE_NAME)
            .itemKey(UPDATED_ITEM_KEY)
            .itemValueJa(UPDATED_ITEM_VALUE_JA)
            .itemValueEn(UPDATED_ITEM_VALUE_EN)
            .itemValuePair(UPDATED_ITEM_VALUE_PAIR)
            .parentCodeId(UPDATED_PARENT_CODE_ID)
            .parentItemKey(UPDATED_PARENT_ITEM_KEY)
            .parentItemKeyBackup(UPDATED_PARENT_ITEM_KEY_BACKUP)
            .parentItemKeyNew(UPDATED_PARENT_ITEM_KEY_NEW)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .updatedTimestamp(UPDATED_UPDATED_TIMESTAMP);
        return demoOrm;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DemoOrm.class).block();
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
        demoOrmSearchRepository.deleteAll().block();
        assertThat(demoOrmSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        demoOrm = createEntity(em);
    }

    @Test
    void createDemoOrm() throws Exception {
        int databaseSizeBeforeCreate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        // Create the DemoOrm
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        DemoOrm testDemoOrm = demoOrmList.get(demoOrmList.size() - 1);
        assertThat(testDemoOrm.getCodeId()).isEqualTo(DEFAULT_CODE_ID);
        assertThat(testDemoOrm.getCodeName()).isEqualTo(DEFAULT_CODE_NAME);
        assertThat(testDemoOrm.getItemKey()).isEqualTo(DEFAULT_ITEM_KEY);
        assertThat(testDemoOrm.getItemValueJa()).isEqualTo(DEFAULT_ITEM_VALUE_JA);
        assertThat(testDemoOrm.getItemValueEn()).isEqualTo(DEFAULT_ITEM_VALUE_EN);
        assertThat(testDemoOrm.getItemValuePair()).isEqualTo(DEFAULT_ITEM_VALUE_PAIR);
        assertThat(testDemoOrm.getParentCodeId()).isEqualTo(DEFAULT_PARENT_CODE_ID);
        assertThat(testDemoOrm.getParentItemKey()).isEqualTo(DEFAULT_PARENT_ITEM_KEY);
        assertThat(testDemoOrm.getParentItemKeyBackup()).isEqualTo(DEFAULT_PARENT_ITEM_KEY_BACKUP);
        assertThat(testDemoOrm.getParentItemKeyNew()).isEqualTo(DEFAULT_PARENT_ITEM_KEY_NEW);
        assertThat(testDemoOrm.getCreatedTimestamp()).isEqualTo(DEFAULT_CREATED_TIMESTAMP);
        assertThat(testDemoOrm.getUpdatedTimestamp()).isEqualTo(DEFAULT_UPDATED_TIMESTAMP);
    }

    @Test
    void createDemoOrmWithExistingId() throws Exception {
        // Create the DemoOrm with an existing ID
        demoOrm.setId(1L);
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        int databaseSizeBeforeCreate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCodeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        // set the field null
        demoOrm.setCodeId(null);

        // Create the DemoOrm, which fails.
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCodeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        // set the field null
        demoOrm.setCodeName(null);

        // Create the DemoOrm, which fails.
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkItemKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        // set the field null
        demoOrm.setItemKey(null);

        // Create the DemoOrm, which fails.
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllDemoOrms() {
        // Initialize the database
        demoOrmRepository.save(demoOrm).block();

        // Get all the demoOrmList
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
            .value(hasItem(demoOrm.getId().intValue()))
            .jsonPath("$.[*].codeId")
            .value(hasItem(DEFAULT_CODE_ID))
            .jsonPath("$.[*].codeName")
            .value(hasItem(DEFAULT_CODE_NAME))
            .jsonPath("$.[*].itemKey")
            .value(hasItem(DEFAULT_ITEM_KEY))
            .jsonPath("$.[*].itemValueJa")
            .value(hasItem(DEFAULT_ITEM_VALUE_JA))
            .jsonPath("$.[*].itemValueEn")
            .value(hasItem(DEFAULT_ITEM_VALUE_EN))
            .jsonPath("$.[*].itemValuePair")
            .value(hasItem(DEFAULT_ITEM_VALUE_PAIR))
            .jsonPath("$.[*].parentCodeId")
            .value(hasItem(DEFAULT_PARENT_CODE_ID))
            .jsonPath("$.[*].parentItemKey")
            .value(hasItem(DEFAULT_PARENT_ITEM_KEY))
            .jsonPath("$.[*].parentItemKeyBackup")
            .value(hasItem(DEFAULT_PARENT_ITEM_KEY_BACKUP))
            .jsonPath("$.[*].parentItemKeyNew")
            .value(hasItem(DEFAULT_PARENT_ITEM_KEY_NEW))
            .jsonPath("$.[*].createdTimestamp")
            .value(hasItem(DEFAULT_CREATED_TIMESTAMP.toString()))
            .jsonPath("$.[*].updatedTimestamp")
            .value(hasItem(DEFAULT_UPDATED_TIMESTAMP.toString()));
    }

    @Test
    void getDemoOrm() {
        // Initialize the database
        demoOrmRepository.save(demoOrm).block();

        // Get the demoOrm
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, demoOrm.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(demoOrm.getId().intValue()))
            .jsonPath("$.codeId")
            .value(is(DEFAULT_CODE_ID))
            .jsonPath("$.codeName")
            .value(is(DEFAULT_CODE_NAME))
            .jsonPath("$.itemKey")
            .value(is(DEFAULT_ITEM_KEY))
            .jsonPath("$.itemValueJa")
            .value(is(DEFAULT_ITEM_VALUE_JA))
            .jsonPath("$.itemValueEn")
            .value(is(DEFAULT_ITEM_VALUE_EN))
            .jsonPath("$.itemValuePair")
            .value(is(DEFAULT_ITEM_VALUE_PAIR))
            .jsonPath("$.parentCodeId")
            .value(is(DEFAULT_PARENT_CODE_ID))
            .jsonPath("$.parentItemKey")
            .value(is(DEFAULT_PARENT_ITEM_KEY))
            .jsonPath("$.parentItemKeyBackup")
            .value(is(DEFAULT_PARENT_ITEM_KEY_BACKUP))
            .jsonPath("$.parentItemKeyNew")
            .value(is(DEFAULT_PARENT_ITEM_KEY_NEW))
            .jsonPath("$.createdTimestamp")
            .value(is(DEFAULT_CREATED_TIMESTAMP.toString()))
            .jsonPath("$.updatedTimestamp")
            .value(is(DEFAULT_UPDATED_TIMESTAMP.toString()));
    }

    @Test
    void getNonExistingDemoOrm() {
        // Get the demoOrm
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDemoOrm() throws Exception {
        // Initialize the database
        demoOrmRepository.save(demoOrm).block();

        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();
        demoOrmSearchRepository.save(demoOrm).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());

        // Update the demoOrm
        DemoOrm updatedDemoOrm = demoOrmRepository.findById(demoOrm.getId()).block();
        updatedDemoOrm
            .codeId(UPDATED_CODE_ID)
            .codeName(UPDATED_CODE_NAME)
            .itemKey(UPDATED_ITEM_KEY)
            .itemValueJa(UPDATED_ITEM_VALUE_JA)
            .itemValueEn(UPDATED_ITEM_VALUE_EN)
            .itemValuePair(UPDATED_ITEM_VALUE_PAIR)
            .parentCodeId(UPDATED_PARENT_CODE_ID)
            .parentItemKey(UPDATED_PARENT_ITEM_KEY)
            .parentItemKeyBackup(UPDATED_PARENT_ITEM_KEY_BACKUP)
            .parentItemKeyNew(UPDATED_PARENT_ITEM_KEY_NEW)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .updatedTimestamp(UPDATED_UPDATED_TIMESTAMP);
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(updatedDemoOrm);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, demoOrmDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        DemoOrm testDemoOrm = demoOrmList.get(demoOrmList.size() - 1);
        assertThat(testDemoOrm.getCodeId()).isEqualTo(UPDATED_CODE_ID);
        assertThat(testDemoOrm.getCodeName()).isEqualTo(UPDATED_CODE_NAME);
        assertThat(testDemoOrm.getItemKey()).isEqualTo(UPDATED_ITEM_KEY);
        assertThat(testDemoOrm.getItemValueJa()).isEqualTo(UPDATED_ITEM_VALUE_JA);
        assertThat(testDemoOrm.getItemValueEn()).isEqualTo(UPDATED_ITEM_VALUE_EN);
        assertThat(testDemoOrm.getItemValuePair()).isEqualTo(UPDATED_ITEM_VALUE_PAIR);
        assertThat(testDemoOrm.getParentCodeId()).isEqualTo(UPDATED_PARENT_CODE_ID);
        assertThat(testDemoOrm.getParentItemKey()).isEqualTo(UPDATED_PARENT_ITEM_KEY);
        assertThat(testDemoOrm.getParentItemKeyBackup()).isEqualTo(UPDATED_PARENT_ITEM_KEY_BACKUP);
        assertThat(testDemoOrm.getParentItemKeyNew()).isEqualTo(UPDATED_PARENT_ITEM_KEY_NEW);
        assertThat(testDemoOrm.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
        assertThat(testDemoOrm.getUpdatedTimestamp()).isEqualTo(UPDATED_UPDATED_TIMESTAMP);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DemoOrm> demoOrmSearchList = IterableUtils.toList(demoOrmSearchRepository.findAll().collectList().block());
                DemoOrm testDemoOrmSearch = demoOrmSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testDemoOrmSearch.getCodeId()).isEqualTo(UPDATED_CODE_ID);
                assertThat(testDemoOrmSearch.getCodeName()).isEqualTo(UPDATED_CODE_NAME);
                assertThat(testDemoOrmSearch.getItemKey()).isEqualTo(UPDATED_ITEM_KEY);
                assertThat(testDemoOrmSearch.getItemValueJa()).isEqualTo(UPDATED_ITEM_VALUE_JA);
                assertThat(testDemoOrmSearch.getItemValueEn()).isEqualTo(UPDATED_ITEM_VALUE_EN);
                assertThat(testDemoOrmSearch.getItemValuePair()).isEqualTo(UPDATED_ITEM_VALUE_PAIR);
                assertThat(testDemoOrmSearch.getParentCodeId()).isEqualTo(UPDATED_PARENT_CODE_ID);
                assertThat(testDemoOrmSearch.getParentItemKey()).isEqualTo(UPDATED_PARENT_ITEM_KEY);
                assertThat(testDemoOrmSearch.getParentItemKeyBackup()).isEqualTo(UPDATED_PARENT_ITEM_KEY_BACKUP);
                assertThat(testDemoOrmSearch.getParentItemKeyNew()).isEqualTo(UPDATED_PARENT_ITEM_KEY_NEW);
                assertThat(testDemoOrmSearch.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
                assertThat(testDemoOrmSearch.getUpdatedTimestamp()).isEqualTo(UPDATED_UPDATED_TIMESTAMP);
            });
    }

    @Test
    void putNonExistingDemoOrm() throws Exception {
        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        demoOrm.setId(count.incrementAndGet());

        // Create the DemoOrm
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, demoOrmDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchDemoOrm() throws Exception {
        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        demoOrm.setId(count.incrementAndGet());

        // Create the DemoOrm
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamDemoOrm() throws Exception {
        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        demoOrm.setId(count.incrementAndGet());

        // Create the DemoOrm
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateDemoOrmWithPatch() throws Exception {
        // Initialize the database
        demoOrmRepository.save(demoOrm).block();

        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();

        // Update the demoOrm using partial update
        DemoOrm partialUpdatedDemoOrm = new DemoOrm();
        partialUpdatedDemoOrm.setId(demoOrm.getId());

        partialUpdatedDemoOrm
            .itemValueJa(UPDATED_ITEM_VALUE_JA)
            .itemValuePair(UPDATED_ITEM_VALUE_PAIR)
            .parentCodeId(UPDATED_PARENT_CODE_ID)
            .parentItemKeyNew(UPDATED_PARENT_ITEM_KEY_NEW)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDemoOrm.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDemoOrm))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        DemoOrm testDemoOrm = demoOrmList.get(demoOrmList.size() - 1);
        assertThat(testDemoOrm.getCodeId()).isEqualTo(DEFAULT_CODE_ID);
        assertThat(testDemoOrm.getCodeName()).isEqualTo(DEFAULT_CODE_NAME);
        assertThat(testDemoOrm.getItemKey()).isEqualTo(DEFAULT_ITEM_KEY);
        assertThat(testDemoOrm.getItemValueJa()).isEqualTo(UPDATED_ITEM_VALUE_JA);
        assertThat(testDemoOrm.getItemValueEn()).isEqualTo(DEFAULT_ITEM_VALUE_EN);
        assertThat(testDemoOrm.getItemValuePair()).isEqualTo(UPDATED_ITEM_VALUE_PAIR);
        assertThat(testDemoOrm.getParentCodeId()).isEqualTo(UPDATED_PARENT_CODE_ID);
        assertThat(testDemoOrm.getParentItemKey()).isEqualTo(DEFAULT_PARENT_ITEM_KEY);
        assertThat(testDemoOrm.getParentItemKeyBackup()).isEqualTo(DEFAULT_PARENT_ITEM_KEY_BACKUP);
        assertThat(testDemoOrm.getParentItemKeyNew()).isEqualTo(UPDATED_PARENT_ITEM_KEY_NEW);
        assertThat(testDemoOrm.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
        assertThat(testDemoOrm.getUpdatedTimestamp()).isEqualTo(DEFAULT_UPDATED_TIMESTAMP);
    }

    @Test
    void fullUpdateDemoOrmWithPatch() throws Exception {
        // Initialize the database
        demoOrmRepository.save(demoOrm).block();

        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();

        // Update the demoOrm using partial update
        DemoOrm partialUpdatedDemoOrm = new DemoOrm();
        partialUpdatedDemoOrm.setId(demoOrm.getId());

        partialUpdatedDemoOrm
            .codeId(UPDATED_CODE_ID)
            .codeName(UPDATED_CODE_NAME)
            .itemKey(UPDATED_ITEM_KEY)
            .itemValueJa(UPDATED_ITEM_VALUE_JA)
            .itemValueEn(UPDATED_ITEM_VALUE_EN)
            .itemValuePair(UPDATED_ITEM_VALUE_PAIR)
            .parentCodeId(UPDATED_PARENT_CODE_ID)
            .parentItemKey(UPDATED_PARENT_ITEM_KEY)
            .parentItemKeyBackup(UPDATED_PARENT_ITEM_KEY_BACKUP)
            .parentItemKeyNew(UPDATED_PARENT_ITEM_KEY_NEW)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .updatedTimestamp(UPDATED_UPDATED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDemoOrm.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDemoOrm))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        DemoOrm testDemoOrm = demoOrmList.get(demoOrmList.size() - 1);
        assertThat(testDemoOrm.getCodeId()).isEqualTo(UPDATED_CODE_ID);
        assertThat(testDemoOrm.getCodeName()).isEqualTo(UPDATED_CODE_NAME);
        assertThat(testDemoOrm.getItemKey()).isEqualTo(UPDATED_ITEM_KEY);
        assertThat(testDemoOrm.getItemValueJa()).isEqualTo(UPDATED_ITEM_VALUE_JA);
        assertThat(testDemoOrm.getItemValueEn()).isEqualTo(UPDATED_ITEM_VALUE_EN);
        assertThat(testDemoOrm.getItemValuePair()).isEqualTo(UPDATED_ITEM_VALUE_PAIR);
        assertThat(testDemoOrm.getParentCodeId()).isEqualTo(UPDATED_PARENT_CODE_ID);
        assertThat(testDemoOrm.getParentItemKey()).isEqualTo(UPDATED_PARENT_ITEM_KEY);
        assertThat(testDemoOrm.getParentItemKeyBackup()).isEqualTo(UPDATED_PARENT_ITEM_KEY_BACKUP);
        assertThat(testDemoOrm.getParentItemKeyNew()).isEqualTo(UPDATED_PARENT_ITEM_KEY_NEW);
        assertThat(testDemoOrm.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
        assertThat(testDemoOrm.getUpdatedTimestamp()).isEqualTo(UPDATED_UPDATED_TIMESTAMP);
    }

    @Test
    void patchNonExistingDemoOrm() throws Exception {
        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        demoOrm.setId(count.incrementAndGet());

        // Create the DemoOrm
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, demoOrmDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchDemoOrm() throws Exception {
        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        demoOrm.setId(count.incrementAndGet());

        // Create the DemoOrm
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamDemoOrm() throws Exception {
        int databaseSizeBeforeUpdate = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        demoOrm.setId(count.incrementAndGet());

        // Create the DemoOrm
        DemoOrmDTO demoOrmDTO = demoOrmMapper.toDto(demoOrm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(demoOrmDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DemoOrm in the database
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteDemoOrm() {
        // Initialize the database
        demoOrmRepository.save(demoOrm).block();
        demoOrmRepository.save(demoOrm).block();
        demoOrmSearchRepository.save(demoOrm).block();

        int databaseSizeBeforeDelete = demoOrmRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the demoOrm
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, demoOrm.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DemoOrm> demoOrmList = demoOrmRepository.findAll().collectList().block();
        assertThat(demoOrmList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(demoOrmSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchDemoOrm() {
        // Initialize the database
        demoOrm = demoOrmRepository.save(demoOrm).block();
        demoOrmSearchRepository.save(demoOrm).block();

        // Search the demoOrm
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + demoOrm.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(demoOrm.getId().intValue()))
            .jsonPath("$.[*].codeId")
            .value(hasItem(DEFAULT_CODE_ID))
            .jsonPath("$.[*].codeName")
            .value(hasItem(DEFAULT_CODE_NAME))
            .jsonPath("$.[*].itemKey")
            .value(hasItem(DEFAULT_ITEM_KEY))
            .jsonPath("$.[*].itemValueJa")
            .value(hasItem(DEFAULT_ITEM_VALUE_JA))
            .jsonPath("$.[*].itemValueEn")
            .value(hasItem(DEFAULT_ITEM_VALUE_EN))
            .jsonPath("$.[*].itemValuePair")
            .value(hasItem(DEFAULT_ITEM_VALUE_PAIR))
            .jsonPath("$.[*].parentCodeId")
            .value(hasItem(DEFAULT_PARENT_CODE_ID))
            .jsonPath("$.[*].parentItemKey")
            .value(hasItem(DEFAULT_PARENT_ITEM_KEY))
            .jsonPath("$.[*].parentItemKeyBackup")
            .value(hasItem(DEFAULT_PARENT_ITEM_KEY_BACKUP))
            .jsonPath("$.[*].parentItemKeyNew")
            .value(hasItem(DEFAULT_PARENT_ITEM_KEY_NEW))
            .jsonPath("$.[*].createdTimestamp")
            .value(hasItem(DEFAULT_CREATED_TIMESTAMP.toString()))
            .jsonPath("$.[*].updatedTimestamp")
            .value(hasItem(DEFAULT_UPDATED_TIMESTAMP.toString()));
    }
}
