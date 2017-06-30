package com.stashya.web.rest;

import com.stashya.AbstractCassandraTest;
import com.stashya.BlogApp;

import com.stashya.domain.Store;
import com.stashya.repository.StoreRepository;
import com.stashya.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StoreResource REST controller.
 *
 * @see StoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class StoreResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_PUBLISHERS = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHERS = "BBBBBBBBBB";

    private static final String DEFAULT_HISTORY = "AAAAAAAAAA";
    private static final String UPDATED_HISTORY = "BBBBBBBBBB";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restStoreMockMvc;

    private Store store;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoreResource storeResource = new StoreResource(storeRepository);
        this.restStoreMockMvc = MockMvcBuilders.standaloneSetup(storeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Store createEntity() {
        Store store = new Store()
            .publishers(DEFAULT_PUBLISHERS)
            .history(DEFAULT_HISTORY);
        return store;
    }

    @Before
    public void initTest() {
        storeRepository.deleteAll();
        store = createEntity();
    }

    @Test
    public void createStore() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate + 1);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getPublishers()).isEqualTo(DEFAULT_PUBLISHERS);
        assertThat(testStore.getHistory()).isEqualTo(DEFAULT_HISTORY);
    }

    @Test
    public void createStoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store with an existing ID
        store.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkPublishersIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setPublishers(null);

        // Create the Store, which fails.

        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllStores() throws Exception {
        // Initialize the database
        storeRepository.save(store);

        // Get all the storeList
        restStoreMockMvc.perform(get("/api/stores"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().toString())))
            .andExpect(jsonPath("$.[*].publishers").value(hasItem(DEFAULT_PUBLISHERS.toString())))
            .andExpect(jsonPath("$.[*].history").value(hasItem(DEFAULT_HISTORY.toString())));
    }

    @Test
    public void getStore() throws Exception {
        // Initialize the database
        storeRepository.save(store);

        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(store.getId().toString()))
            .andExpect(jsonPath("$.publishers").value(DEFAULT_PUBLISHERS.toString()))
            .andExpect(jsonPath("$.history").value(DEFAULT_HISTORY.toString()));
    }

    @Test
    public void getNonExistingStore() throws Exception {
        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStore() throws Exception {
        // Initialize the database
        storeRepository.save(store);
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store
        Store updatedStore = storeRepository.findOne(store.getId());
        updatedStore
            .publishers(UPDATED_PUBLISHERS)
            .history(UPDATED_HISTORY);

        restStoreMockMvc.perform(put("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStore)))
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getPublishers()).isEqualTo(UPDATED_PUBLISHERS);
        assertThat(testStore.getHistory()).isEqualTo(UPDATED_HISTORY);
    }

    @Test
    public void updateNonExistingStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Create the Store

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStoreMockMvc.perform(put("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteStore() throws Exception {
        // Initialize the database
        storeRepository.save(store);
        int databaseSizeBeforeDelete = storeRepository.findAll().size();

        // Get the store
        restStoreMockMvc.perform(delete("/api/stores/{id}", store.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Store.class);
        Store store1 = new Store();
        store1.setId(UUID.randomUUID());
        Store store2 = new Store();
        store2.setId(store1.getId());
        assertThat(store1).isEqualTo(store2);
        store2.setId(UUID.randomUUID());
        assertThat(store1).isNotEqualTo(store2);
        store1.setId(null);
        assertThat(store1).isNotEqualTo(store2);
    }
}
