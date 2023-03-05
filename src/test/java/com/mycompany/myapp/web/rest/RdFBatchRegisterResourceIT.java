package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RdFBatchRegister;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.RdFBatchRegisterRepository;
import com.mycompany.myapp.repository.search.RdFBatchRegisterSearchRepository;
import com.mycompany.myapp.service.dto.RdFBatchRegisterDTO;
import com.mycompany.myapp.service.mapper.RdFBatchRegisterMapper;
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
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link RdFBatchRegisterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RdFBatchRegisterResourceIT {

    private static final String DEFAULT_WORKFLOW_ID = "AAAAAAAAAA";
    private static final String UPDATED_WORKFLOW_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DBNAME = "AAAAAAAAAA";
    private static final String UPDATED_DBNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FEED_ID = "AAAAAAAAAA";
    private static final String UPDATED_FEED_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FUNC = "AAAAAAAAAA";
    private static final String UPDATED_FUNC = "BBBBBBBBBB";

    private static final String DEFAULT_REQ_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_REQ_DATETIME = "BBBBBBBBBB";

    private static final String DEFAULT_EXEC_USER = "AAAAAAAAAA";
    private static final String UPDATED_EXEC_USER = "BBBBBBBBBB";

    private static final String DEFAULT_SYSTEM_IDS = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM_IDS = "BBBBBBBBBB";

    private static final String DEFAULT_MODE = "AAAAAAAAAA";
    private static final String UPDATED_MODE = "BBBBBBBBBB";

    private static final String DEFAULT_DONE = "A";
    private static final String UPDATED_DONE = "B";

    private static final Instant DEFAULT_CREATED_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rd-f-batch-registers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rd-f-batch-registers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RdFBatchRegisterRepository rdFBatchRegisterRepository;

    @Autowired
    private RdFBatchRegisterMapper rdFBatchRegisterMapper;

    @Autowired
    private RdFBatchRegisterSearchRepository rdFBatchRegisterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private RdFBatchRegister rdFBatchRegister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RdFBatchRegister createEntity(EntityManager em) {
        RdFBatchRegister rdFBatchRegister = new RdFBatchRegister()
            .workflowId(DEFAULT_WORKFLOW_ID)
            .dbname(DEFAULT_DBNAME)
            .feedId(DEFAULT_FEED_ID)
            .func(DEFAULT_FUNC)
            .reqDatetime(DEFAULT_REQ_DATETIME)
            .execUser(DEFAULT_EXEC_USER)
            .systemIds(DEFAULT_SYSTEM_IDS)
            .mode(DEFAULT_MODE)
            .done(DEFAULT_DONE)
            .createdTimestamp(DEFAULT_CREATED_TIMESTAMP)
            .updatedTimestamp(DEFAULT_UPDATED_TIMESTAMP);
        return rdFBatchRegister;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RdFBatchRegister createUpdatedEntity(EntityManager em) {
        RdFBatchRegister rdFBatchRegister = new RdFBatchRegister()
            .workflowId(UPDATED_WORKFLOW_ID)
            .dbname(UPDATED_DBNAME)
            .feedId(UPDATED_FEED_ID)
            .func(UPDATED_FUNC)
            .reqDatetime(UPDATED_REQ_DATETIME)
            .execUser(UPDATED_EXEC_USER)
            .systemIds(UPDATED_SYSTEM_IDS)
            .mode(UPDATED_MODE)
            .done(UPDATED_DONE)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .updatedTimestamp(UPDATED_UPDATED_TIMESTAMP);
        return rdFBatchRegister;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RdFBatchRegister.class).block();
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
        rdFBatchRegisterSearchRepository.deleteAll().block();
        assertThat(rdFBatchRegisterSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        rdFBatchRegister = createEntity(em);
    }

    @Test
    void createRdFBatchRegister() throws Exception {
        int databaseSizeBeforeCreate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // Create the RdFBatchRegister
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        RdFBatchRegister testRdFBatchRegister = rdFBatchRegisterList.get(rdFBatchRegisterList.size() - 1);
        assertThat(testRdFBatchRegister.getWorkflowId()).isEqualTo(DEFAULT_WORKFLOW_ID);
        assertThat(testRdFBatchRegister.getDbname()).isEqualTo(DEFAULT_DBNAME);
        assertThat(testRdFBatchRegister.getFeedId()).isEqualTo(DEFAULT_FEED_ID);
        assertThat(testRdFBatchRegister.getFunc()).isEqualTo(DEFAULT_FUNC);
        assertThat(testRdFBatchRegister.getReqDatetime()).isEqualTo(DEFAULT_REQ_DATETIME);
        assertThat(testRdFBatchRegister.getExecUser()).isEqualTo(DEFAULT_EXEC_USER);
        assertThat(testRdFBatchRegister.getSystemIds()).isEqualTo(DEFAULT_SYSTEM_IDS);
        assertThat(testRdFBatchRegister.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testRdFBatchRegister.getDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testRdFBatchRegister.getCreatedTimestamp()).isEqualTo(DEFAULT_CREATED_TIMESTAMP);
        assertThat(testRdFBatchRegister.getUpdatedTimestamp()).isEqualTo(DEFAULT_UPDATED_TIMESTAMP);
    }

    @Test
    void createRdFBatchRegisterWithExistingId() throws Exception {
        // Create the RdFBatchRegister with an existing ID
        rdFBatchRegister.setId(1L);
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        int databaseSizeBeforeCreate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkWorkflowIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setWorkflowId(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDbnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setDbname(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFeedIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setFeedId(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFuncIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setFunc(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkReqDatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setReqDatetime(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkExecUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setExecUser(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setMode(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setDone(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setCreatedTimestamp(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkUpdatedTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        // set the field null
        rdFBatchRegister.setUpdatedTimestamp(null);

        // Create the RdFBatchRegister, which fails.
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllRdFBatchRegisters() {
        // Initialize the database
        rdFBatchRegisterRepository.save(rdFBatchRegister).block();

        // Get all the rdFBatchRegisterList
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
            .value(hasItem(rdFBatchRegister.getId().intValue()))
            .jsonPath("$.[*].workflowId")
            .value(hasItem(DEFAULT_WORKFLOW_ID))
            .jsonPath("$.[*].dbname")
            .value(hasItem(DEFAULT_DBNAME))
            .jsonPath("$.[*].feedId")
            .value(hasItem(DEFAULT_FEED_ID))
            .jsonPath("$.[*].func")
            .value(hasItem(DEFAULT_FUNC))
            .jsonPath("$.[*].reqDatetime")
            .value(hasItem(DEFAULT_REQ_DATETIME))
            .jsonPath("$.[*].execUser")
            .value(hasItem(DEFAULT_EXEC_USER))
            .jsonPath("$.[*].systemIds")
            .value(hasItem(DEFAULT_SYSTEM_IDS.toString()))
            .jsonPath("$.[*].mode")
            .value(hasItem(DEFAULT_MODE))
            .jsonPath("$.[*].done")
            .value(hasItem(DEFAULT_DONE))
            .jsonPath("$.[*].createdTimestamp")
            .value(hasItem(DEFAULT_CREATED_TIMESTAMP.toString()))
            .jsonPath("$.[*].updatedTimestamp")
            .value(hasItem(DEFAULT_UPDATED_TIMESTAMP.toString()));
    }

    @Test
    void getRdFBatchRegister() {
        // Initialize the database
        rdFBatchRegisterRepository.save(rdFBatchRegister).block();

        // Get the rdFBatchRegister
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, rdFBatchRegister.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(rdFBatchRegister.getId().intValue()))
            .jsonPath("$.workflowId")
            .value(is(DEFAULT_WORKFLOW_ID))
            .jsonPath("$.dbname")
            .value(is(DEFAULT_DBNAME))
            .jsonPath("$.feedId")
            .value(is(DEFAULT_FEED_ID))
            .jsonPath("$.func")
            .value(is(DEFAULT_FUNC))
            .jsonPath("$.reqDatetime")
            .value(is(DEFAULT_REQ_DATETIME))
            .jsonPath("$.execUser")
            .value(is(DEFAULT_EXEC_USER))
            .jsonPath("$.systemIds")
            .value(is(DEFAULT_SYSTEM_IDS.toString()))
            .jsonPath("$.mode")
            .value(is(DEFAULT_MODE))
            .jsonPath("$.done")
            .value(is(DEFAULT_DONE))
            .jsonPath("$.createdTimestamp")
            .value(is(DEFAULT_CREATED_TIMESTAMP.toString()))
            .jsonPath("$.updatedTimestamp")
            .value(is(DEFAULT_UPDATED_TIMESTAMP.toString()));
    }

    @Test
    void getNonExistingRdFBatchRegister() {
        // Get the rdFBatchRegister
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRdFBatchRegister() throws Exception {
        // Initialize the database
        rdFBatchRegisterRepository.save(rdFBatchRegister).block();

        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        rdFBatchRegisterSearchRepository.save(rdFBatchRegister).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());

        // Update the rdFBatchRegister
        RdFBatchRegister updatedRdFBatchRegister = rdFBatchRegisterRepository.findById(rdFBatchRegister.getId()).block();
        updatedRdFBatchRegister
            .workflowId(UPDATED_WORKFLOW_ID)
            .dbname(UPDATED_DBNAME)
            .feedId(UPDATED_FEED_ID)
            .func(UPDATED_FUNC)
            .reqDatetime(UPDATED_REQ_DATETIME)
            .execUser(UPDATED_EXEC_USER)
            .systemIds(UPDATED_SYSTEM_IDS)
            .mode(UPDATED_MODE)
            .done(UPDATED_DONE)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .updatedTimestamp(UPDATED_UPDATED_TIMESTAMP);
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(updatedRdFBatchRegister);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rdFBatchRegisterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        RdFBatchRegister testRdFBatchRegister = rdFBatchRegisterList.get(rdFBatchRegisterList.size() - 1);
        assertThat(testRdFBatchRegister.getWorkflowId()).isEqualTo(UPDATED_WORKFLOW_ID);
        assertThat(testRdFBatchRegister.getDbname()).isEqualTo(UPDATED_DBNAME);
        assertThat(testRdFBatchRegister.getFeedId()).isEqualTo(UPDATED_FEED_ID);
        assertThat(testRdFBatchRegister.getFunc()).isEqualTo(UPDATED_FUNC);
        assertThat(testRdFBatchRegister.getReqDatetime()).isEqualTo(UPDATED_REQ_DATETIME);
        assertThat(testRdFBatchRegister.getExecUser()).isEqualTo(UPDATED_EXEC_USER);
        assertThat(testRdFBatchRegister.getSystemIds()).isEqualTo(UPDATED_SYSTEM_IDS);
        assertThat(testRdFBatchRegister.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testRdFBatchRegister.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testRdFBatchRegister.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
        assertThat(testRdFBatchRegister.getUpdatedTimestamp()).isEqualTo(UPDATED_UPDATED_TIMESTAMP);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<RdFBatchRegister> rdFBatchRegisterSearchList = IterableUtils.toList(
                    rdFBatchRegisterSearchRepository.findAll().collectList().block()
                );
                RdFBatchRegister testRdFBatchRegisterSearch = rdFBatchRegisterSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testRdFBatchRegisterSearch.getWorkflowId()).isEqualTo(UPDATED_WORKFLOW_ID);
                assertThat(testRdFBatchRegisterSearch.getDbname()).isEqualTo(UPDATED_DBNAME);
                assertThat(testRdFBatchRegisterSearch.getFeedId()).isEqualTo(UPDATED_FEED_ID);
                assertThat(testRdFBatchRegisterSearch.getFunc()).isEqualTo(UPDATED_FUNC);
                assertThat(testRdFBatchRegisterSearch.getReqDatetime()).isEqualTo(UPDATED_REQ_DATETIME);
                assertThat(testRdFBatchRegisterSearch.getExecUser()).isEqualTo(UPDATED_EXEC_USER);
                assertThat(testRdFBatchRegisterSearch.getSystemIds()).isEqualTo(UPDATED_SYSTEM_IDS);
                assertThat(testRdFBatchRegisterSearch.getMode()).isEqualTo(UPDATED_MODE);
                assertThat(testRdFBatchRegisterSearch.getDone()).isEqualTo(UPDATED_DONE);
                assertThat(testRdFBatchRegisterSearch.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
                assertThat(testRdFBatchRegisterSearch.getUpdatedTimestamp()).isEqualTo(UPDATED_UPDATED_TIMESTAMP);
            });
    }

    @Test
    void putNonExistingRdFBatchRegister() throws Exception {
        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        rdFBatchRegister.setId(count.incrementAndGet());

        // Create the RdFBatchRegister
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rdFBatchRegisterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchRdFBatchRegister() throws Exception {
        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        rdFBatchRegister.setId(count.incrementAndGet());

        // Create the RdFBatchRegister
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamRdFBatchRegister() throws Exception {
        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        rdFBatchRegister.setId(count.incrementAndGet());

        // Create the RdFBatchRegister
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateRdFBatchRegisterWithPatch() throws Exception {
        // Initialize the database
        rdFBatchRegisterRepository.save(rdFBatchRegister).block();

        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();

        // Update the rdFBatchRegister using partial update
        RdFBatchRegister partialUpdatedRdFBatchRegister = new RdFBatchRegister();
        partialUpdatedRdFBatchRegister.setId(rdFBatchRegister.getId());

        partialUpdatedRdFBatchRegister
            .workflowId(UPDATED_WORKFLOW_ID)
            .reqDatetime(UPDATED_REQ_DATETIME)
            .systemIds(UPDATED_SYSTEM_IDS)
            .mode(UPDATED_MODE)
            .done(UPDATED_DONE)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRdFBatchRegister.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRdFBatchRegister))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        RdFBatchRegister testRdFBatchRegister = rdFBatchRegisterList.get(rdFBatchRegisterList.size() - 1);
        assertThat(testRdFBatchRegister.getWorkflowId()).isEqualTo(UPDATED_WORKFLOW_ID);
        assertThat(testRdFBatchRegister.getDbname()).isEqualTo(DEFAULT_DBNAME);
        assertThat(testRdFBatchRegister.getFeedId()).isEqualTo(DEFAULT_FEED_ID);
        assertThat(testRdFBatchRegister.getFunc()).isEqualTo(DEFAULT_FUNC);
        assertThat(testRdFBatchRegister.getReqDatetime()).isEqualTo(UPDATED_REQ_DATETIME);
        assertThat(testRdFBatchRegister.getExecUser()).isEqualTo(DEFAULT_EXEC_USER);
        assertThat(testRdFBatchRegister.getSystemIds()).isEqualTo(UPDATED_SYSTEM_IDS);
        assertThat(testRdFBatchRegister.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testRdFBatchRegister.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testRdFBatchRegister.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
        assertThat(testRdFBatchRegister.getUpdatedTimestamp()).isEqualTo(DEFAULT_UPDATED_TIMESTAMP);
    }

    @Test
    void fullUpdateRdFBatchRegisterWithPatch() throws Exception {
        // Initialize the database
        rdFBatchRegisterRepository.save(rdFBatchRegister).block();

        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();

        // Update the rdFBatchRegister using partial update
        RdFBatchRegister partialUpdatedRdFBatchRegister = new RdFBatchRegister();
        partialUpdatedRdFBatchRegister.setId(rdFBatchRegister.getId());

        partialUpdatedRdFBatchRegister
            .workflowId(UPDATED_WORKFLOW_ID)
            .dbname(UPDATED_DBNAME)
            .feedId(UPDATED_FEED_ID)
            .func(UPDATED_FUNC)
            .reqDatetime(UPDATED_REQ_DATETIME)
            .execUser(UPDATED_EXEC_USER)
            .systemIds(UPDATED_SYSTEM_IDS)
            .mode(UPDATED_MODE)
            .done(UPDATED_DONE)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .updatedTimestamp(UPDATED_UPDATED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRdFBatchRegister.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRdFBatchRegister))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        RdFBatchRegister testRdFBatchRegister = rdFBatchRegisterList.get(rdFBatchRegisterList.size() - 1);
        assertThat(testRdFBatchRegister.getWorkflowId()).isEqualTo(UPDATED_WORKFLOW_ID);
        assertThat(testRdFBatchRegister.getDbname()).isEqualTo(UPDATED_DBNAME);
        assertThat(testRdFBatchRegister.getFeedId()).isEqualTo(UPDATED_FEED_ID);
        assertThat(testRdFBatchRegister.getFunc()).isEqualTo(UPDATED_FUNC);
        assertThat(testRdFBatchRegister.getReqDatetime()).isEqualTo(UPDATED_REQ_DATETIME);
        assertThat(testRdFBatchRegister.getExecUser()).isEqualTo(UPDATED_EXEC_USER);
        assertThat(testRdFBatchRegister.getSystemIds()).isEqualTo(UPDATED_SYSTEM_IDS);
        assertThat(testRdFBatchRegister.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testRdFBatchRegister.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testRdFBatchRegister.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
        assertThat(testRdFBatchRegister.getUpdatedTimestamp()).isEqualTo(UPDATED_UPDATED_TIMESTAMP);
    }

    @Test
    void patchNonExistingRdFBatchRegister() throws Exception {
        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        rdFBatchRegister.setId(count.incrementAndGet());

        // Create the RdFBatchRegister
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, rdFBatchRegisterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchRdFBatchRegister() throws Exception {
        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        rdFBatchRegister.setId(count.incrementAndGet());

        // Create the RdFBatchRegister
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamRdFBatchRegister() throws Exception {
        int databaseSizeBeforeUpdate = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        rdFBatchRegister.setId(count.incrementAndGet());

        // Create the RdFBatchRegister
        RdFBatchRegisterDTO rdFBatchRegisterDTO = rdFBatchRegisterMapper.toDto(rdFBatchRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rdFBatchRegisterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RdFBatchRegister in the database
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteRdFBatchRegister() {
        // Initialize the database
        rdFBatchRegisterRepository.save(rdFBatchRegister).block();
        rdFBatchRegisterRepository.save(rdFBatchRegister).block();
        rdFBatchRegisterSearchRepository.save(rdFBatchRegister).block();

        int databaseSizeBeforeDelete = rdFBatchRegisterRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the rdFBatchRegister
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, rdFBatchRegister.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RdFBatchRegister> rdFBatchRegisterList = rdFBatchRegisterRepository.findAll().collectList().block();
        assertThat(rdFBatchRegisterList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(rdFBatchRegisterSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchRdFBatchRegister() {
        // Initialize the database
        rdFBatchRegister = rdFBatchRegisterRepository.save(rdFBatchRegister).block();
        rdFBatchRegisterSearchRepository.save(rdFBatchRegister).block();

        // Search the rdFBatchRegister
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + rdFBatchRegister.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(rdFBatchRegister.getId().intValue()))
            .jsonPath("$.[*].workflowId")
            .value(hasItem(DEFAULT_WORKFLOW_ID))
            .jsonPath("$.[*].dbname")
            .value(hasItem(DEFAULT_DBNAME))
            .jsonPath("$.[*].feedId")
            .value(hasItem(DEFAULT_FEED_ID))
            .jsonPath("$.[*].func")
            .value(hasItem(DEFAULT_FUNC))
            .jsonPath("$.[*].reqDatetime")
            .value(hasItem(DEFAULT_REQ_DATETIME))
            .jsonPath("$.[*].execUser")
            .value(hasItem(DEFAULT_EXEC_USER))
            .jsonPath("$.[*].systemIds")
            .value(hasItem(DEFAULT_SYSTEM_IDS.toString()))
            .jsonPath("$.[*].mode")
            .value(hasItem(DEFAULT_MODE))
            .jsonPath("$.[*].done")
            .value(hasItem(DEFAULT_DONE))
            .jsonPath("$.[*].createdTimestamp")
            .value(hasItem(DEFAULT_CREATED_TIMESTAMP.toString()))
            .jsonPath("$.[*].updatedTimestamp")
            .value(hasItem(DEFAULT_UPDATED_TIMESTAMP.toString()));
    }
}
