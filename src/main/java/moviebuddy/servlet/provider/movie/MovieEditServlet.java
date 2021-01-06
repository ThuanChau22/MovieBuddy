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
import moviebuddy.model.Movie;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.MOVIE_EDIT)
@MultipartConfig
public class MovieEditServlet extends HttpServlet {
    private static final long serialVersionUID = 6293849533123808650L;
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
                // Redirected from movie-edit
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

                // Sanitize parameter
                String movieId = Validation.sanitize(request.getParameter(S.MOVIE_ID_PARAM));

                // Retrieve movie information
                Movie movie = movieDAO.getMovieById(movieId);
                if (movie != null) {
                    // Set movie information in session
                    request.setAttribute("movieId", movieId);
                    String header = request.getHeader("referer");
                    if (header == null || !header.contains(S.MOVIE_EDIT)) {
                        request.setAttribute("titleInput", movie.getTitle());
                        request.setAttribute("releaseDateInput", movie.getReleaseDate());
                        request.setAttribute("durationInput", movie.getDuration());
                        request.setAttribute("trailerInput", movie.getTrailer());
                        request.setAttribute("descriptionInput", movie.getDescription());
                    }

                    // Forward to Edit Movie page
                    request.getRequestDispatcher(S.MOVIE_EDIT_PAGE).forward(request, response);
                } else {
                    // Back to Manage Movie page
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                String action = request.getParameter(S.ACTION_PARAM);
                switch (action) {
                    // Save action
                    case S.ACTION_SAVE:
                        // Sanitize user inputs
                        String movieId = Validation.sanitize(request.getParameter(S.MOVIE_ID_PARAM));
                        String title = Validation.sanitize(request.getParameter(S.TITLE_PARAM));
                        String releaseDate = Validation.sanitize(request.getParameter(S.RELEASE_DATE_PARAM));
                        String duration = Validation.sanitize(request.getParameter(S.DURATION_PARAM));
                        String trailer = Validation.sanitize(request.getParameter(S.TRAILER_PARAM));
                        Part partPoster = request.getPart(S.POSTER_PARAM);
                        InputStream streamPoster = partPoster.getInputStream();
                        long posterSize = partPoster.getSize();
                        String description = Validation.sanitize(request.getParameter(S.DESCRIPTION_PARAM));

                        // Validate user inputs
                        String errorMessage = Validation.validateMovieForm(title, releaseDate, duration, trailer,
                                description);

                        // Update movie information
                        if (errorMessage.isEmpty()) {
                            errorMessage = movieDAO.updateMovie(movieId, title, releaseDate, duration, trailer,
                                    streamPoster, posterSize, description);
                        }

                        if (errorMessage.isEmpty()) {
                            // Redirect to Manage Movie page
                            response.sendRedirect(S.MOVIE);
                        } else {
                            // Back to Edit Movie page with previous inputs
                            session.setAttribute(S.MOVIE_TITLE_INPUT, title);
                            session.setAttribute(S.MOVIE_RELEASE_DATE_INPUT, releaseDate);
                            session.setAttribute(S.MOVIE_DURATION_INPUT, duration);
                            session.setAttribute(S.MOVIE_TRAILER_INPUT, trailer);
                            session.setAttribute(S.MOVIE_DESCRIPTION_INPUT, description);
                            session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                            response.sendRedirect(S.MOVIE_EDIT + "?" + S.MOVIE_ID_PARAM + "=" + movieId);
                        }
                        break;

                    // Cancel action
                    case S.ACTION_CANCEL:
                        // Redirect to Manage Movie page
                        response.sendRedirect(S.MOVIE);
                        break;
                
                    default:
                        System.out.println("No action specified");
                        response.sendRedirect(S.ERROR);
                        break;
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
