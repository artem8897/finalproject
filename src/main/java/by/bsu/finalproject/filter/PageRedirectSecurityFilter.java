package by.bsu.finalproject.filter;

import by.bsu.finalproject.command.ParamName;
import by.bsu.finalproject.command.PathName;
import by.bsu.finalproject.manager.ConfigurationManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Page redirect for illegal access Filter
 * @author A. Kuzmik
 */

@WebFilter( urlPatterns = { "/jsp/*" }, initParams = {
        @WebInitParam(name = "INDEX_PATH", value = "/index.jsp", description = "security filter") })

public class PageRedirectSecurityFilter implements Filter {

    private String indexPath;

    public void init(FilterConfig fConfig)  {
        indexPath = fConfig.getInitParameter("INDEX_PATH");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String role = (String) httpRequest.getSession().getAttribute(ParamName.PARAM_NAME_USER_TYPE);

        if (role == null) {
            RequestDispatcher dispatcher = httpRequest.getServletContext()
                    .getRequestDispatcher(ConfigurationManager.getProperty(PathName.PATH_LOGIN_PAGE));
            dispatcher.forward(httpRequest, httpResponse);
        } else {
            chain.doFilter(httpRequest, httpResponse);
        }
        httpResponse.sendRedirect(httpRequest.getContextPath() + indexPath);
        chain.doFilter(request, response);
    }
    public void destroy() {
    }
}
