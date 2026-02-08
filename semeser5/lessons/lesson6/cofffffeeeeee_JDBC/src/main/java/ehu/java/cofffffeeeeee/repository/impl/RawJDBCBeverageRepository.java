package ehu.java.cofffffeeeeee.repository.impl;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.repository.BeverageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class RawJDBCBeverageRepository implements BeverageRepository {

    private final DataSource dataSource;

    @Override
    public List<Beverage> getAllBeverages() {
        log.info("Raw JDBC: Fetching all beverages from database");
        List<Beverage> beverages = new ArrayList<>();
        String sql = "SELECT id, name, price, description FROM BEVERAGE";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Beverage beverage = new Beverage();
                beverage.setId(rs.getLong("id"));
                beverage.setName(rs.getString("name"));
                beverage.setPrice(rs.getDouble("price"));
                beverage.setDescription(rs.getString("description"));
                beverages.add(beverage);
            }

            log.debug("Raw JDBC: Total beverages fetched: {}", beverages.size());
        } catch (SQLException e) {
            log.error("Raw JDBC: Error fetching beverages", e);
            throw new RuntimeException("Error fetching beverages", e);
        }

        return beverages;
    }

    @Override
    public boolean addBeverage(Beverage beverage) {
        log.info("Raw JDBC: Adding beverage: {}", beverage.getName());
        String sql = "INSERT INTO BEVERAGE (name, price, description) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, beverage.getName());
            pstmt.setDouble(2, beverage.getPrice());
            pstmt.setString(3, beverage.getDescription());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        beverage.setId(generatedKeys.getLong(1));
                        log.info("Raw JDBC: Beverage added successfully with ID: {}", beverage.getId());
                        return true;
                    }
                }
            }

            log.warn("Raw JDBC: Failed to add beverage");
            return false;
        } catch (SQLException e) {
            log.error("Raw JDBC: Error adding beverage", e);
            throw new RuntimeException("Error adding beverage", e);
        }
    }

    @Override
    public Beverage deleteBeverage(long id) {
        log.info("Raw JDBC: Deleting beverage with ID: {}", id);
        Beverage beverage = getBeverageById(id);

        if (beverage != null) {
            String sql = "DELETE FROM BEVERAGE WHERE id = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setLong(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    log.info("Raw JDBC: Beverage with ID {} deleted successfully", id);
                    return beverage;
                }
            } catch (SQLException e) {
                log.error("Raw JDBC: Error deleting beverage", e);
                throw new RuntimeException("Error deleting beverage", e);
            }
        }

        log.warn("Raw JDBC: Beverage with ID {} not found, cannot delete", id);
        return null;
    }

    @Override
    public Beverage getBeverageById(long id) {
        log.debug("Raw JDBC: Fetching beverage by ID: {}", id);
        String sql = "SELECT id, name, price, description FROM BEVERAGE WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Beverage beverage = new Beverage();
                    beverage.setId(rs.getLong("id"));
                    beverage.setName(rs.getString("name"));
                    beverage.setPrice(rs.getDouble("price"));
                    beverage.setDescription(rs.getString("description"));
                    return beverage;
                }
            }
        } catch (SQLException e) {
            log.error("Raw JDBC: Error fetching beverage by ID", e);
            throw new RuntimeException("Error fetching beverage by ID", e);
        }

        return null;
    }

    @Override
    public long getBeverageCount() {
        log.debug("Raw JDBC: Fetching beverage count");
        String sql = "SELECT COUNT(*) FROM BEVERAGE";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                long count = rs.getLong(1);
                log.debug("Raw JDBC: Total beverage count: {}", count);
                return count;
            }
        } catch (SQLException e) {
            log.error("Raw JDBC: Error fetching beverage count", e);
            throw new RuntimeException("Error fetching beverage count", e);
        }

        return 0;
    }
}

