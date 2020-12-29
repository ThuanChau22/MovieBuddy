package moviebuddy.servlet.provider.schedule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.ScheduleDAO;
import moviebuddy.model.Schedule;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/ScheduleDelete")
public class ScheduleDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = -8764893949095966660L;

    private ScheduleDAO scheduleDAO;

    public void init() {
        scheduleDAO = new ScheduleDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin and manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Sanitize parameter
                String scheduleId = Validation.sanitize(request.getParameter("scheduleId"));

                // Check authorized deletion as manager
                String errorMessage = "";
                if(role.equals(S.MANAGER)) {
                    String theatreId = "";
                    Object theatreIdObj = session.getAttribute(S.EMPLOY_THEATRE_ID);
                    if (theatreIdObj != null) {
                        theatreId = theatreIdObj.toString();
                    }

                    // Retrieve schedule information
                    Schedule schedule = scheduleDAO.getScheduleById(scheduleId);

                    if(theatreId.isEmpty() || !theatreId.equals(schedule.getTheatreId() + "")){
                        errorMessage = "Unauthorized deletion";
                    }
                }

                // Delete schedule
                if(errorMessage.isEmpty()){
                    errorMessage = scheduleDAO.deleteSchedule(scheduleId);
                }
                if (!errorMessage.isEmpty()) {
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                }

                // Redirect to Manage Schedule page
                response.sendRedirect(S.MOVIE_SCHEDULE_PAGE);
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
