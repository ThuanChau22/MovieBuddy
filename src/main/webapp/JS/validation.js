// Element IDs
const USER_NAME_ERROR = "userNameError";
const EMAIL_ERROR = "emailError";
const PASSWORD_ERROR = "passwordError";
const RE_PASSWORD_ERROR = "rePasswordError";
const STAFF_ID_ERROR = "staffIdError";
const ROLE_ERROR = "roleError";
const LOCATION_FORM = "locationForm";
const LOCATION_ERROR = "locationError";
const THEATRE_ID = "theatreId";
const THEATRE_NAME_ERROR = "theatreNameError";
const ZIP_ERROR = "zipError";
const ROOM_ID = "roomId";
const ROOM_NUMBER_ERROR = "roomNumberError";
const SECTIONS_ERROR = "sectionsError";
const SEATS_ERROR = "seatsError";
const ERROR_MESSAGE = "errorMessage";

// Ajax URLs
const FIND_REGISTERED_EMAIL = "FindRegisteredEmail";
const FIND_THEATRE_NAME = "FindTheatreName";
const FIND_ROOM_NUMBER = "FindRoomNumber";

// Input limits
const USERNAME_MIN_LENGTH = 2;
const USER_NAME_MAX_LENGTH = 20;
const PASSWORD_MIN_LENGTH = 8;


// Validate customer signup form
function validateSignUp(form) {
    fail = checkName(form.user_name);
    checkEmail(form.email).then(invalidInput => {
        fail += invalidInput;
        if (fail == "") {
            document.getElementById(form.id).submit();
        }
    });
    fail += checkPassword(form.password);
    fail += checkRePassword(form.id);
    return false;
}

// Validate staff signup form
function validateStaffSignUp(form, isAdmin) {
    fail = "";
    if (isAdmin) {
        fail += checkRole(form.role);
        invalidLocation = "";
        if (form.role.value != "admin") {
            fail += checkLocation(form.theatre_location);
        }
    }
    fail += checkName(form.user_name);
    checkEmail(form.email).then(invalidInput => {
        fail += invalidInput;
        if (fail == "") {
            document.getElementById(form.id).submit();
        }
    });
    fail += checkPassword(form.password);
    return false;
}

// Validate customer signin form
function validateSignIn(form) {
    fail = checkSignInEmail(form.email);
    fail += checkSignInPassword(form.password);
    return fail == "";
}

// Validate staff signin form
function validateStaffSignIn(form) {
    fail = checkSignInStaffId(form.staff_id);
    fail += checkSignInPassword(form.password);
    return fail == "";
}

// Validate theatre form
function validateTheatreForm(form) {
    errorId = document.getElementById(ERROR_MESSAGE);
    errorId.innerHTML = fail = "";
    if (form.theatre_name.value == "" || form.address.value == ""
        || form.city.value == "" || form.state.value == ""
        || form.country.value == "" || form.zip.value == "") {
        errorId.innerHTML = fail += "* required fields\n";
    }
    if (form.theatre_name.value != "") {
        checkTheatreName(form.theatre_name).then(invalidInput => {
            fail += invalidInput;
            if (fail == "") {
                document.getElementById(form.id).submit();
            }
        });
    }
    if (form.zip.value != "") {
        fail += checkZip(form.zip);
    }
    return false;
}

// Validate ticket price form
function validateTicketPriceForm(form) {
    errorId = document.getElementById(ERROR_MESSAGE);
    errorId.innerHTML = "";
    if (form.start_time.value == "") {
        errorId.innerHTML = "Please enter start time\n";
        return false;
    }
    if (form.price.value == "") {
        errorId.innerHTML = "Please enter price\n";
        return false;
    }
    return true;
}

// Validate room form
function validateRoomForm(form) {
    errorId = document.getElementById(ERROR_MESSAGE);
    errorId.innerHTML = fail = "";
    if (form.room_number.value == "" || form.sections.value == "" || form.seats.value == "") {
        errorId.innerHTML = fail += "* required fields\n";
    }
    if (form.room_number.value != "") {
        checkRoomNumber(form.room_number).then(invalidInput => {
            fail += invalidInput;
            if (fail == "") {
                document.getElementById(form.id).submit();
            }
        });
    }
    if (form.sections.value != "") {
        fail += checkSections(form.sections);
    }
    if (form.seats.value != "") {
        fail += checkSeats(form.seats);
    }
    return false;
}

// Validate movie form
function validateMovieForm(form) {
    errorId = document.getElementById(ERROR_MESSAGE);
    errorId.innerHTML = "";
    if (form.title.value == "" || form.release_date.value == ""
        || form.duration.value == "" || form.trailer.value == "" || form.description.value == "") {
        errorId.innerHTML = "* required fields\n";
        return false;
    }
    if (validateNumber(form.duration.value) != "") {
        errorId.innerHTML = "Invalid duration\n";
        return false;
    }
    return true;
}

// Validate schedule form
function validateScheduleForm(form) {
    errorId = document.getElementById(ERROR_MESSAGE);
    errorId.innerHTML = "";
    if (form.show_date.value == "") {
        errorId.innerHTML = "Please enter show date\n";
        return false;
    }
    if (form.start_time.value == "") {
        errorId.innerHTML = "Please enter start time\n";
        return false;
    }
    if (form.room_number.value == "") {
        errorId.innerHTML = "Please select a room\n";
        return false;
    }
    return true;
}

function checkName(nameInput) {
    invalidInput = validateUserName(nameInput.value);
    document.getElementById(USER_NAME_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

async function checkEmail(emailInput) {
    invalidInput = validateEmail(emailInput.value);
    if (invalidInput == "") {
        args = emailInput.name + "=" + emailInput.value;
        invalidInput = await ajaxFunction(args, FIND_REGISTERED_EMAIL);
    }
    document.getElementById(EMAIL_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkPassword(passwordInput) {
    invalidInput = validatePassword(passwordInput.value)
    document.getElementById(PASSWORD_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkRePassword(formId) {
    form = document.getElementById(formId);
    invalidInput = validateRePassword(form.password.value, form.re_password.value);
    document.getElementById(RE_PASSWORD_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkRole(roleInput) {
    invalidInput = validateRole(roleInput.value)
    document.getElementById(ROLE_ERROR).innerHTML = invalidInput;
    if (invalidInput == "" && roleInput.value == "admin") {
        document.getElementById(LOCATION_FORM).setAttribute("hidden", "");
    } else {
        document.getElementById(LOCATION_FORM).removeAttribute("hidden");
    }
    return invalidInput;
}

function checkLocation(locationInput) {
    invalidInput = "";
    if (locationInput.value == "") {
        invalidInput = "Please select a theatre location\n";
    }
    document.getElementById(LOCATION_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkSignInEmail(emailInput) {
    invalidInput = "";
    if (emailInput.value == "") {
        invalidInput = "Please enter your email\n";
    }
    document.getElementById(EMAIL_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkSignInPassword(passwordInput) {
    invalidInput = "";
    if (passwordInput.value == "") {
        invalidInput = "Please enter your password\n";
    }
    document.getElementById(PASSWORD_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkSignInStaffId(staffIdInput) {
    invalidInput = "";
    if (staffIdInput.value == "") {
        invalidInput = "Please enter your staff ID number\n";
    }
    document.getElementById(STAFF_ID_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

async function checkTheatreName(theatreNameInput) {
    invalidInput = "";
    if (theatreNameInput.value != "") {
        args = "";
        theatreIdInput = document.getElementById(THEATRE_ID);
        if (theatreIdInput != null) {
            args += theatreIdInput.name + "=" + theatreIdInput.value + "&";
        }
        args += theatreNameInput.name + "=" + theatreNameInput.value;
        invalidInput = await ajaxFunction(args, FIND_THEATRE_NAME);
    }
    document.getElementById(THEATRE_NAME_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkZip(zipInput) {
    invalidInput = "";
    if (zipInput.value != "" && validateNumber(zipInput.value) != "") {
        invalidInput = "Invalid zip code\n";
    }
    document.getElementById(ZIP_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

async function checkRoomNumber(roomNumberInput) {
    invalidInput = "";
    if (roomNumberInput.value != "" && validateNumber(roomNumberInput.value) != "") {
        invalidInput = "Invalid room number\n";
    }
    if (invalidInput == "") {
        args = "";
        theatreIdInput = document.getElementById(THEATRE_ID);
        if (theatreIdInput != null) {
            args += theatreIdInput.name + "=" + theatreIdInput.value + "&";
        }
        roomIdInput = document.getElementById(ROOM_ID);
        if (roomIdInput != null) {
            args += roomIdInput.name + "=" + roomIdInput.value + "&";
        }
        args += roomNumberInput.name + "=" + roomNumberInput.value;
        invalidInput = await ajaxFunction(args, FIND_ROOM_NUMBER);
    }
    document.getElementById(ROOM_NUMBER_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkSections(sectionsInput) {
    invalidInput = "";
    if (sectionsInput.value != "" && validateNumber(sectionsInput.value) != "") {
        invalidInput = "Invalid number of secions\n";
    }
    document.getElementById(SECTIONS_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function checkSeats(seatsInput) {
    invalidInput = "";
    if (seatsInput.value != "" && validateNumber(seatsInput.value) != "") {
        invalidInput = "Invalid number of seats\n";
    }
    document.getElementById(SEATS_ERROR).innerHTML = invalidInput;
    return invalidInput;
}

function ajaxFunction(args, url) {
    return new Promise((resolve) => {
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", url);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send(args);
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                resolve(this.responseText);
            }
        };
    });
}

function validateUserName(userName) {
    if (userName == "") return "Username field cannot be empty\n";
    if (/[^a-zA-Z0-9]/.test(userName)) return "Username must contain only letters and numbers\n";
    if (!/^[a-zA-Z]/.test(userName)) return "Username must contain a letter as first character\n";
    if (userName.length < USERNAME_MIN_LENGTH) return "Username must contain at least " + USERNAME_MIN_LENGTH + " characters\n";
    if (userName.length > USER_NAME_MAX_LENGTH) return "Username must not contain more than " + USER_NAME_MAX_LENGTH + " characters\n";
    return "";
}

function validateEmail(email) {
    if (email == "") return "Email address field cannot be empty\n";
    if (!/\S+@\S+\.\S+/.test(email)) return "Invalid email address\n";
    return "";
}

function validatePassword(password) {
    if (password == "") return "Passwords field cannot be empty\n";
    if (!/[a-z]/.test(password)) return "Passwords must contain at least a lowercase letter\n";
    if (!/[A-Z]/.test(password)) return "Passwords must contain at least an uppercase letter\n";
    if (!/[0-9]/.test(password)) return "Passwords must contain at least a number\n";
    if (password.length < PASSWORD_MIN_LENGTH) return "Passwords must be at least " + PASSWORD_MIN_LENGTH + " characters\n";
    return "";
}

function validateRePassword(password, rePassword) {
    if (rePassword == "") return "Re-enter passwords field cannot be empty\n";
    if (password != rePassword) return "Passwords are not matched\n";
    return "";
}

function validateRole(role) {
    if (role == "") {
        return "Please select a role\n";
    }
    if (!(role == "admin" || role == "manager" || role == "faculty")) {
        return "Invalid role selected\n";
    }
    return "";
}

function validateNumber(number) {
    if (!/^\d+$/.test(number)) {
        return "Invalid input number\n";
    }
    return "";
}
