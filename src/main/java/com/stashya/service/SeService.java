package com.stashya.service;

import com.stashya.domain.Se;
import java.util.List;

/**
 * Service Interface for managing Se.
 */
public interface SeService {

    /**
     * Save a se.
     *
     * @param se the entity to save
     * @return the persisted entity
     */
    Se save(Se se);

    /**
     *  Get all the ses.
     *
     *  @return the list of entities
     */
    List<Se> findAll();

    /**
     *  Get the "id" se.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Se findOne(String id);

    /**
     *  Delete the "id" se.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}
