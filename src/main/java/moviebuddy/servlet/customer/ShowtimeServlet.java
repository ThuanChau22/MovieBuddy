package moviebuddy.servlet.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.ZoneId;

import moviebuddy.dao.MovieDAO;
import moviebuddy.dao.ScheduleDAO;
import moviebuddy.model.Movie;
import moviebuddy.model.Schedule;
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.SHOWTIME)
public class ShowtimeServlet extends HttpServlet {
    private static final long serialVersionUID = -4284277343704694564L;

    final static int NUMBER_OF_DAYS = 30;

    private MovieDAO movieDAO;
    private ScheduleDAO scheduleDAO;
    private Gson gson;

    public void init() {
        movieDAO = new MovieDAO();
        scheduleDAO = new ScheduleDAO();
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize user inputs
            String movieId = V.sanitize(request.getParameter(S.MOVIE_ID_PARAM));
            String selectedDate = V.sanitize(request.getParameter(S.DATE_PARAM));

            // Set current theatre id
            String theatreId = "32";

            // Set current date
            LocalDate currentDate = LocalDate.now(ZoneId.of("UTC-8"));

            // Validate user inputs
            if (selectedDate.isEmpty() || !V.validateDate(selectedDate).isEmpty()
                    || LocalDate.parse(selectedDate).isBefore(currentDate)
                    || LocalDate.parse(selectedDate).isAfter(currentDate.plusDays(NUMBER_OF_DAYS - 1))) {
                // Initialize selected date and index
                selectedDate = currentDate.toString();
            }

            // Set list of NUMBER_OF_DAYS days from current date
            List<LocalDate> dates = new LinkedList<>();
            for (int i = 0; i < NUMBER_OF_DAYS; i++) {
                LocalDate date = currentDate.plusDays(i);
                dates.add(date);
                if (date.equals(LocalDate.parse(selectedDate))) {
                    request.setAttribute("selectedDateIndex", i);
                }
            }
            request.setAttribute("dateList", dates);

            // Retrieve movie info
            Movie movie = movieDAO.getMovieById(movieId);
            if (movie != null) {
                request.setAttribute("movie", movie);

                // Retrieve schedules from movie Id on selected date
                List<Schedule> schedules = scheduleDAO.listSchedulesByMovieDate(theatreId, movieId, selectedDate);
                request.setAttribute("scheduleListEmpty", true);
                if (!schedules.isEmpty()) {
                    request.setAttribute("scheduleListEmpty", false);
                    request.setAttribute("scheduleList", schedules);
                }

                // Forward to Home page
                request.getRequestDispatcher(S.SHOWTIME_PAGE).forward(request, response);
            } else {
                // Redirect to Home page
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
            // Sanitize user inputs
            String movieId = V.sanitize(request.getParameter(S.MOVIE_ID_PARAM));
            String selectedDate = V.sanitize(request.getParameter(S.DATE_PARAM));

            // Set current theatre id
            String theatreId = "32";

            // Retrieve schedules from movie Id on selected date
            List<Schedule> schedules = new LinkedList<>();
            if (!theatreId.isEmpty() && !movieId.isEmpty() && !selectedDate.isEmpty()) {
                schedules = scheduleDAO.listSchedulesByMovieDate(theatreId, movieId, selectedDate);
            }

            // POJO to JSON
            String result = gson.toJson(schedules);

            // Sending response
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(result);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
