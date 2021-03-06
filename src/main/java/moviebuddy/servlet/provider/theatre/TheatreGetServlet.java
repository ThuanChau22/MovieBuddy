package moviebuddy.servlet.provider.theatre;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.S;

@WebServlet("/" + S.THEATRE_GET)
public class TheatreGetServlet extends HttpServlet {
    private static final long serialVersionUID = -4869640868654901643L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve list of theatres
            List<Theatre> theatres = theatreDAO.listTheatres();
            request.setAttribute("theatreListEmpty", true);
            if (!theatres.isEmpty()) {
                request.setAttribute("theatreListEmpty", false);
                request.setAttribute("theatreList", theatres);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
