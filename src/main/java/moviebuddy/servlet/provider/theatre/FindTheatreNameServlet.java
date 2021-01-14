package moviebuddy.servlet.provider.theatre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.IOException;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.model.Theatre;
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.FIND_THEATRE_NAME)
public class FindTheatreNameServlet extends HttpServlet {
    private static final long serialVersionUID = -917262803356930245L;

    private TheatreDAO theatreDAO;

    public void init() {
        theatreDAO = new TheatreDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize parameter
            String theatreId = V.sanitize(request.getParameter(S.THEATRE_ID_PARAM));
            String theatreName = V.sanitize(request.getParameter(S.THEATRE_NAME_PARAM));

            // Retrieve theater information
            Theatre theatre = theatreDAO.getTheatreByName(theatreName);

            // Duplicated theatre name on create
            boolean duplicateCreateName = theatre != null && theatreId == null;

            // Duplicated theatre name on edit
            boolean duplicateEditName = theatre != null && theatreId != null
                    && !(theatre.getId() + "").equals(theatreId);

            // Check duplicated theatre name
            String result = "";
            if (duplicateCreateName || duplicateEditName) {
                result= "Theatre name already existed\n";
            }

            // Sending response
            PrintWriter out = response.getWriter();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            out.print(result);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
