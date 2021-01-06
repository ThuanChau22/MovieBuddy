package moviebuddy.servlet.provider.ticketprice;

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

@WebServlet("/" + S.TICKET_PRICE)
public class TicketPriceServlet extends HttpServlet {
    private static final long serialVersionUID = 1122222795181016642L;

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
                // Set and remove previous inputs from session
                request.setAttribute("startTimeInput", session.getAttribute(S.TICKET_START_TIME_INPUT));
                request.setAttribute("priceInput", session.getAttribute(S.TICKET_PRICE_INPUT));
                request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
                session.removeAttribute(S.TICKET_START_TIME_INPUT);
                session.removeAttribute(S.TICKET_PRICE_INPUT);
                session.removeAttribute(S.ERROR_MESSAGE);

                // Sanitize parameter
                String theatreId = Validation.sanitize(request.getParameter(S.THEATRE_ID_PARAM));

                // Retrieve theatre information
                Theatre theatre = theatreDAO.getTheatreById(theatreId);
                if (theatre != null) {
                    request.setAttribute("theatreId", theatre.getId());
                    request.setAttribute("theatreName", theatre.getTheatreName());

                    // Retrieve list of ticket prices
                    request.getRequestDispatcher(S.TICKET_PRICE_GET).include(request, response);

                    // Forward to Manage Ticket Price page
                    request.getRequestDispatcher(S.TICKET_PRICE_PAGE).forward(request, response);
                } else {
                    response.sendRedirect(S.THEATRE);
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
