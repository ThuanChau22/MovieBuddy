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
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.ROOM_CREATE)
public class RoomCreateServlet extends HttpServlet {
    private static final long serialVersionUID = -4836590538356344837L;

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
                // Redirected from room-create
                // Set and remove previous inputs from session
                request.setAttribute("roomNumberInput", session.getAttribute(S.ROOM_NUMBER_INPUT));
                request.setAttribute("sectionsInput", session.getAttribute(S.ROOM_SECTIONS_INPUT));
                request.setAttribute("seatsInput", session.getAttribute(S.ROOM_SEATS_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.ROOM_NUMBER_INPUT);
                session.removeAttribute(S.ROOM_SECTIONS_INPUT);
                session.removeAttribute(S.ROOM_SEATS_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);

                // Sanitize parameter
                String theatreId = V.sanitize(request.getParameter(S.THEATRE_ID_PARAM));

                // Check whether theatre id existed
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                if (theatre != null) {
                    // Retrieve theatre information
                    request.setAttribute("theatreId", theatre.getId());
                    request.setAttribute("theatreName", theatre.getTheatreName());

                    // Forward to Create Room page
                    request.getRequestDispatcher(S.ROOM_CREATE_PAGE).forward(request, response);
                } else {
                    // Back to Manage Room page
                    response.sendRedirect(S.ROOM + "?" + S.THEATRE_ID_PARAM + "=" + theatreId);
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Sanitize user inputs
                String theatreId = V.sanitize(request.getParameter(S.THEATRE_ID_PARAM));
                String roomNumber = V.sanitize(request.getParameter(S.ROOM_NUMBER_PARAM));
                String sections = V.sanitize(request.getParameter(S.SECTIONS_PARAM));
                String seats = V.sanitize(request.getParameter(S.SEATS_PARAM));

                // Validate user inputs
                String errorMessage = V.validateRoomForm(roomNumber, sections, seats);
                if(errorMessage.isEmpty() && theatreDAO.getRoomById(theatreId, roomNumber) != null) {
                    errorMessage = "Room number already existed";
                }

                // Upload room information
                if (errorMessage.isEmpty()) {
                    errorMessage = theatreDAO.createRoom(theatreId, roomNumber, sections, seats);
                }

                if (errorMessage.isEmpty()) {
                    // Redirect to Manage Room page
                    response.sendRedirect(S.ROOM + "?" + S.THEATRE_ID_PARAM + "=" + theatreId);
                } else {
                    // Back to Create Room page with previous inputs
                    session.setAttribute(S.ROOM_NUMBER_INPUT, roomNumber);
                    session.setAttribute(S.ROOM_SECTIONS_INPUT, sections);
                    session.setAttribute(S.ROOM_SEATS_INPUT, seats);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.ROOM_CREATE + "?" + S.THEATRE_ID_PARAM + "=" + theatreId);
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
