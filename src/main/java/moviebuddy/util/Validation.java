package moviebuddy.util;

import java.time.LocalDate;
import java.time.LocalTime;
// import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

// import moviebuddy.model.Schedule;
// import moviebuddy.model.ShowTime;

public class Validation {
    private static final int USERNAME_MIN_LENGTH = 2;
    private static final int USERNAME_MAX_LENGTH = 20;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int STAFF_ID_LENGTH = 6;

    private Validation() {
    }

    public static String sanitize(String input) {
        return input != null ? Jsoup.clean(input.trim(), Whitelist.none()) : "";
    }

    public static String validateSignUpForm(String userName, String email, String password, String rePassword) {
        String errorMessage = validateUserName(userName);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = validateEmail(email);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = validatePassword(password);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = validateRePassword(password, rePassword);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        return "";
    }

    public static String validateStaffSignUpForm(String roleInput, String theatreLocation, String userName,
            String email, String password) {
        String errorMessage = validateRole(roleInput);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = validateTheatreLocation(theatreLocation);
        if (!roleInput.equals(S.ADMIN) && !errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = validateUserName(userName);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = validateEmail(email);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = validatePassword(password);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        return "";
    }

    public static String validateSignInForm(String email, String password) {
        if (email.isEmpty()) {
            return "Please enter your email";
        }
        return "";
    }

    public static String validateStaffSignInForm(String staffId, String password) {
        if (staffId.isEmpty()) {
            return "Please enter your staff ID number";
        }
        if (!validateStaffId(staffId).isEmpty()) {
            return "Invalid staff ID/password! Please try again";
        }
        return "";
    }

    public static String validateTheatreForm(String theatreName, String address, String city, String state,
            String country, String zip) {
        if (theatreName.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || country.isEmpty()
                || zip.isEmpty()) {
            return "* required fields";
        }
        if (!validateNumber(zip).isEmpty()) {
            return "Invalid zip code";
        }
        return "";
    }

    public static String validateTicketPriceForm(String startTime, String price) {
        if (startTime.isEmpty()) {
            return "Please enter start time";
        }
        String errorMessage = validateTime(startTime);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        if (price.isEmpty()) {
            return "Please enter price";
        }
        errorMessage = validateDouble(price);
        if (!errorMessage.isEmpty()) {
            return "Invalid price";
        }
        return "";
    }

    public static String validateRoomForm(String roomNumber, String sections, String seats) {
        if (roomNumber.isEmpty() || sections.isEmpty() || seats.isEmpty()) {
            return "* required fields";
        }
        String errorMessage = validateNumber(roomNumber);
        if (!errorMessage.isEmpty()) {
            return "Invalid room number";
        }
        errorMessage = validateNumber(sections);
        if (!errorMessage.isEmpty()) {
            return "Invalid number of secions";
        }
        errorMessage = validateNumber(seats);
        if (!errorMessage.isEmpty()) {
            return "Invalid number of seats";
        }
        return "";
    }

    public static String validateMovieForm(String title, String releaseDate, String duration, String trailer,
            String description) {
        if (title.isEmpty() || releaseDate.isEmpty() || duration.isEmpty() || trailer.isEmpty()
                || description.isEmpty()) {
            return "* required fields";
        }
        String errorMessage = validateDate(releaseDate);
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        if (!validateNumber(duration).isEmpty()) {
            return "Invalid duration";
        }
        return "";
    }

    public static String validateScheduleForm(String showDate, String startTime, String roomNumber) {
        if(showDate.isEmpty()){
            return "Please enter show date";
        }
        String errorMessage = validateDate(showDate);
        if(!errorMessage.isEmpty()){
            return errorMessage;
        }
        if(startTime.isEmpty()){
            return "Please enter start time";
        }
        errorMessage = validateTime(startTime);
        if(!errorMessage.isEmpty()){
            return errorMessage;
        }
        if(roomNumber.isEmpty()){
            return "Please select a room";
        }
        return "";
    }

    public static String validateUserName(String userName) {
        if (userName.isEmpty()) {
            return "Username field cannot be empty\n";
        }
        if (!userName.matches("(.*[a-zA-Z0-9].*)")) {
            return "Username must contain only letters and numbers\n";
        }
        if (!userName.matches("(^[a-zA-Z].*)")) {
            return "Username must a letter as first character\n";
        }
        if (userName.length() < USERNAME_MIN_LENGTH) {
            return "Username must contain at least " + USERNAME_MIN_LENGTH + " characters\n";
        }
        if (userName.length() > USERNAME_MAX_LENGTH) {
            return "Username must not contain more than " + USERNAME_MAX_LENGTH + " characters\n";
        }
        return "";
    }

    public static String validateEmail(String email) {
        if (email.isEmpty()) {
            return "Email address field cannot be empty\n";
        }
        if (!email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            return "Invalid email address\n";
        }
        return "";
    }

    public static String validatePassword(String password) {
        if (password.isEmpty()) {
            return "Passwords field cannot be empty\n";
        }
        if (!password.matches("(.*[a-z].*)")) {
            return "Passwords must contain at least a lowercase letter\n";
        }
        if (!password.matches("(.*[A-Z].*)")) {
            return "Passwords must contain at least an uppercase letter\n";
        }
        if (!password.matches("(.*[0-9].*)")) {
            return "Passwords must contain at least a number\n";
        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            return "Passwords must be at least " + PASSWORD_MIN_LENGTH + " characters\n";
        }
        return "";
    }

    public static String validateRePassword(String password, String rePassword) {
        if (rePassword.isEmpty()) {
            return "Re-enter passwords field cannot be empty\n";
        }
        if (!password.equals(rePassword)) {
            return "Passwords are not matched\n";
        }
        return "";
    }

    public static String validateRole(String role) {
        if (role.isEmpty()) {
            return "Please select a role\n";
        }
        if (!(role.equals("admin") || role.equals("manager") || role.equals("faculty"))) {
            return "Invalid role\n";
        }
        return "";
    }

    public static String validateTheatreLocation(String location) {
        if (location.isEmpty()) {
            return "Please select a theatre location\n";
        }
        return "";
    }

    public static String validateStaffId(String staffId) {
        if (staffId.length() != STAFF_ID_LENGTH || !staffId.matches("[0-9]+")) {
            return "Incorrect staff ID number format\n";
        }
        return "";
    }

    public static String validateNumber(String number) {
        if (!number.matches("[0-9]+")) {
            return "Invalid number input\n";
        }
        return "";
    }

    public static String validateDouble(String number) {
        try {
            Double.parseDouble(number);
        } catch (Exception e) {
            return "Invalid double input\n";
        }
        return "";
    }

    public static String validateDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (Exception e) {
            return "Invalid date input\n";
        }
        return "";
    }

    public static String validateTime(String time) {
        try {
            LocalTime.parse(time);
        } catch (Exception e) {
            return "Invalid time input\n";
        }
        return "";
    }
}
