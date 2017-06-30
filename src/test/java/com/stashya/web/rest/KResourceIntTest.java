package com.stashya.web.rest;

import com.stashya.AbstractCassandraTest;
import com.stashya.BlogApp;

import com.stashya.domain.K;
import com.stashya.repository.KRepository;
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
 * Test class for the KResource REST controller.
 *
 * @see KResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class KResourceIntTest extends AbstractCassandraTest {

    @Autowired
    private KRepository kRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restKMockMvc;

    private K k;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KResource kResource = new KResource(kRepository);
        this.restKMockMvc = MockMvcBuilders.standaloneSetup(kResource)
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
    public static K createEntity() {
        K k = new K();
        return k;
    }

    @Before
    public void initTest() {
        kRepository.deleteAll();
        k = createEntity();
    }

    @Test
    public void createK() throws Exception {
        int databaseSizeBeforeCreate = kRepository.findAll().size();

        // Create the K
        restKMockMvc.perform(post("/api/ks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(k)))
            .andExpect(status().isCreated());

        // Validate the K in the database
        List<K> kList = kRepository.findAll();
        assertThat(kList).hasSize(databaseSizeBeforeCreate + 1);
        K testK = kList.get(kList.size() - 1);
    }

    @Test
    public void createKWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kRepository.findAll().size();

        // Create the K with an existing ID
        k.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restKMockMvc.perform(post("/api/ks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(k)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<K> kList = kRepository.findAll();
        assertThat(kList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllKS() throws Exception {
        // Initialize the database
        kRepository.save(k);

        // Get all the kList
        restKMockMvc.perform(get("/api/ks"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(k.getId().toString())));
    }

    @Test
    public void getK() throws Exception {
        // Initialize the database
        kRepository.save(k);

        // Get the k
        restKMockMvc.perform(get("/api/ks/{id}", k.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(k.getId().toString()));
    }

    @Test
    public void getNonExistingK() throws Exception {
        // Get the k
        restKMockMvc.perform(get("/api/ks/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateK() throws Exception {
        // Initialize the database
        kRepository.save(k);
        int databaseSizeBeforeUpdate = kRepository.findAll().size();

        // Update the k
        K updatedK = kRepository.findOne(k.getId());

        restKMockMvc.perform(put("/api/ks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedK)))
            .andExpect(status().isOk());

        // Validate the K in the database
        List<K> kList = kRepository.findAll();
        assertThat(kList).hasSize(databaseSizeBeforeUpdate);
        K testK = kList.get(kList.size() - 1);
    }

    @Test
    public void updateNonExistingK() throws Exception {
        int databaseSizeBeforeUpdate = kRepository.findAll().size();

        // Create the K

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restKMockMvc.perform(put("/api/ks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(k)))
            .andExpect(status().isCreated());

        // Validate the K in the database
        List<K> kList = kRepository.findAll();
        assertThat(kList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteK() throws Exception {
        // Initialize the database
        kRepository.save(k);
        int databaseSizeBeforeDelete = kRepository.findAll().size();

        // Get the k
        restKMockMvc.perform(delete("/api/ks/{id}", k.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<K> kList = kRepository.findAll();
        assertThat(kList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(K.class);
        K k1 = new K();
        k1.setId(UUID.randomUUID());
        K k2 = new K();
        k2.setId(k1.getId());
        assertThat(k1).isEqualTo(k2);
        k2.setId(UUID.randomUUID());
        assertThat(k1).isNotEqualTo(k2);
        k1.setId(null);
        assertThat(k1).isNotEqualTo(k2);
    }
}
