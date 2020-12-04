package moviebuddy.servlet.provider;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.MovieDAO;
import moviebuddy.model.Movie;

@WebServlet("/GetMovie")
public class GetMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 4366896761698484912L;
    private MovieDAO movieDAO;

    public void init() {
        movieDAO = new MovieDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Movie> movies = movieDAO.getMoviesInfo();
            request.setAttribute("movies", movies);
        } catch (Exception e) {
            response.sendRedirect("error.jsp");
            e.printStackTrace();
        }
    }
}
