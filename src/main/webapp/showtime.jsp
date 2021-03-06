<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="moviebuddy.util.Passwords" %>
<%@ page import="moviebuddy.util.S" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate" ); // HTTP 1.1
    response.setHeader("Pragma", "no-cache" ); // HTTP 1.0
    response.setHeader("Expires", "0" ); // Proxies

    // Initiate session
    session = request.getSession();
    Object sessionId = session.getAttribute(S.SESSION_ID);
    if (sessionId == null || !sessionId.equals(Passwords.applySHA256(session.getId()))) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute(S.SESSION_ID, Passwords.applySHA256(session.getId()));
    }

    // ${selectedDateIndex}
    // ${dateList}
    // ${movie}
    // ${scheduleListEmpty}
    // ${scheduleList}
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
                <jsp:include page="./components/theatrelocation.jsp" />
                <div class="card" id="${movie.getId()}">
                    <div class="card-body">
                        <div class="row">
                            <!-- Movie title -->
                            <div class="col-lg">
                                <h4>${movie.getTitle()}</h4>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <!-- Movie poster -->
                            <div class="col-lg-5">
                                <div class="text-center">
                                    <img src=${movie.getPoster()} class="rounded mx-auto w-100" alt="poster">
                                </div>
                            </div>
                            <div class="col-lg">
                                <ul class="list-inline">
                                    <!-- Movie length -->
                                    <p><b>Length:</b> ${movie.getDuration()} minutes</p>
                                    <!-- Movie release date -->
                                    <p><b>Release Date:</b> ${S.date("MMM dd yyyy",
                                        movie.getReleaseDate())}</p>
                                </ul>
                                <hr>
                                <!-- Movie trailer -->
                                <h3>Trailer</h3>
                                <div class="embed-responsive embed-responsive-16by9">
                                    <video width="907" height="510" controls>
                                        <source src="${movie.getTrailer()}" type="video/mp4">
                                        Video not available.
                                    </video>
                                </div>
                                <hr>
                                <!-- Movie description -->
                                <h3>Description</h3>
                                <p>${movie.getDescription()}</p>
                            </div>
                        </div>
                        <hr>
                        <!-- JCarousel for dates-->
                        <div class="jcarousel-wrapper">
                            <div class="jcarousel showtime">
                                <ul class="jcarousel-list">
                                    <c:set var="i" value="${0}" />
                                    <c:forEach items="${dateList}" var="date">
                                        <li id="dateItem-${i}" class="jcarousel-item">
                                            <a href="./${S.SHOWTIME}?${S.MOVIE_ID_PARAM}=${movie.getId()}&${S.DATE_PARAM}=${date}" class="date-picker-link">
                                                <c:choose>
                                                    <c:when test="${i == 0}">
                                                        <span class="dayOfWeek"><i>Today</i></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="dayOfWeek"><i>${S.date("E", date)}</i></span>
                                                    </c:otherwise>
                                                </c:choose>
                                                <span class="month-date">${S.date("MMM dd", date)}</span>
                                            </a>
                                        </li>
                                        <c:set var="i" value="${i+1}" />
                                    </c:forEach>
                                </ul>
                            </div>
                            <a href="#" class="jcarousel-control prev">&lsaquo;</a>
                            <a href="#" class="jcarousel-control next">&rsaquo;</a>
                        </div>
                        <div class="row">
                            <div class="col">
                                <div class="container" style="height: 3.5rem">
                                    <div id="showtimeSpinner"></div>
                                    <div id="showtime" style="overflow-x: auto;">
                                        <c:choose>
                                            <c:when test="${!scheduleListEmpty}">
                                                <!-- Start times -->
                                                <c:forEach items="${scheduleList}" var="schedule">
                                                    <a href="#" class="list-button">
                                                        <button type="button" class="btn btn-outline-info">${schedule.getStartTime()}</button>
                                                    </a>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="text-center">
                                                    <h5>No showtimes</h5>
                                                    <span>Please pick a differrent date!</span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <br>
            </div>
        </div>
        <!-- Footer -->
        <div class="footer">
            <jsp:include page="./components/footer.jsp" />
        </div>
    </div>

    <!-- Script import -->
    <jsp:include page="./components/script.jsp" />

    <script>
        loadDates("${selectedDateIndex}");
    </script>
</body>

</html>