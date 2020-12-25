package moviebuddy.servlet.provider.staff;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import moviebuddy.dao.UserDAO;
import moviebuddy.model.User;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/StaffDelete")
public class StaffDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = -8552363830506676929L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin and manger
            if (role != null && (role.equals(S.ADMIN) || role.equals(S.MANAGER))) {
                // Sanitize parameter
                String staffId = Validation.sanitize(request.getParameter("staffId"));

                // Retrieve staff account
                User staff = userDAO.getProviderByStaffId(staffId);

                // Check authorized deletion as manager
                String errorMessage = "";
                if (role.equals(S.MANAGER) && staff != null && !staff.getRole().equals(S.FACULTY)) {
                    errorMessage = "Unauthorized deletion";
                }

                // Delete staff account
                if (errorMessage.isEmpty()) {
                    errorMessage = userDAO.deleteProvider(staffId);
                }
                if (!errorMessage.isEmpty()) {
                    session.setAttribute(S.ERROR_MESSAGE, errorMessage);
                }

                // Redirect Manage Staff page
                response.sendRedirect(S.MANAGE_STAFF_PAGE);
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
