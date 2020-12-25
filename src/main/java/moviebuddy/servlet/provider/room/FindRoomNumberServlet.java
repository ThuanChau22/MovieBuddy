package moviebuddy.servlet.provider.room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Room;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/FindRoomNumber")
public class FindRoomNumberServlet extends HttpServlet {
    private static final long serialVersionUID = 7974334247351203743L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            // Retrieve current room number
            HttpSession session = request.getSession();
            Object roomId = session.getAttribute(S.ROOM_EDIT_ID);

            // Sanitize paramaters
            String theatreId = Validation.sanitize(request.getParameter("theatreId"));
            String roomNumber = Validation.sanitize(request.getParameter("roomNumber"));

            // Retrieve room from theatre id and room number
            Room room = theatreDAO.getRoomById(theatreId, roomNumber);

            // Duplicated room number on create
            boolean duplicateCreateNumber = room != null && roomId == null;

            // Duplicated room number on edit
            boolean duplicateEditNumber = room != null && roomId != null && !roomId.equals(roomNumber);

            // Check for duplicated room number
            if (duplicateCreateNumber || duplicateEditNumber) {
                out.print("Room number already existed\n");
            } else {
                out.print("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
