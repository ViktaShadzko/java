package ehu.java.cofffffeeeeee.repository.impl;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.repository.BeverageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Profile("jdbcclient")
@RequiredArgsConstructor
public class JDBCClientBeverageRepository implements BeverageRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<Beverage> getAllBeverages() {
        log.info("Fetching all beverages from database");
        String sql = "SELECT id, name, price, description FROM BEVERAGE";

        List<Beverage> beverages = jdbcClient.sql(sql)
                .query(Beverage.class)
                .list();

        log.debug("Total beverages fetched: {}", beverages.size());
        return beverages;
    }

    @Override
    public boolean addBeverage(Beverage beverage) {
        log.info("Adding beverage: {}", beverage.getName());
        String sql = "INSERT INTO BEVERAGE (name, price, description) VALUES (:name, :price, :description)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("name", beverage.getName())
                .param("price", beverage.getPrice())
                .param("description", beverage.getDescription())
                .update(keyHolder);

        if (rowsAffected > 0 && keyHolder.getKey() != null) {
            beverage.setId(keyHolder.getKey().longValue());
            log.info("Beverage added successfully with ID: {}", beverage.getId());
            return true;
        }

        log.warn("Failed to add beverage");
        return false;
    }

    @Override
    public Beverage deleteBeverage(long id) {
        log.info("Deleting beverage with ID: {}", id);
        Beverage beverage = getBeverageById(id);

        if (beverage != null) {
            String sql = "DELETE FROM BEVERAGE WHERE id = :id";
            int rowsAffected = jdbcClient.sql(sql)
                    .param("id", id)
                    .update();

            if (rowsAffected > 0) {
                log.info("Beverage with ID {} deleted successfully", id);
                return beverage;
            }
        }

        log.warn("Beverage with ID {} not found, cannot delete", id);
        return null;
    }

    @Override
    public Beverage getBeverageById(long id) {
        log.debug("Fetching beverage by ID: {}", id);
        String sql = "SELECT id, name, price, description FROM BEVERAGE WHERE id = :id";

        Optional<Beverage> beverage = jdbcClient.sql(sql)
                .param("id", id)
                .query(Beverage.class)
                .optional();

        return beverage.orElse(null);
    }

    @Override
    public long getBeverageCount() {
        log.debug("Fetching beverage count");
        String sql = "SELECT COUNT(*) FROM BEVERAGE";

        Long count = jdbcClient.sql(sql)
                .query(Long.class)
                .single();

        log.debug("Total beverage count: {}", count);
        return count;
    }
}

