package example;

import com.edge.http.exception.NotFoundException;
import com.edge.http.exception.ServletException;
import com.edge.http.servlet.HttpServletRequest;
import com.edge.http.servlet.HttpServletResponse;
import com.edge.http.servlet.HttpServlet;

public class NotFound extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        throw new NotFoundException();
    }
}
