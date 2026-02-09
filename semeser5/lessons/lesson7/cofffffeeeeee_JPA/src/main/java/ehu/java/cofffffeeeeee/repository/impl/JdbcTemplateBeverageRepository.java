package ehu.java.cofffffeeeeee.repository.impl;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.repository.BeverageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Repository
@Profile("jdbctemplate")
@RequiredArgsConstructor
public class JdbcTemplateBeverageRepository implements BeverageRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Beverage> beverageRowMapper = (rs, rowNum) -> {
        Beverage beverage = new Beverage();
        beverage.setId(rs.getLong("id"));
        beverage.setName(rs.getString("name"));
        beverage.setPrice(rs.getDouble("price"));
        beverage.setDescription(rs.getString("description"));
        return beverage;
    };

    @Override
    public List<Beverage> getAllBeverages() {
        log.info("JdbcTemplate: Fetching all beverages from database");
        String sql = "SELECT id, name, price, description FROM BEVERAGE";

        List<Beverage> beverages = jdbcTemplate.query(sql, beverageRowMapper);

        log.debug("JdbcTemplate: Total beverages fetched: {}", beverages.size());
        return beverages;
    }

    @Override
    public boolean addBeverage(Beverage beverage) {
        log.info("JdbcTemplate: Adding beverage: {}", beverage.getName());
        String sql = "INSERT INTO BEVERAGE (name, price, description) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, beverage.getName());
            ps.setDouble(2, beverage.getPrice());
            ps.setString(3, beverage.getDescription());
            return ps;
        }, keyHolder);

        if (rowsAffected > 0 && keyHolder.getKey() != null) {
            beverage.setId(keyHolder.getKey().longValue());
            log.info("JdbcTemplate: Beverage added successfully with ID: {}", beverage.getId());
            return true;
        }

        log.warn("JdbcTemplate: Failed to add beverage");
        return false;
    }

    @Override
    public Beverage deleteBeverage(long id) {
        log.info("JdbcTemplate: Deleting beverage with ID: {}", id);
        Beverage beverage = getBeverageById(id);

        if (beverage != null) {
            String sql = "DELETE FROM BEVERAGE WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);

            if (rowsAffected > 0) {
                log.info("JdbcTemplate: Beverage with ID {} deleted successfully", id);
                return beverage;
            }
        }

        log.warn("JdbcTemplate: Beverage with ID {} not found, cannot delete", id);
        return null;
    }

    @Override
    public Beverage getBeverageById(long id) {
        log.debug("JdbcTemplate: Fetching beverage by ID: {}", id);
        String sql = "SELECT id, name, price, description FROM BEVERAGE WHERE id = ?";

        List<Beverage> beverages = jdbcTemplate.query(sql, beverageRowMapper, id);

        return beverages.isEmpty() ? null : beverages.get(0);
    }

    @Override
    public long getBeverageCount() {
        log.debug("JdbcTemplate: Fetching beverage count");
        String sql = "SELECT COUNT(*) FROM BEVERAGE";

        Long count = jdbcTemplate.queryForObject(sql, Long.class);

        log.debug("JdbcTemplate: Total beverage count: {}", count);
        return count != null ? count : 0;
    }
}



