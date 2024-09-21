package itstep.learning.filters.auth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SessionAuthFilter implements Filter {
    private final UserDao userDao;


    @Inject
    public SessionAuthFilter(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        String qs = req.getQueryString();

        if (qs != null) {
            Map<String, String[]> parameters = req.getParameterMap();

            if (parameters.containsKey("logout") && "logout".equals(parameters.get("logout")[0])) {
                session.removeAttribute("userId");
                ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/");
                return;
            }
        }

        UUID userID = (UUID) session.getAttribute("userId");

        if (userID != null) {
            User user = userDao.getUserById( userID);
            if(user != null) {
                request.setAttribute("Claim.Sid", userID);
                request.setAttribute("Claim.Name", user.getName() );
                request.setAttribute("Claim.Avatar", user.getAvatar() );

            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
