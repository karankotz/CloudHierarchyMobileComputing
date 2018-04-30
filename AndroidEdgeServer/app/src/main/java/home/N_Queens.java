package home;
import java.util.Objects;
import java.util.logging.Logger;

import java.util.logging.Logger;
import home.logic.AccessControl;
import home.logic.HTMLDocument;
import com.edge.http.configuration.ServerConfig;
import com.edge.http.exception.ServletException;
import com.edge.http.servlet.HttpRequestWrapper;
import com.edge.http.servlet.HttpServlet;
import com.edge.http.servlet.HttpServletRequest;

import android.util.Log;
import android.widget.TextView;
import com.edge.http.servlet.HttpServletResponse;
import com.edge.http.utilities.Utilities;

public class N_Queens extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
    public static final String RELOCATE_PARAM_NAME = "relocate";
    private int nQueensLocalNr;
    private double nQueensLocalTotDur;
    private int nQueensRemoteNr;
    private double nQueensRemoteTotDur;
    private TextView nQueensLocalNrText;
    int count = 0;
    String c = "0";
    String Ns;
    int N;

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ServerConfig serverConfig = (ServerConfig) getServletContext().getAttribute(ServerConfig.class.getName());

        HTMLDocument doc = new HTMLDocument("N_Queens", false);
        doc.setOwnerClass(getClass().getSimpleName());

        doc.writeln("<div class=\"form-unit\">");

        doc.writeln("<h2>N_Queen's Chess Problem</h2>");

        if (request.getMethod().equals(HttpRequestWrapper.METHOD_POST)) {
            LOGGER.fine("Karan reached this page");

            if (request.getPostParameter("value") != null) {
                Ns = request.getPostParameter("value");
                N = Integer.parseInt(Ns);
//--------------------------------------------------------------------------------------------------------
                int n=N_Queen_helper.main(N);
                doc.writeln("<h5>called Queens and number of queens is"+n+"</h5>");



            }
        }
//---------------------------------------------------------------------------------------------------------
        String location = "/home/N_Queens";
        if (request.getParameter(RELOCATE_PARAM_NAME) != null) {
            location += "?" + RELOCATE_PARAM_NAME + "=" + Utilities.urlEncode(request.getParameter(RELOCATE_PARAM_NAME));
        }
        count = count + 1;
        String form = "<form action=\""
                + location
                + "\" method=\"post\">\n"
                + "      <input name=\"N_Queens\" type=\"hidden\" value=\"true\" />\n" +


                " <br /><label for=\"N_Queens\" class=\"sr-only\">N_Queens</label>\n" +

                "          <input name=\"value\" type=\"text\" id=\"value\" class=\"form-control\" placeholder=\"Input Value for N_Queens problem\" required autofocus>\n <br />" +


                "<br /><button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Calculate</button>\n" +


                "</form>\n";

        doc.write(form);
        doc.writeln("</div>");
        if (c != "0") {
            doc.write("<br />");
            doc.write("<h4> Result of the calculation is " + c + "</h4>");

            //doc.write(dropdown);
            //doc.write(tempInFarhenheit);
        }
        response.getWriter().print(doc.toString());
    }

}

