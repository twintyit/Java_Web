package itstep.learning.rest;

public class RestResponseStatus {
    private boolean isSuccessful;
    private int code;
    private String phrase;

    public RestResponseStatus() {}
    public RestResponseStatus( int code) {
        this.setCode( code );
        switch (code) {
            case 200:
                this.isSuccessful = true;
                this.setPhrase("OK");
                break;
            case 201:
                this.isSuccessful = true;
                this.setPhrase("Created");
                break;
            case 202:
                this.isSuccessful = true;
                this.setPhrase("Accepted");
                break;

            case 401:
                this.isSuccessful = false;
                this.setPhrase("Unauthorized");
                break;

            case 403:
                this.isSuccessful = false;
                this.setPhrase("Forbidden");
                break;

            case 404:
                this.isSuccessful = false;
                this.setPhrase("Not Found");
                break;

            case 422:
                this.isSuccessful = false;
                this.setPhrase("Unprocessable Entity");
                break;

            case 500 :
                this.isSuccessful = false;
                this.setPhrase("Internal Server Error");
                break;

            case 501:
                this.isSuccessful = false;
                this.setPhrase("Not Acceptable");
                break;

            default:
                    this.isSuccessful = false;
                    this.setPhrase("Bad Request");
                    break;
        }
        this.isSuccessful = isSuccessful;
        this.code = code;
        this.phrase = phrase;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public RestResponseStatus setSuccessful(boolean successful) {
        isSuccessful = successful;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RestResponseStatus setCode(int code) {
        this.code = code;
        return this;
    }

    public String getPhrase() {
        return phrase;
    }

    public RestResponseStatus setPhrase(String phrase) {
        this.phrase = phrase;
         return this;
    }
}
