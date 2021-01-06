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

    // Check authentication as admin and manager
    Object accountId = session.getAttribute(S.ACCOUNT_ID);
    Object currentSession = session.getAttribute(S.CURRENT_SESSION);
    Object staffId = session.getAttribute(S.STAFF_ID);
    Object role = session.getAttribute(S.ROLE);
    if(accountId == null || !currentSession.equals(Passwords.applySHA256(session.getId() + request.getRemoteAddr())) || staffId == null || !(role.equals(S.ADMIN) || role.equals(S.MANAGER))){
        response.sendRedirect(S.HOME);
    }

    // ${theatreList}
    // ${roleList}
    // ${roleInput}
    // ${locationInput}
    // ${userNameInput}
    // ${emailInput}
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
    <title>Movie Buddy | Manage Staff</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./${S.NAV_BAR_PAGE}" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h1 class="display-3 text-center">Create Staff Account</h1>
                <hr>
                <a class="inputAsLink" href="./${S.STAFF}">&lsaquo;<span>Back</span>
                </a>
                <div class="row">
                    <div class="col-lg"></div>
                    <div class="col-lg-5">
                        <div class="card">
                            <div class="card-body">
                                <!-- Create staff account form -->
                                <form id="staffCreateForm" action="${S.STAFF_CREATE}" method="POST"
                                    onsubmit="return validateStaffSignUp(this, '${isAdmin}')">
                                    <c:if test="${isAdmin}">
                                        <!-- Input role -->
                                        <div class="form-group">
                                            <label>Role</label><br>
                                            <select id="role" class="inputbox" name="${S.ROLE_PARAM}"
                                                form="staffCreateForm" onchange="checkRole(this)">
                                                <option id="defaultRole" hidden value="">Select a role</option>
                                                <c:forEach items="${roleList}" var="role">
                                                    <option value="${role.getTitle()}">${role.getTitle()}</option>
                                                </c:forEach>
                                            </select>
                                            <!-- Role error -->
                                            <span id="roleError" class="errormessage"></span>
                                        </div>
                                        <!-- Input location -->
                                        <div class="form-group" id="locationForm">
                                            <label>Theatre Location</label><br>
                                            <select id="theatreLocation" class="inputbox"
                                                name="${S.THEATRE_LOCATION_PARAM}" form="staffCreateForm"
                                                onchange="checkLocation(this)">
                                                <option id="defaultLocation" hidden value="">Select a theatre location
                                                </option>
                                                <c:forEach items="${theatreList}" var="theatre">
                                                    <option value="${theatre.getId()}">${theatre.getTheatreName()}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <!-- Location error -->
                                            <span id="locationError" class="errormessage"></span>
                                        </div>
                                    </c:if>
                                    <!-- Input name -->
                                    <div class="form-group">
                                        <label>Name</label><br>
                                        <input class="inputbox" type="text" name="${S.USERNAME_PARAM}"
                                            placeholder="Enter your name" onkeyup="checkName(this)"
                                            value="${userNameInput}">
                                        <br>
                                        <!-- Name error -->
                                        <span id="userNameError" class="errormessage"></span>
                                    </div>
                                    <!-- Input email -->
                                    <div class="form-group">
                                        <label>Email</label><br>
                                        <input name="${S.EMAIL_PARAM}" class="inputbox" type="text"
                                            placeholder="Enter email" onkeyup="checkEmail(this)" value="${emailInput}">
                                        <br>
                                        <!-- Email error -->
                                        <span id="emailError" class="errormessage"></span>
                                    </div>
                                    <!-- Input password -->
                                    <div class="form-group">
                                        <label>Password</label><br>
                                        <input name="${S.PASSWORD_PARAM}" class="inputbox" type="password"
                                            placeholder="Enter password" onkeyup="checkPassword(this)">
                                        <br>
                                        <!-- Password error -->
                                        <span id="passwordError" class="errormessage"></span>
                                    </div>
                                    <div class="text-center">
                                        <input type="submit" class="btn btn-primary" value="Create Account">
                                    </div>
                                </form>
                                <!-- Error message -->
                                <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg"></div>
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
    <c:if test="${isAdmin}">
        <!-- Load previous inputs -->
        <script>
            loadSelectedOption("defaultRole", "role", "${roleInput}");
            loadSelectedOption("defaultLocation", "theatreLocation", "${locationInput}");
            if ("${roleInput}" != "") {
                checkRole(document.getElementById("role"));
            }
        </script>
    </c:if>
</body>

</html>