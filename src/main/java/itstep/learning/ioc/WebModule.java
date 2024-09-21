package itstep.learning.ioc;

import com.google.inject.servlet.ServletModule;
import itstep.learning.filters.*;
import itstep.learning.filters.auth.SessionAuthFilter;
import itstep.learning.servlets.*;

public class WebModule extends ServletModule {

    @Override
    protected void configureServlets() {
        filter("/*").through( CharsetFilter.class );
        filter("/*").through( ControlFilter.class );
        filter("/*").through( SessionAuthFilter.class );

        serve("/"         ).with( HomeServlet.class     );
        serve("/file/*"   ).with( DownloadServlet.class );
        serve("/servlets" ).with( ServletsServlet.class );
        serve("/signup"   ).with( SignUpServlet.class   );
        serve("/table"    ).with( TableServlet.class    );
        serve("/index"    ).with( IndexServlet.class    );
        serve("/form_result"    ).with( SignUpServlet.class    );

    }
}
