package com.stashya.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.stashya.domain.Rdr;

import com.stashya.repository.RdrRepository;
import com.stashya.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Rdr.
 */
@RestController
@RequestMapping("/api")
public class RdrResource {

    private final Logger log = LoggerFactory.getLogger(RdrResource.class);

    private static final String ENTITY_NAME = "rdr";

    private final RdrRepository rdrRepository;

    public RdrResource(RdrRepository rdrRepository) {
        this.rdrRepository = rdrRepository;
    }

    /**
     * POST  /rdrs : Create a new rdr.
     *
     * @param rdr the rdr to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rdr, or with status 400 (Bad Request) if the rdr has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rdrs")
    @Timed
    public ResponseEntity<Rdr> createRdr(@RequestBody Rdr rdr) throws URISyntaxException {
        log.debug("REST request to save Rdr : {}", rdr);
        if (rdr.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rdr cannot already have an ID")).body(null);
        }
        Rdr result = rdrRepository.save(rdr);
        return ResponseEntity.created(new URI("/api/rdrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rdrs : Updates an existing rdr.
     *
     * @param rdr the rdr to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rdr,
     * or with status 400 (Bad Request) if the rdr is not valid,
     * or with status 500 (Internal Server Error) if the rdr couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rdrs")
    @Timed
    public ResponseEntity<Rdr> updateRdr(@RequestBody Rdr rdr) throws URISyntaxException {
        log.debug("REST request to update Rdr : {}", rdr);
        if (rdr.getId() == null) {
            return createRdr(rdr);
        }
        Rdr result = rdrRepository.save(rdr);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rdr.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rdrs : get all the rdrs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rdrs in body
     */
    @GetMapping("/rdrs")
    @Timed
    public List<Rdr> getAllRdrs() {
        log.debug("REST request to get all Rdrs");
        return rdrRepository.findAll();
    }

    /**
     * GET  /rdrs/:id : get the "id" rdr.
     *
     * @param id the id of the rdr to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rdr, or with status 404 (Not Found)
     */
    @GetMapping("/rdrs/{id}")
    @Timed
    public ResponseEntity<Rdr> getRdr(@PathVariable String id) {
        log.debug("REST request to get Rdr : {}", id);
        Rdr rdr = rdrRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rdr));
    }

    /**
     * DELETE  /rdrs/:id : delete the "id" rdr.
     *
     * @param id the id of the rdr to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rdrs/{id}")
    @Timed
    public ResponseEntity<Void> deleteRdr(@PathVariable String id) {
        log.debug("REST request to delete Rdr : {}", id);
        rdrRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
