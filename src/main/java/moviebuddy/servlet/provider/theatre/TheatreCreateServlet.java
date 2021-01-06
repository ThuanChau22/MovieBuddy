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

@WebServlet("/" + S.THEATRE_CREATE)
public class TheatreCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 5383246290874129363L;

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
                // Redirected from theatre-create
                // Set and remove previous inputs from session
                request.setAttribute("nameInput", session.getAttribute(S.THEATRE_NAME_INPUT));
                request.setAttribute("addressInput", session.getAttribute(S.THEATRE_ADDRESS_INPUT));
                request.setAttribute("cityInput", session.getAttribute(S.THEATRE_CITY_INPUT));
                request.setAttribute("stateInput", session.getAttribute(S.THEATRE_STATE_INPUT));
                request.setAttribute("countryInput", session.getAttribute(S.THEATRE_COUNTRY_INPUT));
                request.setAttribute("zipInput", session.getAttribute(S.THEATRE_ZIP_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.THEATRE_NAME_INPUT);
                session.removeAttribute(S.THEATRE_ADDRESS_INPUT);
                session.removeAttribute(S.THEATRE_CITY_INPUT);
                session.removeAttribute(S.THEATRE_STATE_INPUT);
                session.removeAttribute(S.THEATRE_COUNTRY_INPUT);
                session.removeAttribute(S.THEATRE_ZIP_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);

                // Forward to Create Theatre page
                request.getRequestDispatcher(S.THEATRE_CREATE_PAGE).forward(request, response);
            } else {
                // Redirect to Home page for unauthorized access
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
                // Sanitize user inputs
                String theatreName = Validation.sanitize(request.getParameter(S.THEATRE_NAME_PARAM));
                String address = Validation.sanitize(request.getParameter(S.ADDRESS_PARAM));
                String city = Validation.sanitize(request.getParameter(S.CITY_PARAM));
                String state = Validation.sanitize(request.getParameter(S.STATE_PARAM));
                String country = Validation.sanitize(request.getParameter(S.COUNTRY_PARAM));
                String zip = Validation.sanitize(request.getParameter(S.ZIP_PARAM));

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
                    response.sendRedirect(S.THEATRE);
                } else {
                    // Back to Create Theatre page with previous inputs
                    session.setAttribute(S.THEATRE_NAME_INPUT, theatreName);
                    session.setAttribute(S.THEATRE_ADDRESS_INPUT, address);
                    session.setAttribute(S.THEATRE_CITY_INPUT, city);
                    session.setAttribute(S.THEATRE_STATE_INPUT, state);
                    session.setAttribute(S.THEATRE_COUNTRY_INPUT, country);
                    session.setAttribute(S.THEATRE_ZIP_INPUT, zip);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                    response.sendRedirect(S.THEATRE_CREATE);
                }
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
