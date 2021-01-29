package moviebuddy.servlet.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.BuddyLocation;
import moviebuddy.dao.TheatreDAO;
import moviebuddy.dao.UserDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.LOCATION)
public class TheatreLocationServlet extends HttpServlet {
    private static final long serialVersionUID = 671217766527331439L;

    private TheatreDAO theatreDAO;
    private UserDAO userDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object zipObj = session.getAttribute(S.ZIPCODE);
            if (zipObj == null) {
                // Initiate current theatre location
                session.setAttribute(S.ZIPCODE, "");
                session.setAttribute(S.CURRENT_THEATRE_ID, "");
                session.setAttribute(S.CURRENT_THEATRE_NAME, "");
                List<Theatre> theatres = theatreDAO.listTheatres();
                if (!theatres.isEmpty()) {
                    session.setAttribute(S.CURRENT_THEATRE_ID, theatres.get(0).getId());
                    session.setAttribute(S.CURRENT_THEATRE_NAME, theatres.get(0).getTheatreName());
                }
            }

            request.setAttribute("theatreId", session.getAttribute(S.CURRENT_THEATRE_ID));
            request.setAttribute("theatrename", session.getAttribute(S.CURRENT_THEATRE_NAME));
            request.setAttribute("zipcode", session.getAttribute(S.ZIPCODE));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize user input
            String zip = V.sanitize(request.getParameter(S.ZIP_PARAM));

            // Validate user input
            String errorMessage = V.validateZipCodeForm(zip);

            // Check for new zip input
            HttpSession session = request.getSession();
            Object currentZipObj = session.getAttribute(S.ZIPCODE);
            if (currentZipObj == null || !currentZipObj.toString().equals(zip)) {
                // Obtain place id from user input
                GeocodingResult origin = BuddyLocation.getLocation(zip);
                errorMessage = origin == null ? "Location could not be found" : "";

                // Retrieve closest theatre location
                Theatre closestTheatre = null;
                if (errorMessage.isEmpty()) {
                    closestTheatre = BuddyLocation.getClosetTheatre(origin.placeId);
                    errorMessage = closestTheatre == null ? "Theatre location could not be found" : "";
                }

                if (errorMessage.isEmpty()) {
                    // Set new zip code
                    String newZip = "";
                    AddressComponent[] addressComponents = origin.addressComponents;
                    for (AddressComponent addressComponent : addressComponents) {
                        for (AddressComponentType type : addressComponent.types) {
                            if (type.equals(AddressComponentType.POSTAL_CODE)) {
                                newZip = addressComponent.longName;
                            }
                        }
                    }

                    // Set closest theatre location
                    session.setAttribute(S.CURRENT_THEATRE_ID, closestTheatre.getId());
                    session.setAttribute(S.CURRENT_THEATRE_NAME, closestTheatre.getTheatreName());
                    session.setAttribute(S.ZIPCODE, newZip);

                    // Update user's zipcode (login only)
                    Object accountIdObj = session.getAttribute(S.ACCOUNT_ID);
                    if (accountIdObj != null) {
                        String accountId = accountIdObj.toString();
                        errorMessage = userDAO.updateZipCode(accountId, newZip);
                    }
                } 
                
                if (!errorMessage.isEmpty()) {
                    // Back to requested page with previous inputs
                    session.setAttribute(S.USER_ZIP_INPUT, zip);
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                }
            }

            // Redirect to requester
            response.sendRedirect(request.getHeader("referer"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
