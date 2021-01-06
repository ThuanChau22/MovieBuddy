package moviebuddy.servlet.provider.schedule;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalTime;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.dao.MovieDAO;
import moviebuddy.dao.ScheduleDAO;
import moviebuddy.model.Movie;
import moviebuddy.model.Schedule;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.SCHEDULE_CREATE)
public class ScheduleCreateServlet extends HttpServlet {
    private static final long serialVersionUID = -9166508971254120994L;

    private TheatreDAO theatreDAO;
    private MovieDAO movieDAO;
    private ScheduleDAO scheduleDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
        movieDAO = new MovieDAO();
        scheduleDAO = new ScheduleDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin and manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Sanitize user inputs
                String movieId = Validation.sanitize(request.getParameter(S.MOVIE_ID_PARAM));
                String showDate = Validation.sanitize(request.getParameter(S.SHOW_DATE_PARAM));
                String startTime = Validation.sanitize(request.getParameter(S.START_TIME_PARAM));
                String roomNumber = Validation.sanitize(request.getParameter(S.ROOM_NUMBER_PARAM));

                // Retrieve theatre id
                String theatreId = "";
                if (role.equals(S.ADMIN)) { // as admin
                    theatreId = session.getAttribute(S.SELECTED_THEATRE_ID).toString();
                }
                if (role.equals(S.MANAGER)) { // as manager
                    Object theatreIdObj = session.getAttribute(S.EMPLOY_THEATRE_ID);
                    if (theatreIdObj != null) {
                        theatreId = theatreIdObj.toString();
                    }
                }

                // Validate user inputs
                String errorMessage = Validation.validateScheduleForm(showDate, startTime, roomNumber);
                // System.out.println(theatreDAO.getRoomById(theatreId, roomNumber));
                if (errorMessage.isEmpty() && theatreDAO.getRoomById(theatreId, roomNumber) == null) {
                    errorMessage = "Room number does not exist";
                }

                // Obtain end time
                String endTime = "";
                if(errorMessage.isEmpty()){
                    Movie movie = movieDAO.getMovieById(movieId);
                    if(movie != null) {
                        endTime = LocalTime.parse(startTime).plusMinutes(movie.getDuration()).toString();
                    } else {
                        errorMessage = "Unable to obtain schedule end time";
                    }
                }

                // Check for schedule conflict
                if (errorMessage.isEmpty()) {
                    Schedule schedule = scheduleDAO.getScheduleConflict(theatreId, showDate, movieId, roomNumber, startTime, endTime);
                    if (schedule != null) {
                        errorMessage = String.format("Time conflict - Movie#%s | Schedule#%s on %s at %s-%s Room#%s", schedule.getMovieId(), schedule.getScheduleId(), schedule.displayShowDate(), schedule.getStartTime(), schedule.getEndTime(), schedule.getRoomNumber());
                    }
                }

                // Add movie schedule
                if (errorMessage.isEmpty()) {
                    errorMessage = scheduleDAO.addSchedule(theatreId, roomNumber, movieId,
                    showDate, startTime, endTime);
                }

                // Return previous inputs
                if (!errorMessage.isEmpty()) {
                    session.setAttribute(S.SCHEDULE_SHOW_DATE_INPUT, showDate);
                    session.setAttribute(S.SCHEDULE_START_TIME_INPUT, startTime);
                    session.setAttribute(S.SCHEDULE_ROOM_NUMBER_INPUT, roomNumber);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                }

                // Redirect to Manage Schedule page
                response.sendRedirect(S.SCHEDULE + "?" + S.MOVIE_ID_PARAM + "=" + movieId);
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
