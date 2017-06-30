package com.stashya.repository;

import com.stashya.domain.Se;
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
 * Cassandra repository for the Se entity.
 */
@Repository
public class SeRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Se> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public SeRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Se.class);
        this.findAllStmt = session.prepare("SELECT * FROM se");
        this.truncateStmt = session.prepare("TRUNCATE se");
    }

    public List<Se> findAll() {
        List<Se> sesList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Se se = new Se();
                se.setId(row.getUUID("id"));
                se.setInvent(row.getString("invent"));
                return se;
            }
        ).forEach(sesList::add);
        return sesList;
    }

    public Se findOne(UUID id) {
        return mapper.get(id);
    }

    public Se save(Se se) {
        if (se.getId() == null) {
            se.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Se>> violations = validator.validate(se);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(se);
        return se;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
