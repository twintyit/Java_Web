package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.RoleDao;
import itstep.learning.dal.dao.TokenDao;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dao.shop.CartDao;
import itstep.learning.dal.dto.Role;
import itstep.learning.dal.dto.Token;
import itstep.learning.dal.dto.User;
import itstep.learning.dal.dto.shop.CartItem;
import itstep.learning.models.rest.UserAuthResponse;
import itstep.learning.rest.RestResponse;
import itstep.learning.rest.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
public class AuthServlet  extends RestServlet {
    private final Logger logger;
    private final UserDao userDao;
    private final TokenDao tokenDao;
    private final CartDao cartDao;

    @Inject
    public AuthServlet(Logger logger, UserDao userDao, TokenDao tokenDao, CartDao cartDao) {
        this.logger = logger;
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.cartDao = cartDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        if(authHeader == null ) {
            super.sendRest(401, "Missing Authorization header" );
            return;
        }
        if(!authHeader.startsWith("Basic ")) {
            super.sendRest(400, "Basic Authorization scheme only" );
            return;
        }
        String credentials64 = authHeader.substring(6);
        String credentials;
        try{
           credentials= new String( Base64.getDecoder().decode(credentials64) );
        }catch (IllegalArgumentException e) {
            logger.warning(e.getMessage());
            super.sendRest(400, "Illegal credential format" );
            return;
        }
        String[] parts = credentials.split(":", 2);
        User user = userDao.authenticate(parts[0], parts[1]);
        if(user == null) {
            super.sendRest(422, "Invalid username or password" );
            return;
        }

        String tmpId = req.getParameter("tmp-id");
        logger.warning(tmpId);
        try{
            if(tmpId != null) {
                logger.warning("ok");
                List<CartItem> tempCart = cartDao.getCartByTmpId(tmpId);
                tempCart.forEach( ci -> cartDao.add(user.getId(), ci.getProductId(), ci.getQuantity() ));
            }
        }catch (Exception ex) {
            logger.warning(ex.getMessage());
        }

        Token token = tokenDao.create(user);
        Role role = userDao.getRoleById(user.getId());

        UserAuthResponse response = new UserAuthResponse(token, role);
        int ageMax = (int)(token.getExp().getTime() / 1000) - (int)(token.getIat().getTime() / 1000);

        super.sendRest(200,ageMax, response);
    }


}
