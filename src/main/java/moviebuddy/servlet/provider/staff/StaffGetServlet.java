package moviebuddy.servlet.provider.staff;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import moviebuddy.dao.UserDAO;
import moviebuddy.model.User;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.STAFF_GET)
public class StaffGetServlet extends HttpServlet {
    private static final long serialVersionUID = 5635940102912001720L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin and manager
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Retrieve theatre id
                String theatreId = "";
                Object theaterIdObj = request.getAttribute("theatreId");
                if (theaterIdObj != null) {
                    theatreId = Validation.sanitize(theaterIdObj.toString());
                }

                // Retrieve list of admins
                if (role.equals(S.ADMIN)) {
                    List<User> admins = userDAO.listAdminUsers();
                    request.setAttribute("adminList", admins);
                }

                // Retrieve list of staffs
                List<User> staffs = userDAO.listProviderByTheatreId(theatreId);
                request.setAttribute("staffList", staffs);
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