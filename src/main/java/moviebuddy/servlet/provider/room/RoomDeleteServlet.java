package moviebuddy.servlet.provider.room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/RoomDelete")
public class RoomDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 5778241877398137416L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Sanitize parameters
                String theatreId = Validation.sanitize(request.getParameter("theatreId"));
                String roomNumber = Validation.sanitize(request.getParameter("roomNumber"));

                // Delete room information
                String errorMessage = theatreDAO.deleteRoom(theatreId, roomNumber);
                if (!errorMessage.isEmpty()) {
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                }

                // Redirect to Manage Room page
                response.sendRedirect(S.MANAGE_ROOM_PAGE);
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
