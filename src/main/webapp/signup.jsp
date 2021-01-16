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
    <!-- Header -->
    <jsp:include page="./components/header.jsp" />
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./components/navbar.jsp" />
    <div class="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h1 class="display-3 text-center">Sign Up</h1>
                <hr>
                <br>
                <div class="row justify-content-center">
                    <div class="col-md-7 col-lg-5">
                        <div class="card">
                            <div class="card-body">
                                <!-- Sign up form -->
                                <form id="signUpForm" action="${S.SIGN_UP}" method="POST"
                                    onsubmit="return validateSignUp(this)">
                                    <!-- Input name -->
                                    <div class="form-group">
                                        <label>Name</label><br>
                                        <input class="form-control" type="text" name="${S.USERNAME_PARAM}"
                                            placeholder="Enter your name" onkeyup="checkName(this)"
                                            value="${userNameInput}">
                                        <!-- Name error -->
                                        <span id="userNameError" class="errormessage"></span>
                                    </div>
                                    <!-- Input email -->
                                    <div class="form-group">
                                        <label>Email</label><br>
                                        <div style="position: relative">
                                            <input class="form-control" name="${S.EMAIL_PARAM}" type="text"
                                                placeholder="Enter email" onkeyup="checkEmail(this)"
                                                value="${emailInput}" style="padding-right: 30px;">
                                            <div class="spinner-wrapper">
                                                <span id="emailSpinner"></span>
                                            </div>
                                        </div>
                                        <!-- Email error -->
                                        <span id="emailError" class="errormessage"></span>
                                    </div>
                                    <!-- Input password -->
                                    <div class="form-group">
                                        <label>Password</label><br>
                                        <input class="form-control" type="password" name="${S.PASSWORD_PARAM}"
                                            placeholder="Enter password" onkeyup="checkPassword(this)">
                                        <!-- Password error -->
                                        <span id="passwordError" class="errormessage"></span>
                                    </div>
                                    <!-- Input Re-password -->
                                    <div class="form-group">
                                        <label>Confirm Password</label><br>
                                        <input class="form-control" type="password" name="${S.RE_PASSWORD_PARAM}"
                                            placeholder="Re-enter password" onkeyup="checkRePassword('signUpForm')">
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
                </div>
            </div>
        </div>
        <!-- Footer -->
        <div class="footer">
            <jsp:include page="./components/footer.jsp" />
        </div>
    </div>

    <!-- Script import -->
    <jsp:include page="./components/script.jsp" />
</body>

</html>