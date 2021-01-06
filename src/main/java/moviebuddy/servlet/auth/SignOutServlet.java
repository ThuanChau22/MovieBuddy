package moviebuddy.servlet.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Enumeration;

import moviebuddy.util.S;

@WebServlet("/" + S.SIGN_OUT)
public class SignOutServlet extends HttpServlet {
    private static final long serialVersionUID = -5845132156063049133L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            // Clear all objects and invalidate session
            Enumeration<String> attributes = session.getAttributeNames();
            while (attributes.hasMoreElements()) {
                session.removeAttribute(attributes.nextElement());
            }
            session.invalidate();

            //Redirect to Home page
            response.sendRedirect(S.HOME);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
