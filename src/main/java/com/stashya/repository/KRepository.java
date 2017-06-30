package com.stashya.repository;

import com.stashya.domain.K;
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
 * Cassandra repository for the K entity.
 */
@Repository
public class KRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<K> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public KRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(K.class);
        this.findAllStmt = session.prepare("SELECT * FROM k");
        this.truncateStmt = session.prepare("TRUNCATE k");
    }

    public List<K> findAll() {
        List<K> ksList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                K k = new K();
                k.setId(row.getUUID("id"));
                return k;
            }
        ).forEach(ksList::add);
        return ksList;
    }

    public K findOne(UUID id) {
        return mapper.get(id);
    }

    public K save(K k) {
        if (k.getId() == null) {
            k.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<K>> violations = validator.validate(k);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(k);
        return k;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
