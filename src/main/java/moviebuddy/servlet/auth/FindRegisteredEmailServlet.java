package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.IOException;

import moviebuddy.dao.UserDAO;
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.FIND_REGISTERED_EMAIL)
public class FindRegisteredEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 5105192454349691062L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Sanitize parameter
            String email = V.sanitize(request.getParameter(S.EMAIL_PARAM));

            //Check for duplicated email
            String result = "";
            if (userDAO.getRegisteredUser(email) != null) {
                result = "Email is already registered\n";
            }

            // Sending response
            PrintWriter out = response.getWriter();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            out.print(result);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
