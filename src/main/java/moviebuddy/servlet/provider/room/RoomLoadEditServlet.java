package moviebuddy.servlet.provider.room;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Room;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/RoomLoadEdit")
public class RoomLoadEditServlet extends HttpServlet {
    private static final long serialVersionUID = 8915502187614190878L;

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

                // Retrieve room from theatre id and room number
                Room room = theatreDAO.getRoomById(theatreId, roomNumber);
                if (room != null) {
                    // Set room information in session
                    session.setAttribute(S.ROOM_EDIT_ID, roomNumber);
                    session.setAttribute(S.ROOM_EDIT_NUMBER, room.getRoomNumber());
                    session.setAttribute(S.ROOM_EDIT_SECTIONS, room.getNumberOfRows());
                    session.setAttribute(S.ROOM_EDIT_SEATS, room.getSeatsPerRow());

                    // Redirect to Edit Room page
                    response.sendRedirect(S.ROOM_EDIT_PAGE);
                } else {
                    // Redirect to Manage Room page
                    response.sendRedirect(S.MANAGE_ROOM_PAGE);
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
