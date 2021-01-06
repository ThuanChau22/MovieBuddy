package moviebuddy.servlet.provider.movie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.MovieDAO;
import moviebuddy.model.Movie;
import moviebuddy.util.S;

@WebServlet("/" + S.MOVIE_GET)
public class MovieGetServlet extends HttpServlet {
    private static final long serialVersionUID = 4366896761698484912L;

    private MovieDAO movieDAO;

    public void init() {
        movieDAO = new MovieDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve list of movies
            List<Movie> movies = movieDAO.listMovies();
            request.setAttribute("movieList", movies);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
