package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.models.form.UserSignupFormModel;
import itstep.learning.rest.RestResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SignUpServlet extends HttpServlet {
    private final FormParseService formParseService;

    @Inject
    public SignUpServlet( FormParseService formParseService ) {
        this.formParseService = formParseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute( "page", "signup" );
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        RestResponse restResponse = new RestResponse();
        resp.setContentType("application/json");

        FormParseResult res = formParseService.parse(req);
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

        // Валидация паролей
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

        if (!errors.isEmpty()) {
            restResponse.setStatus("Error");
            restResponse.setData(errors);
            resp.getWriter().print(new Gson().toJson(restResponse));
            return;
        }

        restResponse.setStatus("Ok");
        restResponse.setData(model);
        resp.getWriter().print(new Gson().toJson(restResponse));
    }

}
