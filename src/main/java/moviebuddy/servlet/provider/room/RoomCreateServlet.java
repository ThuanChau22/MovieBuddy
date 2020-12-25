package moviebuddy.servlet.provider.room;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/RoomCreate")
public class RoomCreateServlet extends HttpServlet {
    private static final long serialVersionUID = -4836590538356344837L;

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
                // Sanitize user inputs
                String theatreId = Validation.sanitize(request.getParameter("theatreId"));
                String roomNumber = Validation.sanitize(request.getParameter("roomNumber"));
                String sections = Validation.sanitize(request.getParameter("sections"));
                String seats = Validation.sanitize(request.getParameter("seats"));

                // Validate user inputs
                String errorMessage = Validation.validateRoomForm(roomNumber, sections, seats);
                if(errorMessage.isEmpty() && theatreDAO.getRoomById(theatreId, roomNumber) != null){
                    errorMessage = "Room number already existed";
                }

                // Upload room information
                if (errorMessage.isEmpty()) {
                    errorMessage = theatreDAO.createRoom(theatreId, roomNumber, sections, seats);
                }

                if (errorMessage.isEmpty()) {
                    // Redirect to Manage Room page
                    response.sendRedirect(S.MANAGE_ROOM_PAGE);
                } else {
                    // Back to Create Room page with previous inputs
                    session.setAttribute(S.ROOM_CREATE_NUMBER, roomNumber);
                    session.setAttribute(S.ROOM_CREATE_SECTIONS, sections);
                    session.setAttribute(S.ROOM_CREATE_SEATS, seats);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.ROOM_CREATE_PAGE);
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
