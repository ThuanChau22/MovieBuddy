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

    // ${errorMessage}
    // ${theatreListEmpty}
    // ${theatreList}
%>
<html lang="en">

<head>
    <!-- Header -->
    <jsp:include page="./components/header.jsp" />
    <title>Movie Buddy | Manage Theatre</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./components/navbar.jsp" />
    <div class="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h3>Manage Theatre</h3>
                <hr>
                <!-- Upload theatre information -->
                <div class="text-center">
                    <a href="./${S.THEATRE_CREATE}">
                        <button type="button" class="btn btn-outline-info">Add Theatre</button>
                    </a>
                </div>
                <hr>
                <!-- Error message -->
                <p id="errorMessage" class="text-center errormessage">${errorMessage}</p>
                <c:choose>
                    <c:when test="${!theatreListEmpty}">
                        <table>
                            <tr>
                                <th>Theatre Name</th>
                                <th>Address</th>
                                <th>City</th>
                                <th>State</th>
                                <th>Country</th>
                                <th>Zip</th>
                                <th>Actions</th>
                            </tr>
                            <!-- List of theatres -->
                            <c:forEach items="${theatreList}" var="theatre">
                                <tr class="anchor" id="${theatre.getId()}">
                                    <td>${theatre.getTheatreName()}</td>
                                    <td>${theatre.getAddress()}</td>
                                    <td>${theatre.getCity()}</td>
                                    <td>${theatre.getState()}</td>
                                    <td>${theatre.getCountry()}</td>
                                    <td>${theatre.getZip()}</td>
                                    <td>
                                        <div class="container">
                                            <!-- Manange ticket price -->
                                            <form action="${S.TICKET_PRICE}" method="GET" class="form-button">
                                                <input type="hidden" name="${S.THEATRE_ID_PARAM}"
                                                    value="${theatre.getId()}" />
                                                <input type="submit" class="btn btn-outline-info"
                                                    value="Ticket Price" />
                                            </form>
                                            <!-- Manage room -->
                                            <form action="${S.ROOM}" method="GET" class="form-button">
                                                <input type="hidden" name="${S.THEATRE_ID_PARAM}"
                                                    value="${theatre.getId()}" />
                                                <input type="submit" class="btn btn-outline-info" value="Room" />
                                            </form>
                                            <!-- Edit theatre information -->
                                            <form action="${S.THEATRE_EDIT}" method="GET" class="form-button">
                                                <input type="hidden" name="${S.THEATRE_ID_PARAM}"
                                                    value="${theatre.getId()}" />
                                                <input type="submit" class="btn btn-outline-info" value="Edit" />
                                            </form>
                                            <!-- Delete theatre information -->
                                            <form action="${S.THEATRE_DELETE}" method="POST" class="form-button">
                                                <input type="hidden" name="${S.THEATRE_ID_PARAM}"
                                                    value="${theatre.getId()}" />
                                                <input type="submit" class="btn btn-outline-danger" value="Delete" />
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="element-center" style="height: 50%;">
                            <div class="text-center">
                                <h5>No theatres</h5>
                                <span>Theatres created will appear here</span>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
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