package com.stashya.web.rest;

import com.stashya.AbstractCassandraTest;
import com.stashya.BlogApp;

import com.stashya.domain.Se;
import com.stashya.repository.SeRepository;
import com.stashya.service.SeService;
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
 * Test class for the SeResource REST controller.
 *
 * @see SeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class SeResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_INVENT = "AAAAAAAAAA";
    private static final String UPDATED_INVENT = "BBBBBBBBBB";

    @Autowired
    private SeRepository seRepository;

    @Autowired
    private SeService seService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restSeMockMvc;

    private Se se;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SeResource seResource = new SeResource(seService);
        this.restSeMockMvc = MockMvcBuilders.standaloneSetup(seResource)
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
    public static Se createEntity() {
        Se se = new Se()
            .invent(DEFAULT_INVENT);
        return se;
    }

    @Before
    public void initTest() {
        seRepository.deleteAll();
        se = createEntity();
    }

    @Test
    public void createSe() throws Exception {
        int databaseSizeBeforeCreate = seRepository.findAll().size();

        // Create the Se
        restSeMockMvc.perform(post("/api/ses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(se)))
            .andExpect(status().isCreated());

        // Validate the Se in the database
        List<Se> seList = seRepository.findAll();
        assertThat(seList).hasSize(databaseSizeBeforeCreate + 1);
        Se testSe = seList.get(seList.size() - 1);
        assertThat(testSe.getInvent()).isEqualTo(DEFAULT_INVENT);
    }

    @Test
    public void createSeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = seRepository.findAll().size();

        // Create the Se with an existing ID
        se.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeMockMvc.perform(post("/api/ses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(se)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Se> seList = seRepository.findAll();
        assertThat(seList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkInventIsRequired() throws Exception {
        int databaseSizeBeforeTest = seRepository.findAll().size();
        // set the field null
        se.setInvent(null);

        // Create the Se, which fails.

        restSeMockMvc.perform(post("/api/ses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(se)))
            .andExpect(status().isBadRequest());

        List<Se> seList = seRepository.findAll();
        assertThat(seList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSes() throws Exception {
        // Initialize the database
        seRepository.save(se);

        // Get all the seList
        restSeMockMvc.perform(get("/api/ses"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(se.getId().toString())))
            .andExpect(jsonPath("$.[*].invent").value(hasItem(DEFAULT_INVENT.toString())));
    }

    @Test
    public void getSe() throws Exception {
        // Initialize the database
        seRepository.save(se);

        // Get the se
        restSeMockMvc.perform(get("/api/ses/{id}", se.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(se.getId().toString()))
            .andExpect(jsonPath("$.invent").value(DEFAULT_INVENT.toString()));
    }

    @Test
    public void getNonExistingSe() throws Exception {
        // Get the se
        restSeMockMvc.perform(get("/api/ses/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSe() throws Exception {
        // Initialize the database
        seService.save(se);

        int databaseSizeBeforeUpdate = seRepository.findAll().size();

        // Update the se
        Se updatedSe = seRepository.findOne(se.getId());
        updatedSe
            .invent(UPDATED_INVENT);

        restSeMockMvc.perform(put("/api/ses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSe)))
            .andExpect(status().isOk());

        // Validate the Se in the database
        List<Se> seList = seRepository.findAll();
        assertThat(seList).hasSize(databaseSizeBeforeUpdate);
        Se testSe = seList.get(seList.size() - 1);
        assertThat(testSe.getInvent()).isEqualTo(UPDATED_INVENT);
    }

    @Test
    public void updateNonExistingSe() throws Exception {
        int databaseSizeBeforeUpdate = seRepository.findAll().size();

        // Create the Se

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSeMockMvc.perform(put("/api/ses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(se)))
            .andExpect(status().isCreated());

        // Validate the Se in the database
        List<Se> seList = seRepository.findAll();
        assertThat(seList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteSe() throws Exception {
        // Initialize the database
        seService.save(se);

        int databaseSizeBeforeDelete = seRepository.findAll().size();

        // Get the se
        restSeMockMvc.perform(delete("/api/ses/{id}", se.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Se> seList = seRepository.findAll();
        assertThat(seList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Se.class);
        Se se1 = new Se();
        se1.setId(UUID.randomUUID());
        Se se2 = new Se();
        se2.setId(se1.getId());
        assertThat(se1).isEqualTo(se2);
        se2.setId(UUID.randomUUID());
        assertThat(se1).isNotEqualTo(se2);
        se1.setId(null);
        assertThat(se1).isNotEqualTo(se2);
    }
}
