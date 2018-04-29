package home;

import java.util.logging.Logger;
import home.logic.AccessControl;
import home.logic.HTMLDocument;
import com.edge.http.configuration.ServerConfig;
import com.edge.http.exception.ServletException;
import com.edge.http.servlet.HttpRequestWrapper;
import com.edge.http.servlet.HttpServlet;
import com.edge.http.servlet.HttpServletRequest;
import com.edge.http.servlet.HttpServletResponse;
import com.edge.http.utilities.Utilities;

public class Login extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
    public static final String RELOCATE_PARAM_NAME = "relocate";

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ServerConfig serverConfig = (ServerConfig) getServletContext().getAttribute(ServerConfig.class.getName());
        AccessControl ac = new AccessControl(serverConfig, request.getSession());

        HTMLDocument doc = new HTMLDocument("Login", false);
        doc.setOwnerClass(getClass().getSimpleName());

        doc.writeln("<div class=\"form-login\">");

        doc.writeln("<h2>Web Server Login</h2>");

        if (request.getMethod().equals(HttpRequestWrapper.METHOD_POST)) {
            if (ac.doLogin(request.getPostParameter("login"), request.getPostParameter("password"))) {

                LOGGER.fine("Successfully logged in");

                if (request.getParameter("relocate") != null) {
                    response.sendRedirect(request.getParameter("relocate"));
                } else {
                    response.sendRedirect("/home/Index");
                }
            } else {
                LOGGER.fine("Wrong login or password");
                doc.writeln("<div class=\"alert alert-danger\" role=\"alert\"><strong>Oh snap!</strong> Incorrect login or password!</div>");
            }
        }

        String location = "/home/Login";
        if (request.getParameter(RELOCATE_PARAM_NAME) != null) {
            location += "?" + RELOCATE_PARAM_NAME + "=" + Utilities.urlEncode(request.getParameter(RELOCATE_PARAM_NAME));
        }


        String form = "<form action=\""
                + location
                + "\" method=\"post\">\n"
                + "      <input name=\"dologin\" type=\"hidden\" value=\"true\" />\n"
                + "      <label for=\"inputLogin\" class=\"sr-only\">Login</label>\n" +
                "        <input name=\"login\" type=\"text\" id=\"inputLogin\" class=\"form-control\" placeholder=\"Login\" required autofocus>\n" +
                "        <label for=\"inputPassword\" class=\"sr-only\">Password</label>\n" +
                "        <input name=\"password\" type=\"password\" id=\"inputPassword\" class=\"form-control\" placeholder=\"Password\" required>"
                + "<button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Login</button>\n"
                + "</form>\n";

        doc.write(form);
        doc.writeln("</div>");
        response.getWriter().print(doc.toString());
    }
}
