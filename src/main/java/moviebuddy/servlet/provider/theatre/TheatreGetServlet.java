package moviebuddy.servlet.provider.theatre;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.S;

@WebServlet("/TheatreGet")
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
            HttpSession session = request.getSession();
            List<Theatre> theatres = theatreDAO.listTheatres();
            session.setAttribute(S.THEATRE_LIST, theatres);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
