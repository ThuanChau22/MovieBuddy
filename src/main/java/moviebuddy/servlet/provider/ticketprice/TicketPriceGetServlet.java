package moviebuddy.servlet.provider.ticketprice;

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
import moviebuddy.model.TicketPrice;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/TicketPriceGet")
public class TicketPriceGetServlet extends HttpServlet {
    private static final long serialVersionUID = -1637661127728707360L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String theatreId = "";

            // Retrieve current theatre id
            Object theatreIdObj = session.getAttribute(S.TICKET_THEATRE_ID);
            if (theatreIdObj != null) {
                theatreId = theatreIdObj.toString();
            }

            // Retrieve list of ticket prices
            List<TicketPrice> ticketPrices = theatreDAO.listTicketPrices(theatreId);
            session.setAttribute(S.TICKET_PRICE_LIST, ticketPrices);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            // Sanitize parameter
            String theatreId = Validation.sanitize(request.getParameter("theatreId"));

            // Set current theatre information in session
            Theatre theatre = theatreDAO.getTheatreById(theatreId);
            if (theatre != null) {
                session.setAttribute(S.TICKET_THEATRE_ID, theatreId);
                session.setAttribute(S.TICKET_THEATRE_NAME, theatre.getTheatreName());
            }

            // Redirect to Manage Ticket Price page
            response.sendRedirect(S.MANAGE_TICKET_PRICE_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
