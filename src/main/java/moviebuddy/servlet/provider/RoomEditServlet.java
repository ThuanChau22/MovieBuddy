package moviebuddy.servlet.provider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.util.Validation;

@WebServlet("/RoomEdit")
public class RoomEditServlet extends HttpServlet {
    private static final long serialVersionUID = 4094214302384168366L;

    private static final String ROOM_NUMBER = "roomNumberEdit";
    private static final String SECTIONS = "roomSectionsEdit";
    private static final String SEATS = "roomSeatsEdit";

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getParameter("action").equals("save")) {
                int theatreId = Integer.parseInt(Validation.sanitize(request.getParameter("theatreId")));
                int roomId = Integer.parseInt(Validation.sanitize(request.getParameter("roomId")));
                int roomNumber = Integer.parseInt(Validation.sanitize(request.getParameter("roomNumber")));
                int sections = Integer.parseInt(Validation.sanitize(request.getParameter("sections")));
                int seats = Integer.parseInt(Validation.sanitize(request.getParameter("seats")));
                String errorMessage = "";
                if (roomId != roomNumber && theatreDAO.getRoomById(theatreId, roomNumber) != null) {
                    errorMessage = "Room number already existed.";
                }
                if (errorMessage.isEmpty()) {
                    errorMessage = theatreDAO.updateRoom(theatreId, roomId, roomNumber, sections, seats);
                }
                if (errorMessage.isEmpty()) {
                    response.sendRedirect("manageroom.jsp");
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("errorMessage", errorMessage);
                    session.setAttribute(ROOM_NUMBER, request.getParameter("roomNumber"));
                    session.setAttribute(SECTIONS, request.getParameter("sections"));
                    session.setAttribute(SEATS, request.getParameter("seats"));
                    response.sendRedirect("roomedit.jsp");
                }
            }
            if (request.getParameter("action").equals("cancel")) {
                response.sendRedirect("manageroom.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
