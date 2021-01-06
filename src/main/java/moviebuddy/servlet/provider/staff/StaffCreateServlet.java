package moviebuddy.servlet.provider.staff;

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

@WebServlet("/" + S.STAFF_CREATE)
public class StaffCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 6851275245718964069L;

    private UserDAO userDAO;
    private TheatreDAO theatreDAO;

    public void init() {
        userDAO = new UserDAO();
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin or manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Redirected from staff-create
                // Set and remove previous inputs from session
                request.setAttribute("roleInput", session.getAttribute(S.ROLE_INPUT));
                request.setAttribute("locationInput", session.getAttribute(S.STAFF_LOCATION_INPUT));
                request.setAttribute("userNameInput", session.getAttribute(S.USERNAME_INPUT));
                request.setAttribute("emailInput", session.getAttribute(S.EMAIL_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.ROLE_INPUT);
                session.removeAttribute(S.STAFF_LOCATION_INPUT);
                session.removeAttribute(S.USERNAME_INPUT);
                session.removeAttribute(S.EMAIL_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);

                if (role.equals(S.ADMIN)) {
                    // Retrieve list of theatres
                    request.getRequestDispatcher(S.THEATRE_GET).include(request, response);

                    // Retrieve list of roles
                    request.getRequestDispatcher(S.ROLE_GET).include(request, response);
                }

                // Forward to Create Staff page
                request.getRequestDispatcher(S.STAFF_CREATE_PAGE).forward(request, response);
            } else {
                // Redirect to Home page for unauthorized access
                response.sendRedirect(S.HOME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
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
                    roleInput = Validation.sanitize(request.getParameter(S.ROLE_PARAM));
                    locationInput = Validation.sanitize(request.getParameter(S.THEATRE_LOCATION_PARAM));
                }
                if (role.equals(S.MANAGER)) {
                    roleInput = S.FACULTY;
                    // Retrieve employ theatre id
                    String theatreId = "";
                    Object employIdObj = session.getAttribute(S.EMPLOY_THEATRE_ID);
                    if (employIdObj != null) {
                        theatreId = employIdObj.toString();
                    }
                    locationInput = theatreId;
                }
                String userName = Validation.sanitize(request.getParameter(S.USERNAME_PARAM));
                String email = Validation.sanitize(request.getParameter(S.EMAIL_PARAM));
                String password = Validation.sanitize(request.getParameter(S.PASSWORD_PARAM));

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
                    response.sendRedirect(S.STAFF);
                } else {
                    // Back to Staff SignUp page with previous inputs
                    session.setAttribute(S.ROLE_INPUT, roleInput);
                    session.setAttribute(S.STAFF_LOCATION_INPUT, locationInput);
                    session.setAttribute(S.USERNAME_INPUT, userName);
                    session.setAttribute(S.EMAIL_INPUT, email);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.STAFF_CREATE);
                }
            } else {
                // Redirect to Home page for unauthorized access
                response.sendRedirect(S.HOME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
