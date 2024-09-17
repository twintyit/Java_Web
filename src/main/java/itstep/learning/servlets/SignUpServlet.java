package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.User;
import itstep.learning.models.form.UserSignupFormModel;
import itstep.learning.rest.RestResponse;
import itstep.learning.services.files.FileService;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Singleton
public class SignUpServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final FileService fileService;
    private final UserDao userDao;


    @Inject
    public SignUpServlet(FormParseService formParseService, FileService fileService, UserDao userDao) {
        this.formParseService = formParseService;
        this.fileService = fileService;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute( "page", "signup" );
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RestResponse restResponse = new RestResponse();
        resp.setContentType("application/json");
        UserSignupFormModel model;

        Object result = getModelFromRequest( req );
        if(result instanceof UserSignupFormModel) {
            model = (UserSignupFormModel) result;
        }
        else {
            restResponse.setStatus( "Error" );
            restResponse.setData( result );
            resp.getWriter().print(
                    new Gson().toJson(restResponse) );
            return;
        }

        User user = userDao.signup( model );
        if (user == null ) {
            restResponse.setStatus( "Error" );
            restResponse.setData( "500 DB Error, details on server logs" );
            resp.getWriter().print(
                    new Gson().toJson(restResponse)
            );
            return;
        }

        restResponse.setStatus("Ok");
        restResponse.setData(model);
        resp.getWriter().print(new Gson().toJson(restResponse));
    }

    private Object getModelFromRequest(HttpServletRequest req) {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        FormParseResult res = formParseService.parse( req );
        UserSignupFormModel model = new UserSignupFormModel();
        Map<String, String> errors = new HashMap<>();

        String userName = res.getFields().get("user-name");
        if (userName == null || userName.isEmpty()) {
            errors.put("user-name", "Введите имя.");
        }
        model.setName(userName);

        String email = res.getFields().get("user-email");
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.put("user-email", "Введите корректный email.");
        }
        model.setEmail(email);

        try {
            String birthdate = res.getFields().get("user-birthdate");
            if (birthdate == null || birthdate.isEmpty()) {
                errors.put("user-birthdate", "Введите дату рождения.");
            }
            model.setBirthdate(dateParser.parse(birthdate));
        } catch (ParseException ex) {
            errors.put("user-birthdate", "Некорректная дата рождения.");
        }

        String password = res.getFields().get("user-password");
        String repeatPassword = res.getFields().get("user-repeat");
        if (password == null || password.isEmpty()) {
            errors.put("user-password", "Введите пароль.");
        }
        if (repeatPassword == null || repeatPassword.isEmpty()) {
            errors.put("user-repeat", "Повторите пароль.");
        } else if (!password.equals(repeatPassword)) {
            errors.put("user-repeat", "Пароли не совпадают.");
        }
        model.setPassword(password);



        String uploadedName = null;
        FileItem avatar = res.getFiles().get("user-avatar");
        if( avatar.getSize() > 0 ){
            uploadedName = fileService.upload( avatar );
            if(uploadedName == null){
                errors.put("user-avatar", "Ошибка при сохранении файла.");
            }else {
                model.setAvatar( uploadedName );
            }
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        System.out.println( uploadedName );

        return model;
    }

}
