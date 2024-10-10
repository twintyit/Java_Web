package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.Role;
import itstep.learning.dal.dto.User;
import itstep.learning.models.form.RoleFormModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class RoleDao {
    private final Connection connection;
    private final Logger logger;

    @Inject
    public RoleDao(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }
    public Role getRoleById(UUID id ) {
        String sql = String.format(
                Locale.ROOT,
                "select * from roles where id = '%s'",
                id.toString()
        );

        try ( Statement statement = connection.createStatement() ){
            ResultSet resultSet = statement.executeQuery( sql );
            if( resultSet.next() ) {
                return new Role( resultSet );
            }
        }
        catch (SQLException ex){
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
        }
        return null;
    }

    public List<Role> all() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles WHERE delete_dt IS NULL";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                roles.add(new Role(resultSet));
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql, ex);
        }
        return roles;
    }

    public Role add(RoleFormModel formModel) {
        Role role = new Role()
                .setId(UUID.randomUUID())
                .setName(formModel.getName());
        String sql = "INSERT INTO roles (role_id, name) VALUES (?, ?)";
        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, role.getId().toString());
            prep.setString(2, role.getName());
            prep.executeUpdate();
            return role;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql, ex);
            return null;
        }
    }

    public boolean installTables() {
        String sql = "CREATE TABLE IF NOT EXISTS roles (" +
                "role_id CHAR(36) PRIMARY KEY DEFAULT (UUID()), " +
                "name VARCHAR(128) NOT NULL, " +
                "delete_dt DATETIME NULL" +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql, ex);
            return false;
        }
    }
}