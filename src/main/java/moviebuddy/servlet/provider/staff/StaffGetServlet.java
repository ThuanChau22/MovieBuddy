package moviebuddy.servlet.provider.staff;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.dao.UserDAO;
import moviebuddy.model.Theatre;
import moviebuddy.model.User;
import moviebuddy.util.S;

@WebServlet("/StaffGet")
public class StaffGetServlet extends HttpServlet {
    private static final long serialVersionUID = 5635940102912001720L;

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
            // Check authorized access as admin and manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                String theatreId = "";

                // Set theatre id as admin
                if (role.equals(S.ADMIN)) {
                    // Retrieve list of admins
                    List<User> admins = userDAO.listAdminUsers();
                    session.setAttribute(S.ADMIN_LIST, admins);

                    // Initiate selected theatre
                    if (session.getAttribute(S.SELECTED_THEATRE_ID) == null) {
                        List<Theatre> theatres = theatreDAO.listTheatres();
                        if (!theatres.isEmpty()) {
                            session.setAttribute(S.SELECTED_THEATRE_ID, theatres.get(0).getId());
                        } else {
                            session.setAttribute(S.SELECTED_THEATRE_ID, "");
                        }
                    }
                    theatreId = session.getAttribute(S.SELECTED_THEATRE_ID).toString();
                }

                // Set theatre id as manager
                if (role.equals(S.MANAGER)) {
                    theatreId = "";
                    Object theatreIdObj = session.getAttribute(S.EMPLOY_THEATRE_ID);
                    if (theatreIdObj != null) {
                        theatreId = theatreIdObj.toString();
                    }
                }

                // Set theatre name in session
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                if (theatre != null) {
                    session.setAttribute(S.SELECTED_THEATRE_NAME, theatre.getTheatreName());
                }

                // Retrieve list of staffs
                List<User> staffs = userDAO.listProviderByTheatreId(theatreId);
                session.setAttribute(S.STAFF_LIST, staffs);
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