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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // Getter и Setter для birthdate
    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    // Getter и Setter для signupDt
    public Date getSignupDt() {
        return signupDt;
    }

    public void setSignupDt(Date signupDt) {
        this.signupDt = signupDt;
    }

    // Getter и Setter для deleteDt
    public Date getDeleteDt() {
        return deleteDt;
    }

    public void setDeleteDt(Date deleteDt) {
        this.deleteDt = deleteDt;
    }
}
