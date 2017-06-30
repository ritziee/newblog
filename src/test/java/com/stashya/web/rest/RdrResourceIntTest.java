package com.stashya.web.rest;

import com.stashya.AbstractCassandraTest;
import com.stashya.BlogApp;

import com.stashya.domain.Rdr;
import com.stashya.repository.RdrRepository;
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
 * Test class for the RdrResource REST controller.
 *
 * @see RdrResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class RdrResourceIntTest extends AbstractCassandraTest {

    @Autowired
    private RdrRepository rdrRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restRdrMockMvc;

    private Rdr rdr;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RdrResource rdrResource = new RdrResource(rdrRepository);
        this.restRdrMockMvc = MockMvcBuilders.standaloneSetup(rdrResource)
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
    public static Rdr createEntity() {
        Rdr rdr = new Rdr();
        return rdr;
    }

    @Before
    public void initTest() {
        rdrRepository.deleteAll();
        rdr = createEntity();
    }

    @Test
    public void createRdr() throws Exception {
        int databaseSizeBeforeCreate = rdrRepository.findAll().size();

        // Create the Rdr
        restRdrMockMvc.perform(post("/api/rdrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rdr)))
            .andExpect(status().isCreated());

        // Validate the Rdr in the database
        List<Rdr> rdrList = rdrRepository.findAll();
        assertThat(rdrList).hasSize(databaseSizeBeforeCreate + 1);
        Rdr testRdr = rdrList.get(rdrList.size() - 1);
    }

    @Test
    public void createRdrWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rdrRepository.findAll().size();

        // Create the Rdr with an existing ID
        rdr.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRdrMockMvc.perform(post("/api/rdrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rdr)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Rdr> rdrList = rdrRepository.findAll();
        assertThat(rdrList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllRdrs() throws Exception {
        // Initialize the database
        rdrRepository.save(rdr);

        // Get all the rdrList
        restRdrMockMvc.perform(get("/api/rdrs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rdr.getId().toString())));
    }

    @Test
    public void getRdr() throws Exception {
        // Initialize the database
        rdrRepository.save(rdr);

        // Get the rdr
        restRdrMockMvc.perform(get("/api/rdrs/{id}", rdr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rdr.getId().toString()));
    }

    @Test
    public void getNonExistingRdr() throws Exception {
        // Get the rdr
        restRdrMockMvc.perform(get("/api/rdrs/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRdr() throws Exception {
        // Initialize the database
        rdrRepository.save(rdr);
        int databaseSizeBeforeUpdate = rdrRepository.findAll().size();

        // Update the rdr
        Rdr updatedRdr = rdrRepository.findOne(rdr.getId());

        restRdrMockMvc.perform(put("/api/rdrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRdr)))
            .andExpect(status().isOk());

        // Validate the Rdr in the database
        List<Rdr> rdrList = rdrRepository.findAll();
        assertThat(rdrList).hasSize(databaseSizeBeforeUpdate);
        Rdr testRdr = rdrList.get(rdrList.size() - 1);
    }

    @Test
    public void updateNonExistingRdr() throws Exception {
        int databaseSizeBeforeUpdate = rdrRepository.findAll().size();

        // Create the Rdr

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRdrMockMvc.perform(put("/api/rdrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rdr)))
            .andExpect(status().isCreated());

        // Validate the Rdr in the database
        List<Rdr> rdrList = rdrRepository.findAll();
        assertThat(rdrList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteRdr() throws Exception {
        // Initialize the database
        rdrRepository.save(rdr);
        int databaseSizeBeforeDelete = rdrRepository.findAll().size();

        // Get the rdr
        restRdrMockMvc.perform(delete("/api/rdrs/{id}", rdr.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rdr> rdrList = rdrRepository.findAll();
        assertThat(rdrList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rdr.class);
        Rdr rdr1 = new Rdr();
        rdr1.setId(UUID.randomUUID());
        Rdr rdr2 = new Rdr();
        rdr2.setId(rdr1.getId());
        assertThat(rdr1).isEqualTo(rdr2);
        rdr2.setId(UUID.randomUUID());
        assertThat(rdr1).isNotEqualTo(rdr2);
        rdr1.setId(null);
        assertThat(rdr1).isNotEqualTo(rdr2);
    }
}
