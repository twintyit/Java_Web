package itstep.learning.services.response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RestService {
    void sendRestError(HttpServletResponse resp, String message) throws IOException;
    void sendRestResponse(HttpServletResponse resp, Object data) throws IOException;
}
