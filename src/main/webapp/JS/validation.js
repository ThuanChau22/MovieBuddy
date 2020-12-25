const username_min_length = 2;
const username_max_length = 20;
const password_min_length = 8;

function validateSignUp(form) {
    fail = "";

    invalidUserName = validateUserName(form.userName.value);
    document.getElementById("userNameError").innerHTML = invalidUserName;
    fail += invalidUserName;

    invalidEmail = validateEmail(form.email.value);
    document.getElementById('emailError').innerHTML = invalidEmail;
    fail += invalidEmail;
    if (invalidEmail == "") {
        findRegisteredEmail(form.email.name + "=" + form.email.value).then((result => {
            document.getElementById('emailError').innerHTML = result;
            fail += result;
            if (fail == "") {
                document.getElementById(form.id).submit();
            }
        }));
    }

    invalidPassword = validatePassword(form.password.value);
    document.getElementById("passwordError").innerHTML = invalidPassword;
    fail += invalidPassword;

    invalidRePassword = validateRePassword(form.password.value, form.rePassword.value);
    document.getElementById("rePasswordError").innerHTML = invalidRePassword;
    fail += invalidRePassword;
    return false;
}

function checkName(nameInput, errorId) {
    invalidUserName = validateUserName(nameInput.value);
    document.getElementById(errorId).innerHTML = invalidUserName;
    return invalidUserName;
}

async function checkEmail(emailInput, errorId) {
    invalidEmail = validateEmail(emailInput.value);
    if (invalidEmail == "") {
        invalidEmail = await findRegisteredEmail(emailInput.name + "=" + emailInput.value);
    }
    document.getElementById(errorId).innerHTML = invalidEmail;
    return invalidEmail;
}

function findRegisteredEmail(args) {
    return new Promise((resolve) => {
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "FindRegisteredUser");
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send(args);
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                resolve(this.responseText);
            }
        };
    });
}

function checkPassword(passwordInput, errorId) {
    invalidPassword = validatePassword(passwordInput.value)
    document.getElementById(errorId).innerHTML = invalidPassword;
    return invalidPassword;
}

function checkRePassword(formId, errorId) {
    form = document.getElementById(formId);
    invalidRePassword = validateRePassword(form.password.value, form.rePassword.value);
    document.getElementById(errorId).innerHTML = invalidRePassword;
    return invalidRePassword;
}

function validateStaffSignUp(form, isAdmin) {
    fail = "";

    if (isAdmin){
        invalidRole = validateRole(form.role.value);
        document.getElementById("roleError").innerHTML = invalidRole;
        fail += invalidRole;

        invalidLocation = "";
        if (invalidRole == "" && form.role.value != "admin") {
            invalidLocation = validateTheatreLocation(form.theatreLocation.value);
        }
        document.getElementById("theatreLocationError").innerHTML = invalidLocation;
        fail += invalidLocation;
    }

    invalidUserName = validateUserName(form.userName.value);
    document.getElementById("userNameError").innerHTML = invalidUserName;
    fail += invalidUserName;

    invalidEmail = validateEmail(form.email.value);
    document.getElementById('emailError').innerHTML = invalidEmail;
    fail += invalidEmail;
    if (invalidEmail == "") {
        findRegisteredEmail(form.email.name + "=" + form.email.value).then((result => {
            document.getElementById('emailError').innerHTML = result;
            fail += result;
            if (fail == "") {
                document.getElementById(form.id).submit();
            }
        }));
    }

    invalidPassword = validatePassword(form.password.value);
    document.getElementById("passwordError").innerHTML = invalidPassword;
    fail += invalidPassword;
    return false;
}

function checkRole(roleInput, errorId, theatreLocationInputId) {
    invalidRole = validateRole(roleInput.value)
    document.getElementById(errorId).innerHTML = invalidRole;
    if (invalidRole == "" && roleInput.value == "admin") {
        document.getElementById(theatreLocationInputId).setAttribute("hidden", "");
    } else {
        document.getElementById(theatreLocationInputId).removeAttribute("hidden");
    }
    return invalidRole;
}

function checkTheatreLocation(locationInput, errorId) {
    invalidLocation = validateTheatreLocation(locationInput.value)
    document.getElementById(errorId).innerHTML = invalidLocation;
    return invalidLocation;
}

function validateSignIn(form) {
    fail = checkSignInEmail(form.email, "emailError");
    fail += checkSignInPassword(form.password, "passwordError");
    return fail == "";
}

function checkSignInEmail(emailInput, errorId) {
    invalidInput = "";
    if (emailInput.value == "") {
        invalidInput = "Please enter your email";
    }
    document.getElementById(errorId).innerHTML = invalidInput;
    return invalidInput;
}

function checkSignInPassword(passwordInput, errorId) {
    invalidInput = "";
    if (passwordInput.value == "") {
        invalidInput = "Please enter your password";
    }
    document.getElementById(errorId).innerHTML = invalidInput;
    return invalidInput;
}

function validateStaffSignIn(form) {
    fail = checkSignInStaffId(form.staffId, "staffIdError");
    fail += checkSignInPassword(form.password, "passwordError");
    return fail == "";
}

function checkSignInStaffId(staffIdInput, errorId) {
    invalidInput = "";
    if (staffIdInput.value == "") {
        invalidInput = "Please enter your staff ID number";
    }
    document.getElementById(errorId).innerHTML = invalidInput;
    return invalidInput;
}

function validateTheatreForm(form) {
    theatreName = form.theatreName.value;
    address = form.address.value;
    city = form.city.value;
    state = form.state.value;
    country = form.country.value;
    zip = form.zip.value;
    errorId = document.getElementById("errorMessage");
    errorId.innerHTML = fail = "";
    if (theatreName == "" || address == "" || city == "" || state == "" || country == "" || zip == "") {
        errorId.innerHTML = errorMessage = "* required fields";
        fail += errorMessage;
    }
    if (theatreName != "") {
        findTheatreName(form.theatreName.name + "=" + theatreName).then((result => {
            document.getElementById('theatreNameError').innerHTML = result;
            fail += result;
            if (fail == "") {
                document.getElementById(form.id).submit();
            }
        }));
    }
    if (zip != "" && validateNumber(zip) != "") {
        document.getElementById('zipError').innerHTML = errorMessage = "Invalid zip code";
        fail += errorMessage;
    }
    return false;
}

async function checkTheatreName(theatreNameInput, errorId) {
    errorMessage = "";
    if (theatreNameInput.value != "") {
        errorMessage = await findTheatreName(theatreNameInput.name + "=" + theatreNameInput.value);
    }
    document.getElementById(errorId).innerHTML = errorMessage;
    return errorMessage;
}

function findTheatreName(args) {
    return new Promise((resolve) => {
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "FindTheatreName");
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send(args);
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                resolve(this.responseText);
            }
        };
    });
}

function checkZip(zipInput, errorId) {
    invalidInput = "";
    if (zipInput.value != "" && validateNumber(zipInput.value) != "") {
        invalidInput = "Invalid zip code";
    }
    document.getElementById(errorId).innerHTML = invalidInput;
    return invalidInput;
}

function validateTicketPriceForm(form) {
    startTime = form.startTime.value;
    price = form.price.value;
    errorId = document.getElementById("errorMessage");
    errorId.innerHTML = "";
    if (startTime == "") {
        errorId.innerHTML = "Please enter start time";
        return false;
    }
    if (price == "") {
        errorId.innerHTML = "Please enter price";
        return false;
    }
    return true;
}

function validateRoomForm(form) {
    roomNumber = form.roomNumber.value;
    sections = form.sections.value;
    seats = form.seats.value;
    errorId = document.getElementById("errorMessage");
    errorId.innerHTML = fail = "";
    if (roomNumber == "" || sections == "" || seats == "") {
        errorId.innerHTML = errorMessage = "* required fields";
        fail += errorMessage;
    }
    invalidRoomNumber = "";
    if (validateNumber(roomNumber) != "") {
        errorId.innerHTML = invalidRoomNumber = "Invalid room number";
        fail += invalidRoomNumber;
    }
    if (invalidRoomNumber == "") {
        theatreId = form.theatreId.name + "=" + form.theatreId.value;
        roomNumber = form.roomNumber.name + "=" + roomNumber;
        findRoomNumber(theatreId + "&" + roomNumber).then((result => {
            document.getElementById('roomNumberError').innerHTML = result;
            fail += result;
            if (fail == "") {
                document.getElementById(form.id).submit();
            }
        }));
    }
    if (validateNumber(sections) != "") {
        errorId.innerHTML = errorMessage = "Invalid number of secions";
        fail += errorMessage;
    }
    if (validateNumber(seats) != "") {
        errorId.innerHTML = errorMessage = "Invalid number of seats";
        fail += errorMessage;
    }
    return false;
}

async function checkRoomNumber(theatreId, roomNumber, errorId) {
    theatreInput = document.getElementById(theatreId);
    theatreParam = theatreInput.name + "=" + theatreInput.value;
    roomNumberParam = roomNumber.name + "=" + roomNumber.value;
    errorMessage = "";
    if (roomNumber.value != "") {
        errorMessage = await findRoomNumber(theatreParam + "&" + roomNumberParam);
    }
    document.getElementById(errorId).innerHTML = errorMessage;
    return errorMessage;
}

function findRoomNumber(args) {
    return new Promise((resolve) => {
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "FindRoomNumber");
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send(args);
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                resolve(this.responseText);
            }
        };
    });
}

function validateMovieForm(form) {
    title = form.title.value;
    releaseDate = form.releaseDate.value;
    duration = form.duration.value;
    trailer = form.trailer.value;
    description = form.description.value
    errorId = document.getElementById("errorMessage");
    if (title == "" || releaseDate == "" || duration == "" || trailer == "" || description == "") {
        errorId.innerHTML = "* required fields";
        return false;
    }
    if (validateNumber(duration) != "") {
        errorId.innerHTML = "Invalid duration";
        return false;
    }
    return true;
}

function validateScheduleForm(form) {
    showDate = form.showDate.value;
    startTime = form.startTime.value;
    roomNumber = form.roomNumber.value;
    errorId = document.getElementById("errorMessage");
    errorId.innerHTML = "";
    if (showDate == "") {
        errorId.innerHTML = "Please enter show date";
        return false;
    }
    if (startTime == "") {
        errorId.innerHTML = "Please enter start time";
        return false;
    }
    if (roomNumber == "") {
        errorId.innerHTML = "Please select a room";
        return false;
    }
    return true;
}

function validateUserName(userName) {
    if (userName == "") return "Username field cannot be empty\n";
    if (/[^a-zA-Z0-9]/.test(userName)) return "Username must contain only letters and numbers\n";
    if (!/^[a-zA-Z]/.test(userName)) return "Username must contain a letter as first character\n";
    if (userName.length < username_min_length) return "Username must contain at least " + username_min_length + " characters\n";
    if (userName.length > username_max_length) return "Username must not contain more than " + username_max_length + " characters\n";
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
    if (password.length < password_min_length) return "Passwords must be at least " + password_min_length + " characters\n";
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

function validateTheatreLocation(location) {
    if (location == "") {
        return "Please select a theatre location\n";
    }
    return "";
}

function validateNumber(number) {
    if (!/^\d+$/.test(number)) {
        return "Invalid input number\n";
    }
    return "";
}
