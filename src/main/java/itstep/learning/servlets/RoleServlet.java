package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.RoleDao;
import itstep.learning.dal.dto.Role;
import itstep.learning.models.form.RoleFormModel;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;
import itstep.learning.services.response.SendRestService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class RoleServlet extends HttpServlet {
    private final Logger logger;
    private final RoleDao roleDao;
    private final SendRestService sendRestService;
    private final FormParseService formParseService;

    @Inject
    public RoleServlet(Logger logger, RoleDao roleDao, SendRestService sendRestService, FormParseService formParseService) {
        this.logger = logger;
        this.roleDao = roleDao;
        this.sendRestService = sendRestService;
        this.formParseService = formParseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute( "page", "role" );
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult formParseResult = formParseService.parse( req );
        String name = formParseResult.getFields().get( "name" );

        if (name == null || name.trim().isEmpty()) {
            sendRestService.sendRestError(resp, "Role name is required");
            return;
        }

        RoleFormModel roleForm = new RoleFormModel();
        roleForm.setName(name);

        try {
            Role role = roleDao.add(roleForm);
            if (role != null) {
                sendRestService.sendRestResponse(resp, role);
            } else {
                sendRestService.sendRestError(resp, "Error while adding role");
            }
        } catch (Exception e) {
            logger.warning("Error adding role: " + e.getMessage());
            sendRestService.sendRestError(resp, "An error occurred while adding the role");
        }
    }

}
