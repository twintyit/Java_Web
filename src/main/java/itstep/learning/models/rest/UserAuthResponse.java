package itstep.learning.models.rest;

import itstep.learning.dal.dto.Role;
import itstep.learning.dal.dto.Token;

public class UserAuthResponse {
    private Token token;
    private Role role;

    public UserAuthResponse(Token token, Role role) {
        this.token = token;
        this.role = role;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
