package itstep.learning.filters.auth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.TokenDao;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.Role;
import itstep.learning.dal.dto.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class TokenAuthFilter implements Filter {
    private final TokenDao tokenDao;
    private final UserDao userDao;

    @Inject
    public TokenAuthFilter(TokenDao tokenDao, UserDao userDao) {
        this.tokenDao = tokenDao;
        this.userDao = userDao;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String authHeader = req.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UUID tokenId;
            User user = null;
            try{
                user = tokenDao.getUserByTokenId( UUID.fromString(token) );
            }
            catch (Exception ignored) { }
            if( user != null ) {

                UUID userId = user.getId();
                Role userRole =  userDao.getRoleById(userId);

                req.setAttribute("Claim.Sid",userId.toString() );
                req.setAttribute("Claim.Name", user.getName() );
                req.setAttribute("Claim.Avatar", user.getAvatar() );
                req.setAttribute("Claim.Role", userRole.getName() );

            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
