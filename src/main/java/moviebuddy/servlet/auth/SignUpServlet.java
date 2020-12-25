package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.io.IOException;

import moviebuddy.dao.UserDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/SignUp")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 6851275245718964069L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();

            // Sanitize user inputs
            String userName = Validation.sanitize(request.getParameter("userName"));
            String email = Validation.sanitize(request.getParameter("email"));
            String password = Validation.sanitize(request.getParameter("password"));
            String rePassword = Validation.sanitize(request.getParameter("rePassword"));

            // Validate user inputs
            String errorMessage = Validation.validateSignUpForm(userName, email, password, rePassword);
            if (errorMessage.isEmpty() && userDAO.getRegisteredUser(email) != null) {
                errorMessage = "Email is already registered";
            }

            // Create user account
            if (errorMessage.isEmpty()) {
                errorMessage = userDAO.createRegisteredUser(userName, email, password);
            }

            if (errorMessage.isEmpty()) {
                // Sign up successfully
                // Process authentication
                RequestDispatcher rd = request.getRequestDispatcher("SignIn");
                rd.forward(request, response);
            } else {
                // Back to SignUp page with previous inputs
                session.setAttribute(S.SIGN_UP_USERNAME, userName);
                session.setAttribute(S.SIGN_UP_EMAIL, email);
                session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                response.sendRedirect(S.SIGN_UP_PAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
