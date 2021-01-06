package moviebuddy.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import java.io.IOException;
import moviebuddy.util.S;

@WebServlet("/" + S.ERROR)
public class ErrorServlet extends HttpServlet {
    private static final long serialVersionUID = -296532786592459322L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set default return link
        request.setAttribute("returnLink", "./" + S.HOME);

        // Set request's address as return link
        String returnLink = request.getHeader("referer");
        if (returnLink != null) {
            request.setAttribute("returnLink", returnLink);
        }

        // Forward to Error page
        RequestDispatcher rd = request.getRequestDispatcher(S.ERROR_PAGE);
        rd.forward(request, response);
    }
}
