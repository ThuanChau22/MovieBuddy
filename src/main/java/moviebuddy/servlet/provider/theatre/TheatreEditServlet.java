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

@WebServlet("/TheatreEdit")
public class TheatreEditServlet extends HttpServlet {
    private static final long serialVersionUID = 986833348812078854L;

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

                // Save action
                if (request.getParameter("action").equals("save")) {
                    // Sanitize user inputs
                    String theatreId = Validation.sanitize(request.getParameter("theatreId"));
                    String theatreName = Validation.sanitize(request.getParameter("theatreName"));
                    String address = Validation.sanitize(request.getParameter("address"));
                    String city = Validation.sanitize(request.getParameter("city"));
                    String state = Validation.sanitize(request.getParameter("state"));
                    String country = Validation.sanitize(request.getParameter("country"));
                    String zip = Validation.sanitize(request.getParameter("zip"));

                    // Validate user inputs
                    String errorMessage = Validation.validateTheatreForm(theatreName, address, city, state, country, zip);
                    if (errorMessage.isEmpty()) {
                        Theatre theatre = theatreDAO.getTheatreByName(theatreName);
                        if (theatre != null && !(theatre.getId() + "").equals(theatreId)) {
                            errorMessage = "Theatre name already existed";
                        }
                    }

                    // Update theatre information
                    if (errorMessage.isEmpty()) {
                        errorMessage = theatreDAO.updateTheatre(theatreId, theatreName, address, city, state, country,
                                zip);
                    }

                    if (errorMessage.isEmpty()) {
                        // Redirect to Manage Theatre page
                        response.sendRedirect(S.MANAGE_THEATRE_PAGE);
                    } else {
                        // Back to Edit Theatre page with previous inputs
                        session.setAttribute(S.THEATRE_EDIT_NAME, theatreName);
                        session.setAttribute(S.THEATRE_EDIT_ADDRESS, address);
                        session.setAttribute(S.THEATRE_EDIT_CITY, city);
                        session.setAttribute(S.THEATRE_EDIT_STATE, state);
                        session.setAttribute(S.THEATRE_EDIT_COUNTRY, country);
                        session.setAttribute(S.THEATRE_EDIT_ZIP, zip);
                        session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                        response.sendRedirect(S.THEATRE_EDIT_PAGE);
                    }
                }

                // Cancel action
                if (request.getParameter("action").equals("cancel")) {
                    response.sendRedirect(S.MANAGE_THEATRE_PAGE);
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
