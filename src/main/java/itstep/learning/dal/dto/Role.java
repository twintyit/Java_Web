package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Role {
    private UUID id;
    private String name;
    private String description;

    public Role() { }

    public Role(ResultSet resultSet) throws SQLException {
        this.id = UUID.fromString(resultSet.getString("role_id"));
        this.name = resultSet.getString("name");
    }

    public UUID getId() {
        return id;
    }

    public Role setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }
}
