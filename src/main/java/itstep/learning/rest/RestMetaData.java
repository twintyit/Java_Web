package itstep.learning.rest;

import java.util.Date;
import java.util.Map;

public class RestMetaData {
    private String uri;
    private String method;
    private String name;
    private Date serverTime;
    private Map<String, Object> params;
    private String locate;
    private String[] acceptMethods;

    public String getUri() {
        return uri;
    }

    public RestMetaData setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public RestMetaData setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getName() {
        return name;
    }

    public RestMetaData setName(String name) {
        this.name = name;
        return this;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public RestMetaData setServerTime(Date serverTime) {
        this.serverTime = serverTime;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public RestMetaData setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public String getLocate() {
        return locate;
    }

    public RestMetaData setLocate(String locate) {
        this.locate = locate;
        return this;
    }

    public String[] getAcceptMethods() {
        return acceptMethods;
    }

    public RestMetaData setAcceptMethods(String[] acceptMethods) {
        this.acceptMethods = acceptMethods;
        return this;
    }
}
