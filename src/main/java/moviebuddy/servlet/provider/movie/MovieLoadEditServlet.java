package moviebuddy.servlet.provider.movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import moviebuddy.dao.MovieDAO;
import moviebuddy.model.Movie;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/MovieLoadEdit")
public class MovieLoadEditServlet extends HttpServlet {
    private static final long serialVersionUID = 4761952928243992449L;

    private MovieDAO movieDAO;

    public void init() {
        movieDAO = new MovieDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Sanitize parameter
                String movieId = Validation.sanitize(request.getParameter("movieId"));

                // Retrieve movie information
                Movie movie = movieDAO.getMovieById(movieId);
                if (movie != null) {
                    // Set movie information in session
                    session.setAttribute(S.MOVIE_EDIT_ID, movieId);
                    session.setAttribute(S.MOVIE_EDIT_TITLE, movie.getTitle());
                    session.setAttribute(S.MOVIE_EDIT_RELEASE_DATE, movie.getReleaseDate());
                    session.setAttribute(S.MOVIE_EDIT_DURATION, movie.getDuration());
                    session.setAttribute(S.MOVIE_EDIT_TRAILER, movie.getTrailer());
                    session.setAttribute(S.MOVIE_EDIT_DESCRIPTION, movie.getDescription());

                    // Redirect to Edit Movie Information page
                    response.sendRedirect(S.MOVIE_EDIT_PAGE);
                } else {
                    // Back to Manage Movie page
                    response.sendRedirect(S.MANAGE_MOVIE_PAGE);
                }
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
