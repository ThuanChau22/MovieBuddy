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
    // ${movieId}
    // ${movieTitle}
    // ${roomListEmpty}
    // ${roomList}
    // ${scheduleList}
    // ${showDateInput}
    // ${startTimeInput}
    // ${roomNumberInput}
    // ${errorMessage}
%>
<html lang="en">

<head>
    <!-- Header -->
    <jsp:include page="./components/header.jsp" />
    <title>Movie Buddy | Manage Schedule</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./components/navbar.jsp" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <!-- Current theatre name -->
                <h3>Manage Schedule</h3>
                <hr>
                <a class="inputAsLink" href="./${S.MOVIE}#${movieId}">&lsaquo;<span>Back</span></a>
                <!-- Current movie information -->
                <div class="row">
                    <div class="col-sm-2">
                        <h4 style="margin-top:20px">#${movieId}</h4>
                    </div>
                    <div class="col-sm">
                        <h1 class="display-5 text-center">${movieTitle}</h1>
                    </div>
                    <div class="col-sm-2"></div>
                </div>
                <hr>
                <!-- List of theatre options -->
                <c:if test="${isAdmin}">
                    <form id="selectTheatreForm" action="${S.THEATRE_SELECT}" method="POST">
                        <div class="form-group">
                            <label>Theatre: </label>
                            <select id="theatreOption" name="${S.THEATRE_OPTION_PARAM}" form="selectTheatreForm"
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
                                <td class="inputCell">#</td>
                                <td class="inputCell">
                                    <!-- Input show date -->
                                    <input form="addScheduleForm" style="width: 150px;" name="${S.SHOW_DATE_PARAM}"
                                        type="date" value="${showDateInput}" />
                                </td>
                                <td class="inputCell">
                                    <!-- Input start time -->
                                    <input form="addScheduleForm" style="width: 80px;" name="${S.START_TIME_PARAM}"
                                        type="time" value="${startTimeInput}" />
                                </td>
                                <td class="inputCell">
                                    <!-- List of room options -->
                                    <select id="roomNumber" name="${S.ROOM_NUMBER_PARAM}" form="addScheduleForm">
                                        <option id="defaultRoom" hidden value="">Select a room</option>
                                        <c:choose>
                                            <c:when test="${!roomListEmpty}">
                                                <c:forEach items="${roomList}" var="room">
                                                    <option value="${room.getRoomNumber()}">Room ${room.getRoomNumber()}
                                                    </option>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <option disabled value="">empty</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </td>
                                <td class="inputCell">
                                    <!-- Add schedule form -->
                                    <form id="addScheduleForm" action="${S.SCHEDULE_CREATE}" method="POST"
                                        class="button" onsubmit="return validateScheduleForm(this)">
                                        <input type="hidden" name="${S.MOVIE_ID_PARAM}" value="${movieId}" />
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
                                    <td>${S.date("MM/dd/yyyy", schedule.getShowDate())}</td>
                                    <!-- Show time -->
                                    <td>${schedule.getStartTime()}-${schedule.getEndTime()}</td>
                                    <!-- Room number -->
                                    <td>Room ${schedule.getRoomNumber()}</td>
                                    <td>
                                        <!-- Delete schedule -->
                                        <form action="${S.SCHEDULE_DELETE}" method="POST" class="button">
                                            <input type="hidden" name="${S.MOVIE_ID_PARAM}" value="${movieId}" />
                                            <input type="hidden" name="${S.SCHEDULE_ID_PARAM}"
                                                value="${schedule.getScheduleId()}" />
                                            <input type="submit" class="btn btn-outline-danger" value="Delete" />
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

    <!-- Load previous room number input -->
    <script>
        loadSelectedOption("#defaultRoom", "#roomNumber", "${roomNumberInput}");
    </script>
</body>

</html>