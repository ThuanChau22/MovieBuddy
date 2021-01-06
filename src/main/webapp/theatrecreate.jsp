<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="moviebuddy.util.Passwords" %>
<%@ page import="moviebuddy.util.S" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setHeader("Expires", "0"); // Proxies

    // Initiate session
    session = request.getSession();
    Object sessionId = session.getAttribute(S.SESSION_ID);
    if (sessionId == null || !sessionId.equals(Passwords.applySHA256(session.getId()))) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute(S.SESSION_ID, Passwords.applySHA256(session.getId()));
    }

    // Check authentication as admin
    Object accountId = session.getAttribute(S.ACCOUNT_ID);
    Object currentSession = session.getAttribute(S.CURRENT_SESSION);
    Object staffId = session.getAttribute(S.STAFF_ID);
    Object role = session.getAttribute(S.ROLE);
    if(accountId == null || !currentSession.equals(Passwords.applySHA256(session.getId() + request.getRemoteAddr())) || staffId == null || !(role.equals(S.ADMIN))){
        response.sendRedirect(S.HOME);
    }

    // ${nameInput}
    // ${addressInput}
    // ${cityInput}
    // ${stateInput}
    // ${countryInput}
    // ${zipInput}
    // ${errorMessage}
%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
        integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/style.css">
    <link rel="icon" href="./images/MovieBuddy.ico">
    <title>Movie Buddy | Manage Theatre</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./${S.NAV_BAR_PAGE}" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h3>Theatre</h3>
                <hr>
                <h1 class="display-3 text-center">Upload Theatre Information</h1>
                <hr>
                <a class="inputAsLink" href="./${S.THEATRE}">&lsaquo;<span>Back</span>
                </a>
                <div class="row">
                    <div class="col-lg-3"></div>
                    <div class="col-lg">
                        <!-- Error message -->
                        <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                        <!-- Upload theatre information form -->
                        <form id="uploadTheatreForm" action="${S.THEATRE_CREATE}" method="POST"
                            onsubmit="return validateTheatreForm(this)">
                            <!-- Input theatre name -->
                            <div class="form-group">
                                <label>Theatre Name</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="${S.THEATRE_NAME_PARAM}" type="text"
                                    placeholder="Buddy###" onkeyup="checkTheatreName(this)" value="${nameInput}" />
                                <br>
                                <!-- Theatre name error -->
                                <span id="theatreNameError" class="errormessage"></span>
                            </div>
                            <!-- Input address -->
                            <div class="form-group">
                                <label>Address</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="${S.ADDRESS_PARAM}" type="text" placeholder="1234 Main St"
                                    value="${addressInput}" />
                            </div>
                            <!-- Input city -->
                            <div class="form-group">
                                <label>City</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="${S.CITY_PARAM}" type="text" placeholder="San Francisco"
                                    value="${cityInput}" />
                            </div>
                            <!-- input state -->
                            <div class="form-group">
                                <label>State</label><span class="errormessage">*</span><br>
                                <select class="inputbox" id="state" name="${S.STATE_PARAM}">
                                    <option id="default" hidden selected value="">Select a State</option>
                                    <option value="AL">Alabama</option>
                                    <option value="AK">Alaska</option>
                                    <option value="AZ">Arizona</option>
                                    <option value="AR">Arkansas</option>
                                    <option value="CA">California</option>
                                    <option value="CO">Colorado</option>
                                    <option value="CT">Connecticut</option>
                                    <option value="DE">Delaware</option>
                                    <option value="DC">District Of Columbia</option>
                                    <option value="FL">Florida</option>
                                    <option value="GA">Georgia</option>
                                    <option value="HI">Hawaii</option>
                                    <option value="ID">Idaho</option>
                                    <option value="IL">Illinois</option>
                                    <option value="IN">Indiana</option>
                                    <option value="IA">Iowa</option>
                                    <option value="KS">Kansas</option>
                                    <option value="KY">Kentucky</option>
                                    <option value="LA">Louisiana</option>
                                    <option value="ME">Maine</option>
                                    <option value="MD">Maryland</option>
                                    <option value="MA">Massachusetts</option>
                                    <option value="MI">Michigan</option>
                                    <option value="MN">Minnesota</option>
                                    <option value="MS">Mississippi</option>
                                    <option value="MO">Missouri</option>
                                    <option value="MT">Montana</option>
                                    <option value="NE">Nebraska</option>
                                    <option value="NV">Nevada</option>
                                    <option value="NH">New Hampshire</option>
                                    <option value="NJ">New Jersey</option>
                                    <option value="NM">New Mexico</option>
                                    <option value="NY">New York</option>
                                    <option value="NC">North Carolina</option>
                                    <option value="ND">North Dakota</option>
                                    <option value="OH">Ohio</option>
                                    <option value="OK">Oklahoma</option>
                                    <option value="OR">Oregon</option>
                                    <option value="PA">Pennsylvania</option>
                                    <option value="RI">Rhode Island</option>
                                    <option value="SC">South Carolina</option>
                                    <option value="SD">South Dakota</option>
                                    <option value="TN">Tennessee</option>
                                    <option value="TX">Texas</option>
                                    <option value="UT">Utah</option>
                                    <option value="VT">Vermont</option>
                                    <option value="VA">Virginia</option>
                                    <option value="WA">Washington</option>
                                    <option value="WV">West Virginia</option>
                                    <option value="WI">Wisconsin</option>
                                    <option value="WY">Wyoming</option>
                                </select>
                            </div>
                            <!-- Input country -->
                            <div class="form-group">
                                <label>Country</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="${S.COUNTRY_PARAM}" type="text" placeholder="USA"
                                    value="${countryInput}" />
                            </div>
                            <!-- Input zip code -->
                            <div class="form-group">
                                <label>Zip Code</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="${S.ZIP_PARAM}" type="text" placeholder="12345"
                                    onkeyup="checkZip(this)" value="${zipInput}" />
                                <br>
                                <!-- Zip code error -->
                                <span id="zipError" class="errormessage"></span>
                            </div>
                            <div class="text-center">
                                <input type="submit" class="btn btn-outline-info" value="Upload">
                            </div>
                        </form>
                    </div>
                    <div class="col-lg-3"></div>
                </div>
            </div>
        </div>
        <!-- Footer -->
        <div class="footer">
            <jsp:include page="./${S.FOOTER_PAGE}" />
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
        crossorigin="anonymous"></script>

    <script src="./JS/functions.js"></script>
    <script src="./JS/validation.js"></script>
    <script>
        loadSelectedOption("default", "state", "${stateInput}");
    </script>
</body>

</html>