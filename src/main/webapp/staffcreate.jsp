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

    // ${theatreListEmpty}
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
    <!-- Header -->
    <jsp:include page="./components/header.jsp" />
    <title>Movie Buddy | Manage Staff</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./components/navbar.jsp" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h3>Manage Staff</h3>
                <hr>
                <a class="inputAsLink" href="./${S.STAFF}">&lsaquo;<span>Back</span></a>
                <h1 class="display-4 text-center">Create Staff Account</h1>
                <hr>
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
                                            <select id="role" class="form-control" name="${S.ROLE_PARAM}"
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
                                            <select id="theatreLocation" class="form-control"
                                                name="${S.THEATRE_LOCATION_PARAM}" form="staffCreateForm"
                                                onchange="checkLocation(this)">
                                                <option id="defaultLocation" hidden value="">Select a theatre location
                                                </option>
                                                <c:choose>
                                                    <c:when test="${!theatreListEmpty}">
                                                        <c:forEach items="${theatreList}" var="theatre">
                                                            <option value="${theatre.getId()}">
                                                                ${theatre.getTheatreName()}
                                                            </option>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option disabled value="">empty</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                            <!-- Location error -->
                                            <span id="locationError" class="errormessage"></span>
                                        </div>
                                    </c:if>
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
                                        <input name="${S.EMAIL_PARAM}" class="form-control" type="text"
                                            placeholder="Enter email" onkeyup="checkEmail(this)" value="${emailInput}">
                                        <!-- Email error -->
                                        <span id="emailError" class="errormessage"></span>
                                    </div>
                                    <!-- Input password -->
                                    <div class="form-group">
                                        <label>Password</label><br>
                                        <input name="${S.PASSWORD_PARAM}" class="form-control" type="password"
                                            placeholder="Enter password" onkeyup="checkPassword(this)">
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
            <jsp:include page="./components/footer.jsp" />
        </div>
    </div>

    <!-- Script import -->
    <jsp:include page="./components/script.jsp" />

    <c:if test="${isAdmin}">
        <!-- Load previous inputs -->
        <script>
            loadSelectedOption("#defaultRole", "#role", "${roleInput}");
            loadSelectedOption("#defaultLocation", "#theatreLocation", "${locationInput}");
            if ("${roleInput}" != "") {
                checkRole(document.getElementById("role"));
            }
        </script>
    </c:if>
</body>

</html>