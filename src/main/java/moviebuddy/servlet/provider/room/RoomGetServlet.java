package moviebuddy.servlet.provider.room;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Room;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.ROOM_GET)
public class RoomGetServlet extends HttpServlet {
    private static final long serialVersionUID = 7128033020470518231L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve theatre id
            String theatreId = "";
            Object theaterIdObj = request.getAttribute("theatreId");
            if (theaterIdObj != null) {
                theatreId = Validation.sanitize(theaterIdObj.toString());
            }

            // Retrieve list of rooms
            List<Room> rooms = theatreDAO.listRooms(theatreId);
            request.setAttribute("roomList", rooms);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
