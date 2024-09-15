package itstep.learning.dal.dto;

import java.util.Date;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String avatar;
    private Date birthdate;
    private Date signupDt;
    private Date deleteDt;

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }
}
