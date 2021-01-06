package moviebuddy.servlet.provider.ticketprice;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.TicketPrice;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.TICKET_PRICE_GET)
public class TicketPriceGetServlet extends HttpServlet {
    private static final long serialVersionUID = -1637661127728707360L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve theatre id
            String theatreId = "";
            Object theaterIdObj = request.getAttribute("theatreId");
            if (theaterIdObj != null) {
                theatreId = Validation.sanitize(theaterIdObj.toString());
            }

            // Retrieve list of ticket prices
            List<TicketPrice> ticketPrices = theatreDAO.listTicketPrices(theatreId);
            request.setAttribute("ticketPriceList", ticketPrices);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
