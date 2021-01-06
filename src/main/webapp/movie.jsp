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

    // ${errorMessage}
    // ${movieList}
%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
        integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/style.css">
    <link rel="icon" href="./images/MovieBuddy.ico">
    <title>Movie Buddy | Manage Movie</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./${S.NAV_BAR_PAGE}" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h3>Movie</h3>
                <hr>
                <!-- Upload movie information -->
                <c:if test="${isAdmin}">
                    <div class="row">
                        <div class="col"></div>
                        <div class="col-6 text-center">
                            <a href="./${S.MOVIE_CREATE}">
                                <button type="button" class="btn btn-outline-info">Add Movie</button>
                            </a>
                        </div>
                        <div class="col"></div>
                    </div>
                    <hr>
                </c:if>
                <!-- Error message -->
                <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                <c:forEach items="${movieList}" var="movie">
                    <div class="card">
                        <div class="card-body">
                            <div class="row">
                                <!-- Movie title -->
                                <div class="col-lg">
                                    <h5>#${movie.getId()}</h5>
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
                                        <p><b>Release Date:</b> ${movie.displayReleaseDate()}</p>
                                    </ul>
                                    <hr>
                                    <!-- Movie trailer -->
                                    <h3>Trailer</h3>
                                    <div class="embed-responsive embed-responsive-16by9">
                                        <iframe width="907" height="510" src="${movie.getTrailer()}" frameborder="0"
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
                                        <!-- Schedule movie -->
                                        <form action="${S.SCHEDULE}" method="GET" class="button">
                                            <input type="hidden" name="${S.MOVIE_ID_PARAM}" value=${movie.getId()} />
                                            <input type="submit" class="btn btn-outline-info" value="Schedule" />
                                        </form>
                                        <c:if test="${isAdmin}">
                                            <!-- Edit movie information -->
                                            <form action="${S.MOVIE_EDIT}" method="GET" class="button">
                                                <input type="hidden" name="${S.MOVIE_ID_PARAM}"
                                                    value=${movie.getId()} />
                                                <input type="submit" class="btn btn-outline-info" value="Edit" />
                                            </form>
                                            <!-- Delete movie information -->
                                            <form action="${S.MOVIE_DELETE}" method="POST" class="button">
                                                <input type="hidden" name="${S.MOVIE_ID_PARAM}"
                                                    value=${movie.getId()} />
                                                <input type="submit" class="btn btn-outline-danger" value="Delete" />
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <br>
                </c:forEach>
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