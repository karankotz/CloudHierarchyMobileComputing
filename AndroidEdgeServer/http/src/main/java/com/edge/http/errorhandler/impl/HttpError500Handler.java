
package com.edge.http.errorhandler.impl;

import com.edge.http.errorhandler.AbstractHtmlErrorHandler;
import com.edge.http.servlet.HttpServletResponse;

public class HttpError500Handler extends AbstractHtmlErrorHandler {

    public HttpError500Handler() {
        super(HttpServletResponse.STATUS_INTERNAL_SERVER_ERROR, "Error 500 - The server made a boo boo",
                "<p>No further details are provided</p>", null);
    }

    /**
     * Sets the reason and generates error message for 500 HTTP error
     *
     * @param e Throwable
     */
    public HttpError500Handler setReason(Throwable e) {

        String message = "<p style=\"color: red; font-weight: bold;\">";

        if (e.getMessage() != null && !e.getMessage().equals("")) {
            message += e.getMessage() + " ";
        }

        message += e.getClass().getName() + "</p>\n";

        StackTraceElement[] el = e.getStackTrace();

        message += "<table>\n";

        message += "    <thead>\n";
        message += "        <tr>\n";
        message += "            <th>File</th>\n";
        message += "            <th>Class</th>\n";
        message += "            <th>Method</th>\n";
        message += "            <th>Line</th>\n";
        message += "        </tr>\n";
        message += "    </thead>\n";

        message += "    <tbody>\n";
        for (int i = 0; i < el.length; i++) {
            message += "        <tr>\n";
            message += "            <td>" + el[i].getFileName() + "</td>\n";
            message += "            <td>" + el[i].getClassName() + "</td>\n";
            message += "            <td>" + el[i].getMethodName() + "</td>\n";
            message += "            <td>" + el[i].getLineNumber() + "</td>\n";
            message += "        </tr>\n";
        }
        message += "    </tbody>\n";

        message += "</table>\n";

        this.explanation = message;

        return this;
    }
}
