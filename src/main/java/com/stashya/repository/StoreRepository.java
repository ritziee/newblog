package com.stashya.repository;

import com.stashya.domain.Store;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Cassandra repository for the Store entity.
 */
@Repository
public class StoreRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Store> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public StoreRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Store.class);
        this.findAllStmt = session.prepare("SELECT * FROM store");
        this.truncateStmt = session.prepare("TRUNCATE store");
    }

    public List<Store> findAll() {
        List<Store> storesList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Store store = new Store();
                store.setId(row.getUUID("id"));
                store.setPublishers(row.getString("publishers"));
                store.setHistory(row.getString("history"));
                return store;
            }
        ).forEach(storesList::add);
        return storesList;
    }

    public Store findOne(UUID id) {
        return mapper.get(id);
    }

    public Store save(Store store) {
        if (store.getId() == null) {
            store.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Store>> violations = validator.validate(store);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(store);
        return store;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
