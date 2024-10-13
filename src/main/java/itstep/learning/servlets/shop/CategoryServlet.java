package itstep.learning.servlets.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.shop.CategoryDao;
import itstep.learning.models.form.ShopCategoryFormModel;
import itstep.learning.rest.RestServlet;
import itstep.learning.services.cache.ConfigService;
import itstep.learning.services.cache.LocalConfigService;
import itstep.learning.services.files.FileService;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CategoryServlet extends RestServlet {
    private final FormParseService formParseService;
    private final FileService fileService;
    private final CategoryDao categoryDao;
    private final LocalConfigService configService;

    @Inject
    public CategoryServlet(FormParseService formParseService, FileService fileService, CategoryDao categoryDao, LocalConfigService configService) {
        this.formParseService = formParseService;
        this.fileService = fileService;
        this.categoryDao = categoryDao;
        this.configService = configService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        super.sendRest( 200, configService.getMaxAgeForCategory(), categoryDao.all() );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult formParseResult = formParseService.parse( req );

        String slug = formParseResult.getFields().get( "category-slug" ) ;
        if( slug != null && ! categoryDao.isSlugFree( slug ) ) {
            super.sendRest( 400, "Slug '" + slug + "' is not free" );
            return;
        }

        String name = formParseResult.getFields().get( "category-name" );
        if ( name == null || name.isEmpty() ) {
            super.sendRest( 422, "Missing required field: 'category-name'" );
            return;
        }
        String description = formParseResult.getFields().get( "category-description" );
        if ( description == null || description.isEmpty() ) {
            super.sendRest(422, "Missing required field: 'category-description'" );
            return;
        }
        // зберігаємо файл-аватарку та одержуємо його збережене ім'я
        String uploadedName;
        FileItem avatar = formParseResult.getFiles().get( "category-img" );
        if( avatar.getSize() > 0 ) {
            uploadedName = fileService.upload( avatar );
        }
        else {
            super.sendRest( 422, "Missing required file: 'category-img'" );
            return;
        }

        int maxAge = configService.getMaxAgeForCategory();

        super.sendRest( 200, maxAge,
                categoryDao.add(
                        new ShopCategoryFormModel()
                                .setSlug( slug )
                                .setName( name )
                                .setDescription( description )
                                .setSavedFilename( uploadedName )
                )
        );
    }
}