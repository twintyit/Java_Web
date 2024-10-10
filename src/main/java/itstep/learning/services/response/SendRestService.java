package itstep.learning.services.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itstep.learning.rest.RestResponse;
import itstep.learning.rest.RestResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendRestService implements RestService {
    private final Gson gson;

    public SendRestService() {
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    @Override
    public void sendRestError(HttpServletResponse resp, String message) throws IOException {

        RestResponse restResponse = new RestResponse();
        restResponse.setStatus(new RestResponseStatus( 400));
        restResponse.setData(message);
        sendJsonResponse(resp, restResponse);
    }

    @Override
    public void sendRestResponse(HttpServletResponse resp, Object data) throws IOException {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatus(new RestResponseStatus( 200));
        restResponse.setData(data);
        sendJsonResponse(resp, restResponse);
    }

    private void sendJsonResponse(HttpServletResponse resp, RestResponse restResponse) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(gson.toJson(restResponse));
    }
}
