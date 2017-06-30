package com.stashya.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.stashya.domain.Se;
import com.stashya.service.SeService;
import com.stashya.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Se.
 */
@RestController
@RequestMapping("/api")
public class SeResource {

    private final Logger log = LoggerFactory.getLogger(SeResource.class);

    private static final String ENTITY_NAME = "se";

    private final SeService seService;

    public SeResource(SeService seService) {
        this.seService = seService;
    }

    /**
     * POST  /ses : Create a new se.
     *
     * @param se the se to create
     * @return the ResponseEntity with status 201 (Created) and with body the new se, or with status 400 (Bad Request) if the se has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ses")
    @Timed
    public ResponseEntity<Se> createSe(@Valid @RequestBody Se se) throws URISyntaxException {
        log.debug("REST request to save Se : {}", se);
        if (se.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new se cannot already have an ID")).body(null);
        }
        Se result = seService.save(se);
        return ResponseEntity.created(new URI("/api/ses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ses : Updates an existing se.
     *
     * @param se the se to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated se,
     * or with status 400 (Bad Request) if the se is not valid,
     * or with status 500 (Internal Server Error) if the se couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ses")
    @Timed
    public ResponseEntity<Se> updateSe(@Valid @RequestBody Se se) throws URISyntaxException {
        log.debug("REST request to update Se : {}", se);
        if (se.getId() == null) {
            return createSe(se);
        }
        Se result = seService.save(se);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, se.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ses : get all the ses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ses in body
     */
    @GetMapping("/ses")
    @Timed
    public List<Se> getAllSes() {
        log.debug("REST request to get all Ses");
        return seService.findAll();
    }

    /**
     * GET  /ses/:id : get the "id" se.
     *
     * @param id the id of the se to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the se, or with status 404 (Not Found)
     */
    @GetMapping("/ses/{id}")
    @Timed
    public ResponseEntity<Se> getSe(@PathVariable String id) {
        log.debug("REST request to get Se : {}", id);
        Se se = seService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(se));
    }

    /**
     * DELETE  /ses/:id : delete the "id" se.
     *
     * @param id the id of the se to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ses/{id}")
    @Timed
    public ResponseEntity<Void> deleteSe(@PathVariable String id) {
        log.debug("REST request to delete Se : {}", id);
        seService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
