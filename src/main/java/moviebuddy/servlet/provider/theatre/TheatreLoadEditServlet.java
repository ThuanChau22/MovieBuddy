package moviebuddy.servlet.provider.theatre;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/TheatreLoadEdit")
public class TheatreLoadEditServlet extends HttpServlet {
    private static final long serialVersionUID = 3856058405158486366L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Sanitize parameter
                String theatreId = Validation.sanitize(request.getParameter("theatreId"));

                // Retrieve theatre information
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                if (theatre != null) {
                    // Set theatre information in session
                    session.setAttribute(S.THEATRE_EDIT_ID, theatreId);
                    session.setAttribute(S.THEATRE_EDIT_NAME, theatre.getTheatreName());
                    session.setAttribute(S.THEATRE_EDIT_ADDRESS, theatre.getAddress());
                    session.setAttribute(S.THEATRE_EDIT_CITY, theatre.getCity());
                    session.setAttribute(S.THEATRE_EDIT_STATE, theatre.getState());
                    session.setAttribute(S.THEATRE_EDIT_COUNTRY, theatre.getCountry());
                    session.setAttribute(S.THEATRE_EDIT_ZIP, theatre.getZip());

                    // Redirect to Edit Theatre page
                    response.sendRedirect(S.THEATRE_EDIT_PAGE);
                } else {
                    // Back to Manage Theatre page
                    response.sendRedirect(S.THEATRE_PAGE);
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
