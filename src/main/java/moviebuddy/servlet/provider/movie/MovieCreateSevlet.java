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

@WebServlet("/MovieCreate")
@MultipartConfig
public class MovieCreateSevlet extends HttpServlet {
    private static final long serialVersionUID = 3223494789974884818L;

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
                // Sanitize user inputs
                String title = Validation.sanitize(request.getParameter("title"));
                String releaseDate = Validation.sanitize(request.getParameter("releaseDate"));
                String duration = Validation.sanitize(request.getParameter("duration"));
                String trailer = Validation.sanitize(request.getParameter("trailer"));
                Part partPoster = request.getPart("poster");
                InputStream streamPoster = partPoster.getInputStream();
                long posterSize = partPoster.getSize();
                String description = Validation.sanitize(request.getParameter("description"));

                // Validate user inputs
                String errorMessage = Validation.validateMovieForm(title, releaseDate, duration, trailer, description);

                // Upload movie information
                if (errorMessage.isEmpty()) {
                    errorMessage = movieDAO.uploadMovie(title, releaseDate, duration, trailer, streamPoster, posterSize,
                            description);
                }

                if (errorMessage.isEmpty()) {
                    // Redirect to Manage Movie page
                    response.sendRedirect(S.MANAGE_MOVIE_PAGE);
                } else {
                    // Back to Upload Movie Information page
                    session.setAttribute(S.MOVIE_CREATE_TITLE, title);
                    session.setAttribute(S.MOVIE_CREATE_RELEASE_DATE, releaseDate);
                    session.setAttribute(S.MOVIE_CREATE_DURATION, duration);
                    session.setAttribute(S.MOVIE_CREATE_TRAILER, trailer);
                    session.setAttribute(S.MOVIE_CREATE_DESCRIPTION, description);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.MOVIE_CREATE_PAGE);
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
