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
    // ${theatreListEmpty}
    // ${theatreList}
    // ${adminList}
    // ${staffList}
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
                <!-- Current theatre name -->
                <h3>Manage Staff</h3>
                <hr>
                <!-- Create staff account -->
                <div class="row">
                    <div class="col"></div>
                    <div class="col-6 text-center">
                        <a href="./${S.STAFF_CREATE}">
                            <button type="button" class="btn btn-outline-info">Add Staff Account</button>
                        </a>
                    </div>
                    <div class="col"></div>
                </div>
                <hr>
                <!-- List of theatre options -->
                <c:if test="${isAdmin}">
                    <form id="selectTheatreForm" action="${S.THEATRE_SELECT}" method="POST" class="form-inline">
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text">Theatre</span>
                            </div>
                            <select id="theatreOption" name="${S.THEATRE_OPTION_PARAM}" class="custom-select"
                                onchange="submitForm('#selectTheatreForm')">
                                <option id="defaultLocation" hidden value="">Select a theatre</option>
                                <c:choose>
                                    <c:when test="${!theatreListEmpty}">
                                        <c:forEach items="${theatreList}" var="theatre">
                                            <option value="${theatre.getId()}">${theatre.getTheatreName()}</option>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <option disabled value="">empty</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </form>
                    <hr>
                </c:if>
                <!-- Error message -->
                <p id="errorMessage" class="text-center errormessage">${errorMessage}</p>
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
                            <tr class="anchor" id="${admin.getStaffId()}">
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
                        <tr class="anchor" id="${staff.getStaffId()}">
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
                                            <form action="${S.STAFF_DELETE}" method="POST" class="form-button">
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
            <jsp:include page="./components/footer.jsp" />
        </div>
    </div>

    <!-- Script import -->
    <jsp:include page="./components/script.jsp" />

    <c:if test="${isAdmin}">
        <!-- Load selected theatre -->
        <script>
            loadSelectedOption("#defaultLocation", "#theatreOption", "${theatreId}");
        </script>
    </c:if>
</body>

</html>