package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.IOException;

import moviebuddy.dao.UserDAO;
import moviebuddy.util.Validation;
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
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            //Sanitize parameter
            String email = Validation.sanitize(request.getParameter(S.EMAIL_PARAM));

            //Check for duplicated email
            if (userDAO.getRegisteredUser(email) != null) {
                out.print("Email is already registered\n");
            } else {
                out.print("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
