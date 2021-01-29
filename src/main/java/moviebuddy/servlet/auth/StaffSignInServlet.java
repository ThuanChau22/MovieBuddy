package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;

import moviebuddy.dao.BuddyLocation;
import moviebuddy.dao.UserDAO;
import moviebuddy.model.Theatre;
import moviebuddy.model.User;
import moviebuddy.util.Passwords;
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.STAFF_SIGN_IN)
public class StaffSignInServlet extends HttpServlet {
    private static final long serialVersionUID = -7713354808955052674L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String header = request.getHeader("referer");
            if(header != null && header.contains(S.STAFF_SIGN_IN)) {
                // Redirected from sign-up
                // Set previous inputs
                HttpSession session = request.getSession();
                request.setAttribute("staffIdInput", session.getAttribute(S.STAFF_ID_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.STAFF_ID_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);
            }

            // Forward to Staff SignIn page
            request.getRequestDispatcher(S.STAFF_SIGN_IN_PAGE).forward(request, response);
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
            String staffId = V.sanitize(request.getParameter(S.STAFF_ID_PARAM));
            String password = V.sanitize(request.getParameter(S.PASSWORD_PARAM));

            // Validate user inputs
            String errorMessage = V.validateStaffSignInForm(staffId, password);

            // Authenticate user
            if (errorMessage.isEmpty()) {
                User user = userDAO.signInProvider(staffId, password);
                if (user != null) {
                    // Sign in successfully
                    // Set user info in session
                    session.setAttribute(S.CURRENT_SESSION,
                            Passwords.applySHA256(session.getId() + request.getRemoteAddr()));
                    session.setAttribute(S.ACCOUNT_ID, user.getAccountId());
                    session.setAttribute(S.USERNAME, user.getUserName());
                    session.setAttribute(S.STAFF_ID, user.getStaffId());
                    session.setAttribute(S.ROLE, user.getRole());
                    session.setAttribute(S.EMPLOY_THEATRE_ID, user.getTheatre_id());
                    if (user.getZip() != null) {
                        GeocodingResult origin = BuddyLocation.getLocation(user.getZip());
                        Theatre closestTheatre = null;
                        if (origin != null) {
                            closestTheatre = BuddyLocation.getClosetTheatre(origin.placeId);
                        }
                        if (closestTheatre != null) {
                            session.setAttribute(S.CURRENT_THEATRE_ID, closestTheatre.getId());
                            session.setAttribute(S.CURRENT_THEATRE_NAME, closestTheatre.getTheatreName());
                            session.setAttribute(S.ZIPCODE, user.getZip());
                        }
                    }
                } else {
                    errorMessage = "Invalid staff ID/password! Please try again";
                }
            }

            if (errorMessage.isEmpty()) {
                // Redirect to Home page
                response.sendRedirect(S.HOME);
            } else {
                // Back to Staff SignIn page with previous inputs
                session.setAttribute(S.STAFF_ID_INPUT, staffId);
                session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                response.sendRedirect(S.STAFF_SIGN_IN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
