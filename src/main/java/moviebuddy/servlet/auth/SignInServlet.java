package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.UserDAO;
import moviebuddy.model.User;
import moviebuddy.util.Passwords;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.SIGN_IN)
public class SignInServlet extends HttpServlet {
    private static final long serialVersionUID = 4660290895566468329L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String header = request.getHeader("referer");
            if (header != null && header.contains(S.SIGN_IN)) {
                // Redirected from sign-in
                // Set previous inputs
                HttpSession session = request.getSession();
                request.setAttribute("emailInput", session.getAttribute(S.EMAIL_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.EMAIL_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);
            }

            // Forward to SignIn page
            request.getRequestDispatcher(S.SIGN_IN_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();

            // Sanitize user inputs
            String email = Validation.sanitize(request.getParameter(S.EMAIL_PARAM));
            String password = Validation.sanitize(request.getParameter(S.PASSWORD_PARAM));

            // Validate user inputs
            String errorMessage = Validation.validateSignInForm(email, password);

            // Authenticate user
            if (errorMessage.isEmpty()) {
                User user = userDAO.signInCustomer(email, password);
                if (user != null) {
                    // Sign in successfully
                    // Set user info in session
                    session.setAttribute(S.CURRENT_SESSION,
                        Passwords.applySHA256(session.getId() + request.getRemoteAddr()));
                    session.setAttribute(S.ACCOUNT_ID, user.getAccountId());
                    session.setAttribute(S.USERNAME, user.getUserName());
                    session.setAttribute(S.ZIPCODE, user.getZip());
                } else {
                    errorMessage = "Invalid email/password! Please try again";
                }
            }

            if (errorMessage.isEmpty()) {
                // Redirect to Home page
                response.sendRedirect(S.HOME);
            } else {
                // Back to SignIn page with previous inputs
                session.setAttribute(S.EMAIL_INPUT, email);
                session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                response.sendRedirect(S.SIGN_IN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
