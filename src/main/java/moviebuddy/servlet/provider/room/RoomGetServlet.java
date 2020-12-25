package moviebuddy.servlet.provider.room;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.model.Room;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/RoomGet")
public class RoomGetServlet extends HttpServlet {
    private static final long serialVersionUID = 7128033020470518231L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            // Retrieve current theatre id
            Object theatreIdObj = session.getAttribute(S.ROOM_THEATRE_ID);
            if (theatreIdObj != null) {
                String theatreId = theatreIdObj.toString();
                // Retrieve list of rooms
                List<Room> rooms = theatreDAO.listRooms(theatreId);
                session.setAttribute(S.ROOM_LIST, rooms);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            // Sanitize parameter
            String theatreId = Validation.sanitize(request.getParameter("theatreId"));

            // Set theatre information in session
            Theatre theatre = theatreDAO.getTheatreById(theatreId);
            session.setAttribute(S.ROOM_THEATRE_NAME, theatre.getTheatreName());
            session.setAttribute(S.ROOM_THEATRE_ID, theatreId);

            // Redirect to Manage Room page
            response.sendRedirect(S.MANAGE_ROOM_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
