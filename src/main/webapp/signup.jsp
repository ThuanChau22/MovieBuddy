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

    // Check authentication
    Object accountId = session.getAttribute(S.ACCOUNT_ID);
    Object currentSession = session.getAttribute(S.CURRENT_SESSION);
    if(accountId != null && currentSession.equals(Passwords.applySHA256(session.getId() + request.getRemoteAddr()))) {
        response.sendRedirect(S.HOME);
    }

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
    <title>Movie Buddy | Sign Up</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./${S.NAV_BAR_PAGE}" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h1 class="display-3 text-center">Sign Up</h1>
                <hr>
                <br>
                <div class="row">
                    <div class="col-lg"></div>
                    <div class="col-lg-5">
                        <div class="card">
                            <div class="card-body">
                                <!-- Sign up form -->
                                <form id="signUpForm" action="${S.SIGN_UP}" method="POST"
                                    onsubmit="return validateSignUp(this)">
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
                                        <input class="inputbox" type="text" name="${S.EMAIL_PARAM}"
                                            placeholder="Enter email" onkeyup="checkEmail(this)" value="${emailInput}">
                                        <br>
                                        <!-- Email error -->
                                        <span id="emailError" class="errormessage"></span>
                                    </div>
                                    <!-- Input password -->
                                    <div class="form-group">
                                        <label>Password</label><br>
                                        <input class="inputbox" type="password" name="${S.PASSWORD_PARAM}"
                                            placeholder="Enter password" onkeyup="checkPassword(this)">
                                        <br>
                                        <!-- Password error -->
                                        <span id="passwordError" class="errormessage"></span>
                                    </div>
                                    <!-- Input Re-password -->
                                    <div class="form-group">
                                        <label>Confirm Password</label><br>
                                        <input class="inputbox" type="password" name="${S.RE_PASSWORD_PARAM}"
                                            placeholder="Re-enter password" onkeyup="checkRePassword('signUpForm')">
                                        <br>
                                        <!-- Re-password error -->
                                        <span id="rePasswordError" class="errormessage"></span>
                                    </div>
                                    <div class="text-center">
                                        <input type="submit" class="btn btn-primary" value="Sign Up">
                                    </div>
                                </form>
                                <!-- Error message -->
                                <p class="text-center errormessage">${errorMessage}</p>
                                <a href="./${S.SIGN_IN}">Already have an account? Sign in here</a>
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

    <script src="./js/functions.js"></script>
    <script src="./js/validation.js"></script>
</body>

</html>