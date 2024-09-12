package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        FormParseResult res = formParseService.parse(req);
//        System.out.println( res.getFields().size() + " "+ res.getFiles().size() );
//        System.out.println( res.getFields().toString() );


        // Выводим поля формы
        Map<String, String> fields = res.getFields();
        Map<String, FileItem> files = res.getFiles();

        StringBuilder sbFields = new StringBuilder();
        fields.forEach((name, value) -> {
            sbFields.append(String.format("<p><strong>%s</strong>: %s</p>", name, value));
        });

        StringBuilder sbFile = new StringBuilder();
        // Выводим информацию о загруженных файлах
        files.forEach((name, fileItem) -> {
            sbFile.append(String.format("<p><strong>%s</strong>: %s (size: %d bytes)</p>",
                    name, fileItem.getName(), fileItem.getSize()));
        });

        // Устанавливаем вывод в атрибут запроса
        req.setAttribute("fieldsData", sbFields.toString());
        req.setAttribute("fileData", sbFile.toString());


        // Перенаправляем на страницу с результатом
        req.getRequestDispatcher("WEB-INF/views/form_result.jsp").forward(req, resp);


    }
}
