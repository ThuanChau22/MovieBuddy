package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import moviebuddy.dao.UserDAO;
import moviebuddy.dao.TheatreDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/SignUpStaff")
public class SignUpStaffServlet extends HttpServlet {
    private static final long serialVersionUID = 6851275245718964069L;

    private UserDAO userDAO;
    private TheatreDAO theatreDAO;

    public void init() {
        userDAO = new UserDAO();
        theatreDAO = new TheatreDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin or manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Sanitize user inputs
                String roleInput = "";
                String locationInput = "";
                if (role.equals(S.ADMIN)) {
                    roleInput = Validation.sanitize(request.getParameter("role"));
                    locationInput = Validation.sanitize(request.getParameter("theatreLocation"));
                }
                if (role.equals(S.MANAGER)) {
                    roleInput = S.FACULTY;
                    // Retrieve employ theatre id
                    String employTheatreId = "";
                    Object employIdObj = session.getAttribute(S.EMPLOY_THEATRE_ID);
                    if (employIdObj != null) {
                        employTheatreId = employIdObj.toString();
                    }
                    locationInput = employTheatreId;
                }
                String userName = Validation.sanitize(request.getParameter("userName"));
                String email = Validation.sanitize(request.getParameter("email"));
                String password = Validation.sanitize(request.getParameter("password"));

                // Validate user inputs
                String errorMessage = Validation.validateStaffSignUpForm(roleInput, locationInput, userName, email, password);
                if (errorMessage.isEmpty() && !locationInput.isEmpty() && theatreDAO.getTheatreById(locationInput) == null) {
                    errorMessage = "Theatre location does not exist";
                }
                if (errorMessage.isEmpty() && userDAO.getRegisteredUser(email) != null) {
                    errorMessage = "Email is already registered";
                }

                // Create staff account
                if (errorMessage.isEmpty()) {
                    errorMessage = userDAO.createProvider(roleInput, locationInput, userName, email, password);
                }

                if (errorMessage.isEmpty()) {
                    // Redirect to Manage Staff page
                    response.sendRedirect(S.MANAGE_STAFF_PAGE);
                } else {
                    // Back to Staff SignUp page with previous inputs
                    session.setAttribute(S.SIGN_UP_STAFF_ROLE, roleInput);
                    session.setAttribute(S.SIGN_UP_STAFF_LOCATION, locationInput);
                    session.setAttribute(S.SIGN_UP_STAFF_USERNAME, userName);
                    session.setAttribute(S.SIGN_UP_STAFF_EMAIL, email);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.STAFF_SIGN_UP_PAGE);
                }
            } else {
                // Redirect to Home page for unauthorized access
                response.sendRedirect(S.HOME_PAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
