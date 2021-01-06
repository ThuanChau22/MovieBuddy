package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.UserDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.SIGN_UP)
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 6851275245718964069L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String header = request.getHeader("referer");
            if(header != null && header.contains(S.SIGN_UP)) {
                // Redirected from sign-up
                // Set previous inputs
                HttpSession session = request.getSession();
                request.setAttribute("userNameInput", session.getAttribute(S.USERNAME_INPUT));
                request.setAttribute("emailInput", session.getAttribute(S.EMAIL_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.USERNAME_INPUT);
                session.removeAttribute(S.EMAIL_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);
            }

            // Forward to SignUp page
            request.getRequestDispatcher(S.SIGN_UP_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize user inputs
            String userName = Validation.sanitize(request.getParameter(S.USERNAME_PARAM));
            String email = Validation.sanitize(request.getParameter(S.EMAIL_PARAM));
            String password = Validation.sanitize(request.getParameter(S.PASSWORD_PARAM));
            String rePassword = Validation.sanitize(request.getParameter(S.RE_PASSWORD_PARAM));

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
                request.getRequestDispatcher(S.SIGN_IN).forward(request, response);
            } else {
                // Back to SignUp page with previous inputs
                HttpSession session = request.getSession();
                session.setAttribute(S.USERNAME_INPUT, userName);
                session.setAttribute(S.EMAIL_INPUT, email);
                session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                response.sendRedirect(S.SIGN_UP);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
