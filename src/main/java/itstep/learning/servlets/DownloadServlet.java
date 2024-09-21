package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.files.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.logging.Logger;

@Singleton
public class DownloadServlet extends HttpServlet {
    private final FileService fileService;
    private final Logger logger;

    @Inject
    public DownloadServlet(FileService fileService, Logger logger) {
        this.fileService = fileService;
        this.logger = logger;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(InputStream fileReadStream = Objects.requireNonNull( fileService.download( req.getPathInfo() ) );
            OutputStream outputStream = resp.getOutputStream()
        ) {
            //resp.setContentType("application/octet-stream");
            //resp.setHeader();
            resp.setContentType("image/jpeg");

            byte[] buffer = new byte[4096];
            int length;
            while ( ( length = fileReadStream.read( buffer ) ) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        catch (Exception ex) {
            logger.warning( ex.getMessage() );
            resp.setStatus( 404 );
        }
    }

    /*
req.getRequestURL()
req.getPathInfo();
     */
}
