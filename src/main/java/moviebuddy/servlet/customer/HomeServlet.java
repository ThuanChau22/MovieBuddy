package moviebuddy.servlet.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;

import moviebuddy.dao.ScheduleDAO;
import moviebuddy.model.Movie;
import moviebuddy.util.Validation;
import moviebuddy.util.S;

@WebServlet("/" + S.HOME)
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 156345443434387L;

    final static int NUMBER_OF_DAYS = 60;

    private ScheduleDAO scheduleDAO;

    public void init() {
        scheduleDAO = new ScheduleDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sanitize user inputs
            String selectedDate = Validation.sanitize(request.getParameter(S.DATE_PARAM));

            // Set current theatre id
            String theatreId = "1";

            // Set current date
            LocalDate currentDate = LocalDate.parse("2020-12-24"); // LocalDate.now();

            // Validate user inputs
            if (selectedDate.isEmpty() || !Validation.validateDate(selectedDate).isEmpty()
                    || LocalDate.parse(selectedDate).isBefore(currentDate)
                    || LocalDate.parse(selectedDate).isAfter(currentDate.plusDays(NUMBER_OF_DAYS - 1))) {
                // Initialize selected date and index
                selectedDate = currentDate.toString();
            }

            // Set list of 60 days from current date
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
            List<Movie> schedules = scheduleDAO.getScheduleByDate(theatreId, selectedDate);
            request.setAttribute("scheduleListEmpty", true);
            if (!schedules.isEmpty()) {
                request.setAttribute("scheduleListEmpty", false);
                request.setAttribute("scheduleList", schedules);
            }

            // Forward to Home page
            RequestDispatcher rd = request.getRequestDispatcher(S.HOME_PAGE);
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(S.ERROR);
        }
    }
}
