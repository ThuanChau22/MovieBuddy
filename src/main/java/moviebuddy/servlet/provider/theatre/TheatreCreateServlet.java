package moviebuddy.servlet.provider.theatre;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/TheatreCreate")
public class TheatreCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 5383246290874129363L;

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
                // Sanitize user inputs
                String theatreName = Validation.sanitize(request.getParameter("theatreName"));
                String address = Validation.sanitize(request.getParameter("address"));
                String city = Validation.sanitize(request.getParameter("city"));
                String state = Validation.sanitize(request.getParameter("state"));
                String country = Validation.sanitize(request.getParameter("country"));
                String zip = Validation.sanitize(request.getParameter("zip"));

                // Validate user inputs
                String errorMessage = Validation.validateTheatreForm(theatreName, address, city, state, country, zip);
                if(errorMessage.isEmpty() && theatreDAO.getTheatreByName(theatreName) != null){
                    errorMessage = "Theatre name already existed";
                }

                // Upload theatre information
                if (errorMessage.isEmpty()) {
                    errorMessage = theatreDAO.createTheatre(theatreName, address, city, state, country, zip);
                }

                if (errorMessage.isEmpty()) {
                    // Redirect to Manage Theatre page
                    response.sendRedirect(S.MANAGE_THEATRE_PAGE);
                } else {
                    // Back to Create Theatre page with previous inputs
                    session.setAttribute(S.THEATRE_CREATE_NAME, theatreName);
                    session.setAttribute(S.THEATRE_CREATE_ADDRESS, address);
                    session.setAttribute(S.THEATRE_CREATE_CITY, city);
                    session.setAttribute(S.THEATRE_CREATE_STATE, state);
                    session.setAttribute(S.THEATRE_CREATE_COUNTRY, country);
                    session.setAttribute(S.THEATRE_CREATE_ZIP, zip);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.THEATRE_CREATE_PAGE);
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
