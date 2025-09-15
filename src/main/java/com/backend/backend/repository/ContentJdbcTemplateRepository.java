package com.backend.backend.repository;

import com.backend.backend.model.Content;
import com.backend.backend.model.Status;
import com.backend.backend.model.Type;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ContentJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public ContentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String FIND_ALL = "SELECT * FROM content";
    private static final String FIND_BY_ID = "SELECT * FROM content WHERE id = ?";
    private static final String CREATE = """
            INSERT INTO content(title, description, status, content_type, date_created, date_updated, url)
            VALUES(?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE = """
            UPDATE content SET title = ?, description = ?, status = ?, content_type = ?, 
            date_created = ?, date_updated = ?, url = ? WHERE id = ?
            """;
    private static final String DELETE = "DELETE FROM content WHERE id = ?";
    private static final String EXISTS_BY_ID = "SELECT COUNT(*) FROM content WHERE id = ?";

    public List<Content> findAll() {
        return jdbcTemplate.query(FIND_ALL, contentRowMapper());
    }

    public Optional<Content> findById(Integer id) {
        try {
            Content content = jdbcTemplate.queryForObject(FIND_BY_ID, contentRowMapper(), id);
            return Optional.ofNullable(content);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(Content content) {
        if (content.id() == null) {
            // Insert new content
            jdbcTemplate.update(CREATE,
                    content.title(),
                    content.desc(),
                    content.status().toString(),
                    content.contentType().toString(),
                    content.dateCreated(),
                    content.dateUpdated(),
                    content.url()
            );
        } else {
            // Update existing content
            jdbcTemplate.update(UPDATE,
                    content.title(),
                    content.desc(),
                    content.status().toString(),
                    content.contentType().toString(),
                    content.dateCreated(),
                    content.dateUpdated(),
                    content.url(),
                    content.id()
            );
        }
    }

    public boolean existsById(Integer id) {
        Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);
        return count != null && count > 0;
    }

    public void deleteById(Integer id) {
        jdbcTemplate.update(DELETE, id);
    }

    private RowMapper<Content> contentRowMapper() {
        return new RowMapper<Content>() {
            @Override
            public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Content(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        Status.valueOf(rs.getString("status")),
                        Type.valueOf(rs.getString("content_type")),
                        rs.getTimestamp("date_created") != null ?
                                rs.getTimestamp("date_created").toLocalDateTime() : null,
                        rs.getTimestamp("date_updated") != null ?
                                rs.getTimestamp("date_updated").toLocalDateTime() : null,
                        rs.getString("url")
                );
            }
        };
    }
}