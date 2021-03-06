package moviebuddy.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/******* Generate schedule *******/
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import moviebuddy.dao.TheatreDAO;
import moviebuddy.dao.MovieDAO;
import moviebuddy.dao.ScheduleDAO;
import moviebuddy.model.Theatre;
import moviebuddy.model.Room;
import moviebuddy.model.Movie;

/*********************************/

public class S {

    private S() {
    }

    // .properties
    public static final String CREDENTIAL = "credential.properties";

    // Servlets
    public static final String HOME = "home";
    public static final String SIGN_UP = "sign-up";
    public static final String SIGN_IN = "sign-in";
    public static final String STAFF_SIGN_IN = "staff-sign-in";
    public static final String SIGN_OUT = "sign-out";
    public static final String THEATRE = "theatre";
    public static final String THEATRE_GET = "theatre-get";
    public static final String THEATRE_CREATE = "theatre-create";
    public static final String THEATRE_EDIT = "theatre-edit";
    public static final String THEATRE_DELETE = "theatre-delete";
    public static final String THEATRE_SELECT = "theatre-select";
    public static final String ROOM = "room";
    public static final String ROOM_GET = "room-get";
    public static final String ROOM_CREATE = "room-create";
    public static final String ROOM_EDIT = "room-edit";
    public static final String ROOM_DELETE = "room-delete";
    public static final String TICKET_PRICE = "ticket-price";
    public static final String TICKET_PRICE_GET = "ticket-price-get";
    public static final String TICKET_PRICE_CREATE = "ticket-price-create";
    public static final String TICKET_PRICE_DELETE = "ticket-price-delete";
    public static final String MOVIE = "movie";
    public static final String MOVIE_GET = "movie-get";
    public static final String MOVIE_CREATE = "movie-create";
    public static final String MOVIE_EDIT = "movie-edit";
    public static final String MOVIE_DELETE = "movie-delete";
    public static final String SCHEDULE = "schedule";
    public static final String SCHEDULE_GET = "schedule-get";
    public static final String SCHEDULE_CREATE = "schedule-create";
    public static final String SCHEDULE_DELETE = "schedule-delete";
    public static final String STAFF = "staff";
    public static final String STAFF_GET = "staff-get";
    public static final String STAFF_CREATE = "staff-create";
    public static final String STAFF_DELETE = "staff-delete";
    public static final String LOCATION = "customer-theatre-location";
    public static final String ROLE_GET = "role-get";
    public static final String SHOWTIME = "showtime";
    public static final String ERROR = "error";
    public static final String SEARCH_MOVIE = "search";
    public static final String FIND_REGISTERED_EMAIL = "find-registered-email";
    public static final String FIND_THEATRE_NAME = "find-theatre-name";
    public static final String FIND_ROOM_NUMBER = "find-room-number";

    // JSP pages
    public static final String HOME_PAGE = "home.jsp";
    public static final String SEARCH_PAGE = "search.jsp";
    public static final String SIGN_UP_PAGE = "signup.jsp";
    public static final String SIGN_IN_PAGE = "signin.jsp";
    public static final String STAFF_SIGN_IN_PAGE = "staffsignin.jsp";
    public static final String THEATRE_PAGE = "theatre.jsp";
    public static final String THEATRE_CREATE_PAGE = "theatrecreate.jsp";
    public static final String THEATRE_EDIT_PAGE = "theatreedit.jsp";
    public static final String ROOM_PAGE = "room.jsp";
    public static final String ROOM_CREATE_PAGE = "roomcreate.jsp";
    public static final String ROOM_EDIT_PAGE = "roomedit.jsp";
    public static final String TICKET_PRICE_PAGE = "ticketprice.jsp";
    public static final String MOVIE_PAGE = "movie.jsp";
    public static final String MOVIE_CREATE_PAGE = "moviecreate.jsp";
    public static final String MOVIE_EDIT_PAGE = "movieedit.jsp";
    public static final String MOVIE_SCHEDULE_PAGE = "movieschedule.jsp";
    public static final String STAFF_PAGE = "staff.jsp";
    public static final String STAFF_CREATE_PAGE = "staffcreate.jsp";
    public static final String SHOWTIME_PAGE = "showtime.jsp";
    public static final String ERROR_PAGE = "error.jsp";
    public static final String CHECK_OUT_PAGE = "checkout.jsp";
    public static final String PROFILE_PAGE = "customerPortal.jsp";

    // Input parameters
    public static final String USERNAME_PARAM = "user_name";
    public static final String EMAIL_PARAM = "email";
    public static final String PASSWORD_PARAM = "password";
    public static final String RE_PASSWORD_PARAM = "re_password";
    public static final String STAFF_ID_PARAM = "staff_id";
    public static final String ROLE_PARAM = "role";
    public static final String THEATRE_LOCATION_PARAM = "theatre_location";
    public static final String THEATRE_ID_PARAM = "theatre_id";
    public static final String THEATRE_OPTION_PARAM = "theatre_option";
    public static final String THEATRE_NAME_PARAM = "theatre_name";
    public static final String ADDRESS_PARAM = "address";
    public static final String CITY_PARAM = "city";
    public static final String STATE_PARAM = "state";
    public static final String COUNTRY_PARAM = "country";
    public static final String ZIP_PARAM = "zip";
    public static final String ROOM_ID_PARAM = "room_id";
    public static final String ROOM_NUMBER_PARAM = "room_number";
    public static final String SECTIONS_PARAM = "sections";
    public static final String SEATS_PARAM = "seats";
    public static final String START_TIME_PARAM = "start_time";
    public static final String PRICE_PARAM = "price";
    public static final String MOVIE_ID_PARAM = "movie_id";
    public static final String TITLE_PARAM = "title";
    public static final String RELEASE_DATE_PARAM = "release_date";
    public static final String DURATION_PARAM = "duration";
    public static final String TRAILER_PARAM = "trailer";
    public static final String POSTER_PARAM = "poster";
    public static final String DESCRIPTION_PARAM = "description";
    public static final String SCHEDULE_ID_PARAM = "schedule_id";
    public static final String SHOW_DATE_PARAM = "show_date";
    public static final String ACTION_PARAM = "action";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_CANCEL = "cancel";
    public static final String DATE_PARAM = "date";

    // Session
    public static final String SESSION_ID = "sessionId";
    public static final String CURRENT_SESSION = "currentSession";

    // User
    public static final String ACCOUNT_ID = "accountId";
    public static final String USERNAME = "userName";
    public static final String ZIPCODE = "zipcode";
    public static final String CURRENT_THEATRE_ID = "currentTheatreId";
    public static final String CURRENT_THEATRE_NAME = "currentTheatreName";
    public static final String STAFF_ID = "staffId";
    public static final String ROLE = "role";
    public static final String EMPLOY_THEATRE_ID = "employTheatreId";
    public static final String SELECTED_THEATRE_ID = "selectTheatreId";
    public static final String ADMIN = "admin";
    public static final String MANAGER = "manager";
    public static final String FACULTY = "faculty";
    public static final String USER_ZIP_INPUT = "prevUserZipInput";


    // Auth
    public static final String EMAIL_INPUT = "prevEmailInput";
    public static final String STAFF_ID_INPUT = "prevStaffIdInput";
    public static final String USERNAME_INPUT = "prevUserNameInput";
    public static final String ROLE_INPUT = "prevRoleInput";
    public static final String STAFF_LOCATION_INPUT = "prevLocationInput";

    // Theatre
    public static final String THEATRE_NAME_INPUT = "prevTheatreNameInput";
    public static final String THEATRE_ADDRESS_INPUT = "prevTheatreAddressInput";
    public static final String THEATRE_CITY_INPUT = "prevTheatreCityInput";
    public static final String THEATRE_STATE_INPUT = "prevTheatreStateInput";
    public static final String THEATRE_COUNTRY_INPUT = "prevTheatreCountryInput";
    public static final String THEATRE_ZIP_INPUT = "prevTheatreZipInput";

    // Room
    public static final String ROOM_NUMBER_INPUT = "prevRoomNumberInput";
    public static final String ROOM_SECTIONS_INPUT = "prevRoomSectionsInput";
    public static final String ROOM_SEATS_INPUT = "prevRoomSeatsInput";

    // Ticket price
    public static final String TICKET_START_TIME_INPUT = "prevTicketStartTimeInput";
    public static final String TICKET_PRICE_INPUT = "prevTicketPriceInput";

    // Movie
    public static final String MOVIE_TITLE_INPUT = "prevMovieTitleInput";
    public static final String MOVIE_RELEASE_DATE_INPUT = "prevMovieReleaseDateInput";
    public static final String MOVIE_DURATION_INPUT = "prevMovieDurationInput";
    public static final String MOVIE_DESCRIPTION_INPUT = "prevMovieDescriptionInput";

    // Schedule
    public static final String SCHEDULE_SHOW_DATE_INPUT = "prevScheduleShowDateInput";
    public static final String SCHEDULE_START_TIME_INPUT = "prevScheduleStartTimeInput";
    public static final String SCHEDULE_ROOM_NUMBER_INPUT = "prevScheduleRoomNumberInput";

    // Error
    public static final String ERROR_MESSAGE = "prevErrorMessage";

    // Get api key
    public static String ggKey() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll("%20",
                " ");
        String dbConfigPath = rootPath + S.CREDENTIAL;

        // Get credential from .properties file
        Properties props = new Properties();
        props.load(new FileInputStream(dbConfigPath));
        return props.getProperty("key");
    }

    // Format date with given pattern
    public static String date(String pattern, LocalDate date) {
        return DateTimeFormatter.ofPattern(pattern).format(date);
    }

    // Generate simple random schedules
    public static void initSchedule() {
        // Parameters
        final int MAX_SHOWDATE = 7;
        final int MAX_THEATRE = 2;
        final int MAX_MOVIE = 5;
        final int MAX_SHOWTIME = 7;
        final String INIT_TIME = "08:00";
        final String TIME_LIMIT = "23:00";

        try {
            Random random = new Random();

            TheatreDAO theatreDAO = new TheatreDAO();
            MovieDAO movieDAO = new MovieDAO();
            ScheduleDAO scheduleDAO = new ScheduleDAO();

            // Set list of dates
            LocalDate currentDate = LocalDate.now(ZoneId.of("UTC-8"));
            List<LocalDate> dates = new LinkedList<>();
            for (int i = 0; i < MAX_SHOWDATE; i++) {
                LocalDate date = currentDate.plusDays(i);
                dates.add(date);
            }

            // Set list of theatres
            List<Theatre> theatres = theatreDAO.listTheatres();
            Collections.shuffle(theatres);
            int numberOfTheatres = Integer.min(MAX_THEATRE, theatres.size());

            // Set lists of movies
            List<Movie> movies = movieDAO.listMovies();
            int numberOfMovies = Integer.min(MAX_MOVIE, movies.size());

            for (LocalDate date : dates) {
                // Get show date
                String showDate = date.toString();
                for (int i = 0; i < numberOfTheatres; i++) {
                    // Get theatre id
                    String theatreId = Integer.toString(theatres.get(i).getId());
                    if (scheduleDAO.listSchedulesByDate(theatreId, showDate).isEmpty()) {
                        // Set list of rooms
                        List<Room> rooms = theatreDAO.listRooms(theatreId);
                        Collections.shuffle(rooms);
                        Iterator<Room> roomIter = rooms.iterator();

                        // Set room number
                        String[] spaceArgs = { "" };
                        boolean hasRoom = setRoom(spaceArgs, roomIter);
                        String roomNumber = spaceArgs[0];

                        // Set initial time and time limit
                        String[] timeArgs = { showDate, INIT_TIME, TIME_LIMIT };
                        if (LocalTime.parse(timeArgs[2]).isAfter(LocalTime.parse("23:30"))) {
                            timeArgs[2] = "23:30";
                        }

                        boolean hasTime = setTime(timeArgs, 0);
                        for (int j = 0; j < numberOfMovies && hasRoom && hasTime; j++) {
                            // Get movie id
                            String movieId = Integer.toString(movies.get(j).getId());
                            int duration = movies.get(j).getDuration();

                            // Randomize number of showtime per movie
                            int maxShowTime = random.nextInt(MAX_SHOWTIME);

                            int showtimeIndex = 0;
                            while (showtimeIndex < maxShowTime && hasRoom && hasTime) {
                                // Get start time & end time
                                String startTime = timeArgs[1];
                                String endTime = LocalTime.parse(startTime).plusMinutes(duration).toString();

                                // Check schedule conflict
                                String errorMessage = "";
                                if (scheduleDAO.getScheduleConflict(theatreId, showDate, movieId, roomNumber, startTime,
                                        endTime) == null) {
                                    // Create schedule
                                    errorMessage = scheduleDAO.addSchedule(theatreId, roomNumber, movieId, showDate,
                                            startTime, endTime);

                                    // Print each result
                                    System.out.println(String.format("%2s|%2s|%2s|%s|%s-%s", theatreId, roomNumber,
                                            movieId, showDate, startTime, endTime));
                                }

                                // Set next default start time
                                hasTime = setTime(timeArgs, 15);

                                // Create schedule successfully
                                if (errorMessage.isEmpty()) {
                                    showtimeIndex++;
                                    // Set follow up start time
                                    hasTime = setTime(timeArgs, duration);
                                }

                                // Out of time
                                if (!hasTime) {
                                    // Set next available room
                                    hasRoom = setRoom(spaceArgs, roomIter);
                                    roomNumber = spaceArgs[0];

                                    // Reset start time
                                    timeArgs[1] = INIT_TIME;
                                    hasTime = setTime(timeArgs, 0);
                                }
                            }

                            // Out of time
                            if (!hasTime) {
                                // Set next available room
                                hasRoom = setRoom(spaceArgs, roomIter);
                                roomNumber = spaceArgs[0];

                                // Reset start time
                                timeArgs[1] = INIT_TIME;
                                hasTime = setTime(timeArgs, 0);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // args { roomNumber }
    private static boolean setRoom(String[] args, Iterator<Room> roomIter) {
        args[0] = roomIter.hasNext() ? Integer.toString(roomIter.next().getRoomNumber()) : "";
        return !args[0].isEmpty();
    }

    // args { currentDate, currentTime, timelimit }
    private static boolean setTime(String[] args, int minutes) {
        LocalDateTime current = LocalDateTime.parse(args[0] + "T" + args[1]).plusMinutes(minutes);
        LocalDateTime limit = LocalDateTime.parse(args[0] + "T" + args[2]);
        args[1] = LocalTime.parse(args[1]).plusMinutes(minutes).toString();
        return current.isBefore(limit);
    }
}
