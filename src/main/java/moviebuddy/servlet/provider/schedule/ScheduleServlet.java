package moviebuddy.servlet.provider.schedule;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.dao.MovieDAO;
import moviebuddy.model.Theatre;
import moviebuddy.model.Movie;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.SCHEDULE)
public class ScheduleServlet extends HttpServlet {
    private static final long serialVersionUID = 1203019655469490044L;

    private TheatreDAO theatreDAO;
    private MovieDAO movieDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
        movieDAO = new MovieDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin and manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Set and remove previous inputs from session
                request.setAttribute("showDateInput", session.getAttribute(S.SCHEDULE_SHOW_DATE_INPUT));
                request.setAttribute("startTimeInput", session.getAttribute(S.SCHEDULE_START_TIME_INPUT));
                request.setAttribute("roomNumberInput", session.getAttribute(S.SCHEDULE_ROOM_NUMBER_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.SCHEDULE_SHOW_DATE_INPUT);
                session.removeAttribute(S.SCHEDULE_START_TIME_INPUT);
                session.removeAttribute(S.SCHEDULE_ROOM_NUMBER_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);

                // Sanitize parameter
                String movieId = Validation.sanitize(request.getParameter(S.MOVIE_ID_PARAM));

                String theatreId = "";
                if (role.equals(S.ADMIN)) {
                    // Retrieve theatre id as admin
                    request.getRequestDispatcher(S.THEATRE_SELECT).include(request, response);
                    theatreId = session.getAttribute(S.SELECTED_THEATRE_ID).toString();

                    // Retrieve list of theatres
                    request.getRequestDispatcher(S.THEATRE_GET).include(request, response);
                }

                if (role.equals(S.MANAGER)) {
                    // Retrieve theatre id as manager
                    Object theatreIdObj = session.getAttribute(S.EMPLOY_THEATRE_ID);
                    if (theatreIdObj != null) {
                        theatreId = theatreIdObj.toString();
                    }
                }

                // Check whether theatre id and movie id existed
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                Movie movie = movieDAO.getMovieById(movieId);
                if (theatre != null && movie != null) {
                    // Retrieve theatre information
                    request.setAttribute("theatreId", theatre.getId());
                    request.setAttribute("theatreName", theatre.getTheatreName());

                    // Retrieve list of rooms
                    request.getRequestDispatcher(S.ROOM_GET).include(request, response);

                    // Retrieve movie information
                    request.setAttribute("movieId", movie.getId());
                    request.setAttribute("movieTitle", movie.getTitle());

                    // Retrieve list of schedules
                    request.getRequestDispatcher(S.SCHEDULE_GET).include(request, response);

                    // Forward to Manage Ticket Price page
                    request.getRequestDispatcher(S.MOVIE_SCHEDULE_PAGE).forward(request, response);
                } else {
                    response.sendRedirect(S.MOVIE);
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
