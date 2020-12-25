package moviebuddy.servlet.provider.theatre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/FindTheatreName")
public class FindTheatreNameServlet extends HttpServlet {
    private static final long serialVersionUID = -917262803356930245L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            // Retrieve current theatre id
            HttpSession session = request.getSession();
            Object theatreId = session.getAttribute(S.THEATRE_EDIT_ID);

            // Sanitize parameter
            String theatreName = Validation.sanitize(request.getParameter("theatreName"));

            // Retrieve theater information
            Theatre theatre = theatreDAO.getTheatreByName(theatreName);

            // Duplicated theatre name on create
            boolean duplicateCreateName = theatre != null && theatreId == null;

            // Duplicated theatre name on edit
            boolean duplicateEditName = theatre != null && theatreId != null
                    && !(theatre.getId() + "").equals(theatreId);

            // Check duplcated theatre name
            if (duplicateCreateName || duplicateEditName) {
                out.print("Theatre name already existed\n");
            } else {
                out.print("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR_PAGE);
        }
    }
}
