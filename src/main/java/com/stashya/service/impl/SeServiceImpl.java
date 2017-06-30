package com.stashya.service.impl;

import com.stashya.service.SeService;
import com.stashya.domain.Se;
import com.stashya.repository.SeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service Implementation for managing Se.
 */
@Service
public class SeServiceImpl implements SeService{

    private final Logger log = LoggerFactory.getLogger(SeServiceImpl.class);

    private final SeRepository seRepository;

    public SeServiceImpl(SeRepository seRepository) {
        this.seRepository = seRepository;
    }

    /**
     * Save a se.
     *
     * @param se the entity to save
     * @return the persisted entity
     */
    @Override
    public Se save(Se se) {
        log.debug("Request to save Se : {}", se);
        return seRepository.save(se);
    }

    /**
     *  Get all the ses.
     *
     *  @return the list of entities
     */
    @Override
    public List<Se> findAll() {
        log.debug("Request to get all Ses");
        return seRepository.findAll();
    }

    /**
     *  Get one se by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public Se findOne(String id) {
        log.debug("Request to get Se : {}", id);
        return seRepository.findOne(UUID.fromString(id));
    }

    /**
     *  Delete the  se by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Se : {}", id);
        seRepository.delete(UUID.fromString(id));
    }
}
