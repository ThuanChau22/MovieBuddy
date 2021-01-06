package moviebuddy.servlet.provider.room;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.ROOM)
public class RoomServlet extends HttpServlet {
    private static final long serialVersionUID = -6097339470568911718L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Set and remove previous inputs from session
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.ERROR_MESSAGE);

                // Sanitize parameter
                String theatreId = Validation.sanitize(request.getParameter(S.THEATRE_ID_PARAM));

                // Retrieve theatre information
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                if (theatre != null) {
                    request.setAttribute("theatreId", theatre.getId());
                    request.setAttribute("theatreName", theatre.getTheatreName());

                    // Retrieve list of rooms
                    request.getRequestDispatcher(S.ROOM_GET).include(request, response);

                    // Forward to Manage Room page
                    request.getRequestDispatcher(S.ROOM_PAGE).forward(request, response);
                } else {
                    response.sendRedirect(S.THEATRE);
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
