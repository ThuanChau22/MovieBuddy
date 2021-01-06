package moviebuddy.servlet.provider.movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.MovieDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.MOVIE_DELETE)
public class MovieDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = -2683675903760366416L;
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
                String movieId = Validation.sanitize(request.getParameter(S.MOVIE_ID_PARAM));

                // Delete movie information
                String errorMessage = movieDAO.deleteMovie(movieId);
                if (!errorMessage.isEmpty()) {
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                }

                // Redirect to Manage Movie page
                response.sendRedirect(S.MOVIE);
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
