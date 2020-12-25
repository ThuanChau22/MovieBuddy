package moviebuddy.servlet.provider.room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Room;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/RoomEdit")
public class RoomEditServlet extends HttpServlet {
    private static final long serialVersionUID = 4094214302384168366L;

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

                // Save action
                if (request.getParameter("action").equals("save")) {
                    // Sanitize user inputs
                    String theatreId = Validation.sanitize(request.getParameter("theatreId"));
                    String roomId = Validation.sanitize(request.getParameter("roomId"));
                    String roomNumber = Validation.sanitize(request.getParameter("roomNumber"));
                    String sections = Validation.sanitize(request.getParameter("sections"));
                    String seats = Validation.sanitize(request.getParameter("seats"));

                    // Validate user inputs
                    String errorMessage = Validation.validateRoomForm(roomNumber, sections, seats);
                    if (errorMessage.isEmpty()) {
                        Room room = theatreDAO.getRoomById(theatreId, roomNumber);
                        if (room != null && !roomId.equals(roomNumber)) {
                            errorMessage = "Room number already existed";
                        }
                    }

                    // Update room information
                    if (errorMessage.isEmpty()) {
                        errorMessage = theatreDAO.updateRoom(theatreId, roomId, roomNumber, sections, seats);
                    }

                    if (errorMessage.isEmpty()) {
                        // Redirect to Manage Room page
                        response.sendRedirect(S.MANAGE_ROOM_PAGE);
                    } else {
                        // Back to Edit Room page with previous inputs
                        session.setAttribute(S.ROOM_EDIT_NUMBER, roomNumber);
                        session.setAttribute(S.ROOM_EDIT_SECTIONS, sections);
                        session.setAttribute(S.ROOM_EDIT_SEATS, seats);
                        session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                        response.sendRedirect(S.ROOM_EDIT_PAGE);
                    }
                }

                // Cancel action
                if (request.getParameter("action").equals("cancel")) {
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
