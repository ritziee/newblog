package com.stashya.repository;

import com.stashya.domain.Rdr;
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
 * Cassandra repository for the Rdr entity.
 */
@Repository
public class RdrRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Rdr> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public RdrRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Rdr.class);
        this.findAllStmt = session.prepare("SELECT * FROM rdr");
        this.truncateStmt = session.prepare("TRUNCATE rdr");
    }

    public List<Rdr> findAll() {
        List<Rdr> rdrsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Rdr rdr = new Rdr();
                rdr.setId(row.getUUID("id"));
                return rdr;
            }
        ).forEach(rdrsList::add);
        return rdrsList;
    }

    public Rdr findOne(UUID id) {
        return mapper.get(id);
    }

    public Rdr save(Rdr rdr) {
        if (rdr.getId() == null) {
            rdr.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Rdr>> violations = validator.validate(rdr);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(rdr);
        return rdr;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
