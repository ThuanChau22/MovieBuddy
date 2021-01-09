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
    // ${scheduleListEmpty}
    // ${scheduleList}
%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
        integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/style.css">
    <link rel="icon" href="./images/MovieBuddy.ico">
    <title>Movie Buddy | Home</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./${S.NAV_BAR_PAGE}" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h1 class="display-3 text-center">Movie Buddy</h1>
                <!-- Search bar -->
                <div id="searchBar">
                    <br>
                    <form class="form-inline justify-content-center">
                        <div class="form-group mx-sm-3 mb-2">
                            <input type="search" class="form-control" id="searchInput" placeholder="Search">
                        </div>
                        <button type="submit" class="btn btn-outline-info mb-2">Search</button>
                    </form>
                </div>
                <hr>
                <!-- JCarousel for dates-->
                <div class="jcarousel-wrapper" style="width: 80%;">
                    <div class="jcarousel">
                        <ul class="jcarousel-list">
                            <c:set var="i" value="${0}" />
                            <c:forEach items="${dateList}" var="date">
                                <c:choose>
                                    <c:when test="${i == 0}">
                                        <li id="dateItem-${i}" class="jcarousel-item">
                                            <a href="?date=${date}" class="date-picker-link">
                                                <span class="dayOfWeek"><i>Today</i></span>
                                                <span class="month-date">${S.date("MMM dd", date)}</span>
                                            </a>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li id="dateItem-${i}" class="jcarousel-item">
                                            <a href="?date=${date}" class="date-picker-link">
                                                <span class="dayOfWeek"><i>${S.date("EEEE", date)}</i></span>
                                                <span class="month-date">${S.date("MMM dd", date)}</span>
                                            </a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                                <c:set var="i" value="${i+1}" />
                            </c:forEach>
                        </ul>
                    </div>
                    <a href="#" class="jcarousel-control prev">&lsaquo;</a>
                    <a href="#" class="jcarousel-control next">&rsaquo;</a>
                </div>
                <!-- Show times -->
                <h3 class="display-5">Showtimes</h3>
                <hr>
                <div class="container">
                    <c:choose>
                        <c:when test="${!scheduleListEmpty}">
                            <c:forEach items="${scheduleList}" var="movie">
                                <div class="card">
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
                                                    <img src=${movie.getPoster()} class="rounded mx-auto w-100"
                                                        alt="poster">
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
                                                    <iframe width="907" height="510" src="${movie.getTrailer()}"
                                                        frameborder="0"
                                                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                                        allowfullscreen></iframe>
                                                </div>
                                                <hr>
                                                <!-- Movie description -->
                                                <h3>Description</h3>
                                                <p>${movie.getDescription()}</p>
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col">
                                                <div class="container">
                                                    <!-- Start times -->
                                                    <c:forEach items="${movie.getSchedules()}" var="schedule">
                                                        <a href="#">
                                                            <button type="button"
                                                                class="btn btn-outline-info">${schedule.getStartTime()}</button>
                                                        </a>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="element-center" style="height: 40%;">
                                <div class="text-center">
                                    <h5>No showtimes</h5>
                                    <span>Please pick a differrent date or come back later!</span>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="footer">
            <jsp:include page="./${S.FOOTER_PAGE}" />
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
        crossorigin="anonymous"></script>

    <script src="./JS/dist/jquery.jcarousel.min.js"></script>
    <script src="./JS/dist/jquery.jcarousel-swipe.min.js"></script>
    <script src="./JS/functions.js"></script>
    <script src="./JS/validation.js"></script>
    <script>
        loadDates("${selectedDateIndex}");
    </script>
</body>

</html>