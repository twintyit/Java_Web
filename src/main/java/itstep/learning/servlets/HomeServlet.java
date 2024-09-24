package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.dal.dao.TokenDao;
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
    private final UserDao userDao;
    private final TokenDao tokenDao;

    @Inject
    public HomeServlet(UserDao userDao, TokenDao tokenDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
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
                userDao.installTables() && tokenDao.installTables()
                        ? "Tables OK" : "Tables Fail");



        req.setAttribute( "page", "home" );
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }
}
