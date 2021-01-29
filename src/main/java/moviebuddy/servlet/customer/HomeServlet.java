package moviebuddy.servlet.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.ZoneId;

import moviebuddy.dao.ScheduleDAO;
import moviebuddy.model.Movie;
import moviebuddy.util.V;
import moviebuddy.util.S;

@WebServlet("/" + S.HOME)
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 156345443434387L;

    private final static int NUMBER_OF_DAYS = 30;
    private static LocalDate initDate;

    private ScheduleDAO scheduleDAO;
    private Gson gson;

    public void init() {
        initDate = LocalDate.now(ZoneId.of("UTC-8"));
        S.initSchedule();
        scheduleDAO = new ScheduleDAO();
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Set and remove previous inputs from session
            HttpSession session = request.getSession();
            request.setAttribute("zipInput", session.getAttribute(S.USER_ZIP_INPUT));
            request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
            session.removeAttribute(S.USER_ZIP_INPUT);
            session.removeAttribute(S.ERROR_MESSAGE);

            // Sanitize user inputs
            String selectedDate = V.sanitize(request.getParameter(S.DATE_PARAM));

            // Get current theatre id
            request.getRequestDispatcher(S.LOCATION).include(request, response);
            String theatreId = "";
            Object theatreIdObj = session.getAttribute(S.CURRENT_THEATRE_ID);
            if (theatreIdObj != null) {
                theatreId = theatreIdObj.toString();
            }

            // Set current date
            LocalDate currentDate = LocalDate.now(ZoneId.of("UTC-8"));

            // Generate schedule on new date
            if (currentDate.isAfter(initDate)) {
                initDate = currentDate;
                S.initSchedule();
            }

            // Validate user inputs
            if (selectedDate.isEmpty() || !V.validateDate(selectedDate).isEmpty()
                    || LocalDate.parse(selectedDate).isBefore(currentDate)
                    || LocalDate.parse(selectedDate).isAfter(currentDate.plusDays(NUMBER_OF_DAYS - 1))) {
                // Initialize selected date and index
                selectedDate = currentDate.toString();
            }

            // Set list of NUMBER_OF_DAYS days from current date
            List<LocalDate> dates = new LinkedList<>();
            for (int i = 0; i < NUMBER_OF_DAYS; i++) {
                LocalDate date = currentDate.plusDays(i);
                dates.add(date);
                if (date.equals(LocalDate.parse(selectedDate))) {
                    request.setAttribute("selectedDateIndex", i);
                }
            }
            request.setAttribute("dateList", dates);

            // Retrieve list of movie schedules on selected date
            List<Movie> schedules = scheduleDAO.listSchedulesByDate(theatreId, selectedDate);
            request.setAttribute("scheduleListEmpty", true);
            if (!schedules.isEmpty()) {
                request.setAttribute("scheduleListEmpty", false);
                request.setAttribute("scheduleList", schedules);
            }

            // Forward to Home page
            request.getRequestDispatcher(S.HOME_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize user inputs
            String selectedDate = V.sanitize(request.getParameter(S.DATE_PARAM));

            // Get current theatre id
            String theatreId = "";
            Object theatreIdObj = request.getSession().getAttribute(S.CURRENT_THEATRE_ID);
            if (theatreIdObj != null) {
                theatreId = theatreIdObj.toString();
            }

            // Retrieve list of movie schedules on selected date
            List<Movie> schedules = new LinkedList<>();
            if (!theatreId.isEmpty() && !selectedDate.isEmpty()) {
                schedules = scheduleDAO.listSchedulesByDate(theatreId, selectedDate);
            }

            // POJO to JSON
            String result = gson.toJson(schedules);

            // Sending response
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(result);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
