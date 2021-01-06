package moviebuddy.servlet.provider.staff;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.S;

@WebServlet("/" + S.STAFF)
public class StaffServlet extends HttpServlet {
    private static final long serialVersionUID = 6120709702185619182L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin and manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Set and remove previous inputs from session
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.ERROR_MESSAGE);

                String theatreId = "";
                if (role.equals(S.ADMIN)) {
                    // Retrieve theatre id as admin
                    request.getRequestDispatcher(S.THEATRE_SELECT).include(request, response);
                    theatreId = session.getAttribute(S.SELECTED_THEATRE_ID).toString();

                    // Retrieve list of theatres
                    request.getRequestDispatcher(S.THEATRE_GET).include(request, response);
                }

                if (role.equals(S.MANAGER)) {
                    // Retrieve theatre id as manager
                    Object theatreIdObj = session.getAttribute(S.EMPLOY_THEATRE_ID);
                    if (theatreIdObj != null) {
                        theatreId = theatreIdObj.toString();
                    }
                }

                // Retrieve theatre information
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                if (theatre != null) {
                    request.setAttribute("theatreId", theatre.getId());
                    request.setAttribute("theatreName", theatre.getTheatreName());

                    // Retrieve list of staff
                    request.getRequestDispatcher(S.STAFF_GET).include(request, response);

                    // Forward to Manage Staff page
                    request.getRequestDispatcher(S.STAFF_PAGE).forward(request, response);
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
