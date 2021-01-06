package moviebuddy.servlet.provider.theatre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.THEATRE_SELECT)
public class SelectTheatreServlet extends HttpServlet {
    private static final long serialVersionUID = 7741928030965144876L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Retrieve theatre id from session
                Object theatreId = session.getAttribute(S.SELECTED_THEATRE_ID);
                if (theatreId == null || theatreDAO.getTheatreById(theatreId.toString()) == null) {
                    // Initiate selected theatre
                    List<Theatre> theatres = theatreDAO.listTheatres();
                    session.setAttribute(S.SELECTED_THEATRE_ID, !theatres.isEmpty() ? theatres.get(0).getId() : "");
                }
            } else {
                // Redirect to Home page for unauthorize access
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
                // Sanitize parameter
                String theatreId = Validation.sanitize(request.getParameter(S.THEATRE_OPTION_PARAM));

                // Set selected theatre in session
                session.setAttribute(S.SELECTED_THEATRE_ID, theatreId);

                // Redirect to requester
                response.sendRedirect(request.getHeader("referer"));
            } else {
                // Redirect to Home page for unauthorize access
                response.sendRedirect(S.HOME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
