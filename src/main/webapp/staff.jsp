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

    // ${theatreId}
    // ${theatreName}
    // ${theatreList}
    // ${adminList}
    // ${staffList}
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
                <!-- Current theatre name -->
                <h3>Theatre: ${theatreName}</h3>
                <hr>
                <!-- Create staff account -->
                <div class="row">
                    <div class="col"></div>
                    <div class="col-6 text-center">
                        <a href="./${S.STAFF_CREATE}">
                            <button type="button" class="btn btn-outline-info">Create Staff Account</button>
                        </a>
                    </div>
                    <div class="col"></div>
                </div>
                <hr>
                <!-- List of theatre options -->
                <c:if test="${isAdmin}">
                    <form id="selectTheatreForm" action="${S.THEATRE_SELECT}" method="POST">
                        <div class="form-group">
                            <label>Theatre: </label>
                            <select id="theatreOption" name="${S.THEATRE_OPTION_PARAM}" form="selectTheatreForm"
                                onchange="submitForm('selectTheatreForm')">
                                <option id="defaultLocation" hidden value="">Select a theatre location</option>
                                <c:forEach items="${theatreList}" var="theatre">
                                    <option value="${theatre.getId()}">${theatre.getTheatreName()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                    <hr>
                </c:if>
                <!-- Error message -->
                <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                <table>
                    <tr>
                        <th>Staff Id</th>
                        <th>Name</th>
                        <th>Role</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                    <!-- List of admins -->
                    <c:if test="${isAdmin}">
                        <c:forEach items="${adminList}" var="admin">
                            <tr>
                                <td>${admin.getStaffId()}</td>
                                <td>${admin.getUserName()}</td>
                                <td>${admin.getRole()}</td>
                                <td>${admin.getEmail()}</td>
                                <td>N/A</td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <!-- List of staffs -->
                    <c:forEach items="${staffList}" var="staff">
                        <tr>
                            <!-- Staff id -->
                            <td>${staff.getStaffId()}</td>
                            <!-- User name -->
                            <td>${staff.getUserName()}</td>
                            <!-- Role -->
                            <td>${staff.getRole()}</td>
                            <!-- Email -->
                            <td>${staff.getEmail()}</td>
                            <c:choose>
                                <c:when test="${staff.getRole().equals(S.MANAGER) && !isAdmin}">
                                    <td>N/A</td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <div class="container">
                                            <!-- Delete staff account -->
                                            <form action="${S.STAFF_DELETE}" method="POST" class="button">
                                                <input type="hidden" name="${S.STAFF_ID_PARAM}"
                                                    value="${staff.getStaffId()}" />
                                                <input type="submit" class="btn btn-outline-danger" value="Delete" />
                                            </form>
                                        </div>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>
                </table>
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
        <!-- Load selected theatre -->
        <script>
            loadSelectedOption("defaultLocation", "theatreOption", "${theatreId}");
        </script>
    </c:if>
</body>

</html>