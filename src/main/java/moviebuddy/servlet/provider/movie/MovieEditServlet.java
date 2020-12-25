package moviebuddy.servlet.provider.movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
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

@WebServlet("/MovieEdit")
@MultipartConfig
public class MovieEditServlet extends HttpServlet {
    private static final long serialVersionUID = 6293849533123808650L;
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

                // Save action
                if (request.getParameter("action").equals("save")) {
                    // Sanitize user inputs
                    String movieId = Validation.sanitize(request.getParameter("movieId"));
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

                    // Update movie information
                    if(errorMessage.isEmpty()){
                        errorMessage = movieDAO.updateMovie(movieId, title, releaseDate, duration, trailer,
                                streamPoster, posterSize, description);
                    }

                    if (errorMessage.isEmpty()) {
                        // Redirect to Manage Movie page
                        response.sendRedirect(S.MANAGE_MOVIE_PAGE);
                    } else {
                        // Back to Edit Movie page with previous inputs
                        session.setAttribute(S.MOVIE_EDIT_TITLE, title);
                        session.setAttribute(S.MOVIE_EDIT_RELEASE_DATE, releaseDate);
                        session.setAttribute(S.MOVIE_EDIT_DURATION, duration);
                        session.setAttribute(S.MOVIE_EDIT_TRAILER, trailer);
                        session.setAttribute(S.MOVIE_EDIT_DESCRIPTION, description);
                        session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                        response.sendRedirect(S.MOVIE_EDIT_PAGE);
                    }
                }

                // Cancel action
                if (request.getParameter("action").equals("cancel")) {
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
