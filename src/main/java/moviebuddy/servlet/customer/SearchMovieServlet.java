package moviebuddy.servlet.customer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

import moviebuddy.dao.MovieDAO;
import moviebuddy.model.Movie;
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.SEARCH_MOVIE)
public class SearchMovieServlet extends HttpServlet {
    private static final long serialVersionUID = -1237538741866714268L;

    private MovieDAO movieDAO;
    private Gson gson;

    public void init() {
        movieDAO = new MovieDAO();
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize parameter
            String title = V.sanitize(request.getParameter(S.TITLE_PARAM));

            // Retrieve list of movies
            List<Movie> movies = new LinkedList<>();
            if (!title.isEmpty()) {
                movies = movieDAO.listMovies(title);
            }

            request.setAttribute("input", title);
            request.setAttribute("resultCount", movies.size());
            if (!movies.isEmpty()) {
                request.setAttribute("movieList", movies);
            }

            // Forward to Home page
            request.getRequestDispatcher(S.SEARCH_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize parameter
            String title = V.sanitize(request.getParameter(S.TITLE_PARAM));

            // Retrieve list of movies
            List<Movie> movies = new LinkedList<>();
            if (!title.isEmpty()) {
                movies = movieDAO.listMovies(title);
            }

            // POJO to JSON
            String result = gson.toJson(movies);

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
