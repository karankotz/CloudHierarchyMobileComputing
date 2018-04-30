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
import com.edge.http.servlet.HttpServletResponse;
import com.edge.http.utilities.Utilities;

public class UnitConverter extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
    public static final String RELOCATE_PARAM_NAME = "relocate";

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ServerConfig serverConfig = (ServerConfig) getServletContext().getAttribute(ServerConfig.class.getName());

        HTMLDocument doc = new HTMLDocument("UnitConverter", false);
        doc.setOwnerClass(getClass().getSimpleName());

        doc.writeln("<div class=\"form-unit\">");

        doc.writeln("<h2>Unit Converter</h2>");

//    public static double celsius2Farenheit(double f){
//        return (f-32)* 5/9;
//    }
//
//    public static double farenheit2Celsius(double c){
//        return 32+c*9/5;
//    }
        double convertedOutput=0.0;
        String c="0";
        int count=0;
        String dropdown="Karan";
        String unconvertedInput="k";
        if (request.getMethod().equals(HttpRequestWrapper.METHOD_POST)) {
            LOGGER.fine("Karan reached this page");

            if (request.getPostParameter("temp")!=null) {
                unconvertedInput = request.getPostParameter("temp");
                double t=Double.parseDouble(unconvertedInput);

                dropdown = request.getPostParameter("typeofconversion");

                if (Objects.equals(dropdown, new String("f2c"))){
                    convertedOutput=(t-32)*0.55556;
                }

                else if(Objects.equals(dropdown, new String("c2f"))){
                    convertedOutput=(t*1.8)+32;
                }

                else if(Objects.equals(dropdown, new String("l2g"))){
                    convertedOutput=(t*0.264);
                }

                else if(Objects.equals(dropdown, new String("g2l"))){
                    convertedOutput=(t*3.7854);
                }
                else if(Objects.equals(dropdown, new String("k2m"))){
                    convertedOutput=(t*0.62137);
                }
                else if(Objects.equals(dropdown, new String("m2k"))) {
                    convertedOutput = (t * 1.60934);
                }

                else if(Objects.equals(dropdown, new String("w2h"))){
                    convertedOutput=( t * 168);

                }
                double roundOff = Math.round(convertedOutput*100)/100D;

                c = Double.toString(roundOff);


            } else {
                LOGGER.fine("Enter temp not 0.0!");

            }



        }

        String location = "/home/UnitConverter";
        if (request.getParameter(RELOCATE_PARAM_NAME) != null) {
            location += "?" + RELOCATE_PARAM_NAME + "=" + Utilities.urlEncode(request.getParameter(RELOCATE_PARAM_NAME));
        }
        count=count+1;
        String form = "<form action=\""
                + location
                + "\" method=\"post\">\n"
                + "      <input name=\"Unitconverter\" type=\"hidden\" value=\"true\" />\n"+

                " <select name=\"typeofconversion\" id=\"typeofconversion\">\n" +

                "        <option value=\"-1\">None</option>\n" +

                "        <option value=\"f2c\">Farenheit To Celsius</option>\n" +
                "        <option value=\"c2f\"> Celsius To Farenheit</option>\n " +
                "        <option value=\"l2g\"> Litre To US Liquid Gallon</option>\n " +
                "        <option value=\"g2l\"> US Liquid Gallon To Liter</option>\n " +
                "        <option value=\"k2m\">Speed - Kilometer/hour to Miles/Hour</option>\n " +
                "        <option value=\"m2k\">Speed - Miles/hour to Kilometer/hour</option>\n " +
                "        <option value=\"w2h\">Time - Weeks to hour</option>\n " +


                " </select>\n"+ "<br />"+

                " <br /><label for=\"Unitconverter\" class=\"sr-only\">Unitconverter</label>\n" +

                "          <input name=\"temp\" type=\"text\" id=\"tempField\" class=\"form-control\" placeholder=\"Value to be converted\" required autofocus>\n <br />" +


                "<br /><button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Convert</button>\n"+


                "</form>\n";

        doc.write(form);
        doc.writeln("</div>");
        if (c!="0") {
            doc.write("<br />");
            doc.write("<h4> Result of the convertion is "+c+"</h4>");

            //doc.write(dropdown);
            //doc.write(tempInFarhenheit);
        }
        response.getWriter().print(doc.toString());
    }


}
