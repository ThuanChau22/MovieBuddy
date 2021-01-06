package moviebuddy.servlet.provider.movie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;

import moviebuddy.dao.MovieDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.MOVIE_CREATE)
@MultipartConfig
public class MovieCreateSevlet extends HttpServlet {
    private static final long serialVersionUID = 3223494789974884818L;

    private MovieDAO movieDAO;

    public void init() {
        movieDAO = new MovieDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Redirected from movie-create
                // Set and remove previous inputs from session
                request.setAttribute("titleInput", session.getAttribute(S.MOVIE_TITLE_INPUT));
                request.setAttribute("releaseDateInput", session.getAttribute(S.MOVIE_RELEASE_DATE_INPUT));
                request.setAttribute("durationInput", session.getAttribute(S.MOVIE_DURATION_INPUT));
                request.setAttribute("trailerInput", session.getAttribute(S.MOVIE_TRAILER_INPUT));
                request.setAttribute("descriptionInput", session.getAttribute(S.MOVIE_DESCRIPTION_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.MOVIE_TITLE_INPUT);
                session.removeAttribute(S.MOVIE_RELEASE_DATE_INPUT);
                session.removeAttribute(S.MOVIE_DURATION_INPUT);
                session.removeAttribute(S.MOVIE_TRAILER_INPUT);
                session.removeAttribute(S.MOVIE_DESCRIPTION_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);

                // Forward to Create Movie page
                request.getRequestDispatcher(S.MOVIE_CREATE_PAGE).forward(request, response);
            } else {
                // Redirect to Home page for unauthorized access
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
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Sanitize user inputs
                String title = Validation.sanitize(request.getParameter(S.TITLE_PARAM));
                String releaseDate = Validation.sanitize(request.getParameter(S.RELEASE_DATE_PARAM));
                String duration = Validation.sanitize(request.getParameter(S.DURATION_PARAM));
                String trailer = Validation.sanitize(request.getParameter(S.TRAILER_PARAM));
                Part partPoster = request.getPart(S.POSTER_PARAM);
                InputStream streamPoster = partPoster.getInputStream();
                long posterSize = partPoster.getSize();
                String description = Validation.sanitize(request.getParameter(S.DESCRIPTION_PARAM));

                // Validate user inputs
                String errorMessage = Validation.validateMovieForm(title, releaseDate, duration, trailer, description);

                // Upload movie information
                if (errorMessage.isEmpty()) {
                    errorMessage = movieDAO.uploadMovie(title, releaseDate, duration, trailer, streamPoster, posterSize, description);
                }

                if (errorMessage.isEmpty()) {
                    // Redirect to Manage Movie page
                    response.sendRedirect(S.MOVIE);
                } else {
                    // Back to Upload Movie Information page
                    session.setAttribute(S.MOVIE_TITLE_INPUT, title);
                    session.setAttribute(S.MOVIE_RELEASE_DATE_INPUT, releaseDate);
                    session.setAttribute(S.MOVIE_DURATION_INPUT, duration);
                    session.setAttribute(S.MOVIE_TRAILER_INPUT, trailer);
                    session.setAttribute(S.MOVIE_DESCRIPTION_INPUT, description);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.MOVIE_CREATE);
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
