package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.services.hash.HashService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@Singleton
public class HomeServlet extends HttpServlet {
    private final HashService md5HashService;
    private final HashService shaHashService;
    private final Connection connection;
    private final UserDao userDao;

    @Inject
    public HomeServlet(@Named("digest") HashService md5HashService,
                       @Named("signature") HashService shaHashService,
                       Connection connection,
                       UserDao userDao) {
        this.md5HashService = md5HashService;
        this.shaHashService = shaHashService;
        this.connection = connection;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Boolean control = (Boolean) req.getAttribute("control");

        if (control != null && control) {
            req.setAttribute( "control" ,"Control success");
        } else {
            req.setAttribute( "control" ,"Control failed ");
        }

        req.setAttribute("user",
                userDao.installTables() ? "Tables OK" : "Tables Fail");

        req.setAttribute("connection",
                connection == null ? "No" : " Ok ");

        req.setAttribute( "MD5",
                "MD5: " + md5HashService.digest("123") + "<br/>" +
                this.hashCode() );

        req.setAttribute( "SHA",
                "SHA: " + shaHashService.digest("123") + "<br/>"
                );

        req.setAttribute( "page", "home" );
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }
}
