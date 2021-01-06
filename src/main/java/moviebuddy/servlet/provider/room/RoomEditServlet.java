package moviebuddy.servlet.provider.room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.model.Room;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.ROOM_EDIT)
public class RoomEditServlet extends HttpServlet {
    private static final long serialVersionUID = 4094214302384168366L;

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
                // Redirected from room-edit
                // Set and remove previous inputs from session
                request.setAttribute("roomNumberInput", session.getAttribute(S.ROOM_NUMBER_INPUT));
                request.setAttribute("sectionsInput", session.getAttribute(S.ROOM_SECTIONS_INPUT));
                request.setAttribute("seatsInput", session.getAttribute(S.ROOM_SEATS_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.ROOM_NUMBER_INPUT);
                session.removeAttribute(S.ROOM_SECTIONS_INPUT);
                session.removeAttribute(S.ROOM_SEATS_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);

                // Sanitize parameters
                String theatreId = Validation.sanitize(request.getParameter(S.THEATRE_ID_PARAM));
                String roomNumber = Validation.sanitize(request.getParameter(S.ROOM_NUMBER_PARAM));

                // Check whether theatre id and room number existed
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                Room room = theatreDAO.getRoomById(theatreId, roomNumber);
                if (theatre != null && room != null) {
                    // Retrieve theatre information
                    request.setAttribute("theatreId", theatre.getId());
                    request.setAttribute("theatreName", theatre.getTheatreName());

                    // Retrieve room information
                    request.setAttribute("roomId", room.getRoomNumber());
                    String header = request.getHeader("referer");
                    if (header == null || !header.contains(S.ROOM_EDIT)) {
                        // Set room information
                        request.setAttribute("roomNumberInput", room.getRoomNumber());
                        request.setAttribute("sectionsInput", room.getNumberOfRows());
                        request.setAttribute("seatsInput", room.getSeatsPerRow());
                    }

                    // Forward to Edit Room page
                    request.getRequestDispatcher(S.ROOM_EDIT_PAGE).forward(request, response);
                } else {
                    // Redirect to Manage Room page
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
                // Sanitize user input
                String theatreId = Validation.sanitize(request.getParameter(S.THEATRE_ID_PARAM));

                String action = request.getParameter(S.ACTION_PARAM);
                switch (action) {
                    // Save action
                    case S.ACTION_SAVE:
                        // Sanitize user inputs
                        String roomId = Validation.sanitize(request.getParameter(S.ROOM_ID_PARAM));
                        String roomNumber = Validation.sanitize(request.getParameter(S.ROOM_NUMBER_PARAM));
                        String sections = Validation.sanitize(request.getParameter(S.SECTIONS_PARAM));
                        String seats = Validation.sanitize(request.getParameter(S.SEATS_PARAM));

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
                            response.sendRedirect(S.ROOM + "?" + S.THEATRE_ID_PARAM + "=" + theatreId);
                        } else {
                            // Back to Edit Room page with previous inputs
                            session.setAttribute(S.ROOM_NUMBER_INPUT, roomNumber);
                            session.setAttribute(S.ROOM_SECTIONS_INPUT, sections);
                            session.setAttribute(S.ROOM_SEATS_INPUT, seats);
                            session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                            response.sendRedirect(S.ROOM_EDIT + "?" + S.THEATRE_ID_PARAM + "=" + theatreId
                                                                + "&" + S.ROOM_NUMBER_PARAM + "=" + roomId);
                        }
                        break;

                    // Cancel action
                    case S.ACTION_CANCEL:
                        // Redirect to Manage Room page
                        response.sendRedirect(S.ROOM + "?" + S.THEATRE_ID_PARAM + "=" + theatreId);
                        break;

                    default:
                        System.out.println("No action specified");
                        response.sendRedirect(S.ERROR);
                        break;
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
