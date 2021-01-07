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

    // ${theatreId}
    // ${theatreName}
    // ${errorMessage}
    // ${roomListEmpty}
    // ${roomList}
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
                <!-- Current theatre name -->
                <h3>Theatre: ${theatreName}</h3>
                <hr>
                <a class="inputAsLink" href="./${S.THEATRE}#${theatreId}">&lsaquo;<span>Back</span></a>
                <!-- Upload room information -->
                <div class="row">
                    <div class="col"></div>
                    <div class="col-6 text-center">
                        <form action="${S.ROOM_CREATE}" method="GET" class="button">
                            <input type="hidden" name="${S.THEATRE_ID_PARAM}" value="${theatreId}" />
                            <input type="submit" class="btn btn-outline-info" value="Add Room" />
                        </form>
                    </div>
                    <div class="col"></div>
                </div>
                <hr>
                <!-- Error message -->
                <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                <div class="row">
                    <div class="col-lg-1"></div>
                    <div class="col-lg">
                        <c:choose>
                            <c:when test="${!roomListEmpty}">
                                <table>
                                    <tr>
                                        <th>Room Number</th>
                                        <th>Number of Sections</th>
                                        <th>Seats per Section</th>
                                        <th>Actions</th>
                                    </tr>
                                    <c:forEach items="${roomList}" var="room">
                                        <tr class="anchor" id="${room.getRoomNumber()}">
                                            <!-- Room number -->
                                            <td>${room.getRoomNumber()}</td>
                                            <!-- Number of sections -->
                                            <td>${room.getNumberOfRows()}</td>
                                            <!-- Number of seats per section -->
                                            <td>${room.getSeatsPerRow()}</td>
                                            <td>
                                                <div class="container">
                                                    <!-- Edit room information -->
                                                    <form action="${S.ROOM_EDIT}" method="GET" class="button">
                                                        <input type="hidden" name="${S.THEATRE_ID_PARAM}"
                                                            value="${theatreId}" />
                                                        <input type="hidden" name="${S.ROOM_NUMBER_PARAM}"
                                                            value="${room.getRoomNumber()}" />
                                                        <input type="submit" class="btn btn-outline-info"
                                                            value="Edit" />
                                                    </form>
                                                    <!-- Delete room information -->
                                                    <form action="${S.ROOM_DELETE}" method="POST" class="button">
                                                        <input type="hidden" name="${S.THEATRE_ID_PARAM}"
                                                            value="${theatreId}" />
                                                        <input type="hidden" name="${S.ROOM_NUMBER_PARAM}"
                                                            value="${room.getRoomNumber()}" />
                                                        <input type="submit" class="btn btn-outline-danger"
                                                            value="Delete" />
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center">
                                    <h5>No rooms</h5>
                                    <span>Rooms created will appear here</span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col-lg-1"></div>
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
</body>

</html>