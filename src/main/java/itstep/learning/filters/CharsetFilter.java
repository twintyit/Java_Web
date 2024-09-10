package itstep.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class CharsetFilter  implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("Filter works for" +
               ((HttpServletRequest)request).getRequestURI() );
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
       this.filterConfig = null;
    }
}
