package com.stashya.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.stashya.domain.K;

import com.stashya.repository.KRepository;
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
 * REST controller for managing K.
 */
@RestController
@RequestMapping("/api")
public class KResource {

    private final Logger log = LoggerFactory.getLogger(KResource.class);

    private static final String ENTITY_NAME = "k";

    private final KRepository kRepository;

    public KResource(KRepository kRepository) {
        this.kRepository = kRepository;
    }

    /**
     * POST  /ks : Create a new k.
     *
     * @param k the k to create
     * @return the ResponseEntity with status 201 (Created) and with body the new k, or with status 400 (Bad Request) if the k has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ks")
    @Timed
    public ResponseEntity<K> createK(@RequestBody K k) throws URISyntaxException {
        log.debug("REST request to save K : {}", k);
        if (k.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new k cannot already have an ID")).body(null);
        }
        K result = kRepository.save(k);
        return ResponseEntity.created(new URI("/api/ks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ks : Updates an existing k.
     *
     * @param k the k to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated k,
     * or with status 400 (Bad Request) if the k is not valid,
     * or with status 500 (Internal Server Error) if the k couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ks")
    @Timed
    public ResponseEntity<K> updateK(@RequestBody K k) throws URISyntaxException {
        log.debug("REST request to update K : {}", k);
        if (k.getId() == null) {
            return createK(k);
        }
        K result = kRepository.save(k);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, k.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ks : get all the ks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ks in body
     */
    @GetMapping("/ks")
    @Timed
    public List<K> getAllKS() {
        log.debug("REST request to get all KS");
        return kRepository.findAll();
    }

    /**
     * GET  /ks/:id : get the "id" k.
     *
     * @param id the id of the k to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the k, or with status 404 (Not Found)
     */
    @GetMapping("/ks/{id}")
    @Timed
    public ResponseEntity<K> getK(@PathVariable String id) {
        log.debug("REST request to get K : {}", id);
        K k = kRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(k));
    }

    /**
     * DELETE  /ks/:id : delete the "id" k.
     *
     * @param id the id of the k to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ks/{id}")
    @Timed
    public ResponseEntity<Void> deleteK(@PathVariable String id) {
        log.debug("REST request to delete K : {}", id);
        kRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
