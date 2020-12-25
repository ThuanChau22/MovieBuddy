<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="moviebuddy.util.Passwords" %>
<%@ page import="moviebuddy.util.S" %>
<jsp:include page="/TheatreGet" />
<jsp:include page="/ScheduleGet" />
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
        response.sendRedirect(S.HOME_PAGE);
    }

    request.setAttribute("selectTheatreId", session.getAttribute(S.SELECTED_THEATRE_ID));
    request.setAttribute("movieId", session.getAttribute(S.SCHEDULE_MOVIE_ID));

    request.setAttribute("theatreName", session.getAttribute(S.SELECTED_THEATRE_NAME));
    request.setAttribute("movie", session.getAttribute(S.SCHEDULE_MOVIE_INFO));
    request.setAttribute("theatreList", session.getAttribute(S.THEATRE_LIST));
    request.setAttribute("roomList", session.getAttribute(S.ROOM_LIST));
    request.setAttribute("scheduleList", session.getAttribute(S.SCHEDULE_LIST));
    request.setAttribute("showDateInput", session.getAttribute(S.SCHEDULE_SHOW_DATE_CREATE));
    request.setAttribute("startTimeInput", session.getAttribute(S.SCHEDULE_START_TIME_CREATE));
    request.setAttribute("roomNumberInput", session.getAttribute(S.SCHEDULE_ROOM_NUMBER_CREATE));
    request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
    session.removeAttribute(S.SELECTED_THEATRE_NAME);
    session.removeAttribute(S.SCHEDULE_MOVIE_INFO);
    session.removeAttribute(S.THEATRE_LIST);
    session.removeAttribute(S.ROOM_LIST);
    session.removeAttribute(S.SCHEDULE_LIST);
    session.removeAttribute(S.SCHEDULE_SHOW_DATE_CREATE);
    session.removeAttribute(S.SCHEDULE_START_TIME_CREATE);
    session.removeAttribute(S.SCHEDULE_ROOM_NUMBER_CREATE);
    session.removeAttribute(S.ERROR_MESSAGE);
%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
        integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/style.css">
    <link rel="icon" href="./images/MovieBuddy.ico">
    <title>Movie Buddy | Manage Schedule</title>
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
                <a class="inputAsLink" href="./${S.MANAGE_MOVIE_PAGE}">&#8249;
                    <span>Back</span>
                </a>
                <!-- Current movie information -->
                <div class="row">
                    <div class="col-sm-2">
                        <h4 style="margin-top:20px">#${movie.getId()}</h4>
                    </div>
                    <div class="col-sm">
                        <h1 class="display-5 text-center">${movie.getTitle()}</h1>
                    </div>
                    <div class="col-sm-2"></div>
                </div>
                <hr>
                <!-- List of theatre options -->
                <c:if test="${isAdmin}">
                    <form id="selectTheatreForm" action="SelectTheatre" method="POST">
                        <div class="form-group">
                            <label>Theatre: </label>
                            <select id="selectTheatreOption" name="selectTheatreOption" form="selectTheatreForm"
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
                <div class="errormessagePadding">
                    <div class="errormessageWrapper">
                        <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xl-2"></div>
                    <div class="col-xl">
                        <table>
                            <tr>
                                <th>Schedule</th>
                                <th>Show Date</th>
                                <th>Show Time</th>
                                <th>Room</th>
                                <th>Actions</th>
                            </tr>
                            <tr>
                                <td>#</td>
                                <td>
                                    <!-- Input show date -->
                                    <input form="addScheduleForm" style="width: 150px;" name="showDate" type="date"
                                        value="${showDateInput}" />
                                </td>
                                <td>
                                    <!-- Input start time -->
                                    <input form="addScheduleForm" style="width: 80px;" name="startTime" type="time"
                                        value="${startTimeInput}" />
                                </td>
                                <td>
                                    <!-- List of room options -->
                                    <select id="roomNumber" name="roomNumber" form="addScheduleForm">
                                        <option id="defaultRoom" hidden value="">Select a room</option>
                                        <c:forEach items="${roomList}" var="room">
                                            <option value="${room.getRoomNumber()}">Room ${room.getRoomNumber()}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <!-- Add schedule form -->
                                    <form id="addScheduleForm" action="ScheduleAdd" method="POST" class="button"
                                        onsubmit="return validateScheduleForm(this)">
                                        <input type="hidden" name="movieId" value="${movieId}" />
                                        <input type="submit" class="btn btn-outline-info" value="Add" />
                                    </form>
                                </td>
                            </tr>
                            <!-- List of schedules -->
                            <c:forEach items="${scheduleList}" var="schedule">
                                <tr>
                                    <!-- Schedule id -->
                                    <td>${schedule.getScheduleId()}</td>
                                    <!-- Show date -->
                                    <td>${schedule.displayShowDate()}</td>
                                    <!-- Show time -->
                                    <td>${schedule.getStartTime()}-${schedule.getEndTime()}</td>
                                    <!-- Room number -->
                                    <td>Room ${schedule.getRoomNumber()}</td>
                                    <td>
                                        <!-- Delete schedule -->
                                        <form action="ScheduleDelete" method="POST" class="button">
                                            <input type="hidden" name="scheduleId" value="${schedule.getScheduleId()}" />
                                            <input type="submit" class="btn btn-outline-info" value="Delete" />
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                    <div class="col-xl-2"></div>
                </div>
            </div>
        </div>
        <!-- Footer -->
        <div class="footer">
            <jsp:include page="./${S.FOOTER_PAGE}" />
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
        crossorigin="anonymous"></script>

    <script src="./JS/functions.js"></script>
    <script src="./JS/validation.js"></script>
    <c:if test="${isAdmin}">
        <!-- Load selected theatre -->
        <script>
            loadSelectedOption("defaultLocation", "selectTheatreOption", "${selectTheatreId}");
        </script>
    </c:if>
    <!-- Load previous room number input -->
    <script>
        loadSelectedOption("defaultRoom", "roomNumber", "${roomNumberInput}");
    </script>
</body>

</html>