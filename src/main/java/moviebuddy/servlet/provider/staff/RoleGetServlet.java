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
import moviebuddy.model.Role;
import moviebuddy.util.S;

@WebServlet("/RoleGet")
public class RoleGetServlet extends HttpServlet {
    private static final long serialVersionUID = -8371206317700891681L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Object role = session.getAttribute(S.ROLE);
            // Check authorized access as admin
            if (role != null && (role.equals(S.ADMIN))) {
                List<Role> roles = userDAO.listRoles();
                session.setAttribute(S.ROLE_LIST, roles);
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
