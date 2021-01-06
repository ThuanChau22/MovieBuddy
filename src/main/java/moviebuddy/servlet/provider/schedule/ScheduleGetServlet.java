package moviebuddy.servlet.provider.schedule;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;


import moviebuddy.dao.ScheduleDAO;
import moviebuddy.model.Schedule;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.SCHEDULE_GET)
public class ScheduleGetServlet extends HttpServlet {
    private static final long serialVersionUID = 6599031675901730118L;

    private ScheduleDAO scheduleDAO;

    public void init() {
        scheduleDAO = new ScheduleDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin and manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Retrieve theatre id
                String theatreId = "";
                Object theaterIdObj = request.getAttribute("theatreId");
                if (theaterIdObj != null) {
                    theatreId = Validation.sanitize(theaterIdObj.toString());
                }

                // Retrieve movie id
                String movieId = "";
                Object movieIdObj = request.getAttribute("movieId");
                if (movieIdObj != null) {
                    movieId = Validation.sanitize(movieIdObj.toString());
                }

                // Retrieve list of schedules
                List<Schedule> schedules = scheduleDAO.listSchedule(theatreId, movieId);
                request.setAttribute("scheduleList", schedules);
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