package moviebuddy.servlet.provider.theatre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/SelectTheatre")
public class SelectTheatreServlet extends HttpServlet {
    private static final long serialVersionUID = 7741928030965144876L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && role.equals(S.ADMIN)) {
                // Sanitize parameter
                String theatreId = Validation.sanitize(request.getParameter("selectTheatreOption"));

                // Set selected theatre in session
                session.setAttribute(S.SELECTED_THEATRE_ID, theatreId);

                // Redirect to Manage Staff page
                response.sendRedirect(request.getHeader("referer"));
            } else {
                // Redirect to Home page for unauthorize access
                response.sendRedirect(S.HOME_PAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
