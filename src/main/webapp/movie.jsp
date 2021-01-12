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
    // ${movieListEmpty}
    // ${movieList}
%>
<html lang="en">

<head>
    <!-- Header -->
    <jsp:include page="./components/header.jsp" />
    <title>Movie Buddy | Manage Movie</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./components/navbar.jsp" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <h3>Manage Movie</h3>
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
                <div class="container">
                    <!-- Error message -->
                    <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                    <c:choose>
                        <c:when test="${!movieListEmpty}">
                            <c:forEach items="${movieList}" var="movie">
                                <!-- Movie id for anchor -->
                                <div class="card" id="${movie.getId()}">
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
                                                    <!-- Schedule movie -->
                                                    <form action="${S.SCHEDULE}" method="GET" class="button">
                                                        <input type="hidden" name="${S.MOVIE_ID_PARAM}"
                                                            value=${movie.getId()} />
                                                        <input type="submit" class="btn btn-outline-info"
                                                            value="Schedule" />
                                                    </form>
                                                    <c:if test="${isAdmin}">
                                                        <!-- Edit movie information -->
                                                        <form action="${S.MOVIE_EDIT}" method="GET" class="button">
                                                            <input type="hidden" name="${S.MOVIE_ID_PARAM}"
                                                                value=${movie.getId()} />
                                                            <input type="submit" class="btn btn-outline-info"
                                                                value="Edit" />
                                                        </form>
                                                        <!-- Delete movie information -->
                                                        <form action="${S.MOVIE_DELETE}" method="POST" class="button">
                                                            <input type="hidden" name="${S.MOVIE_ID_PARAM}"
                                                                value=${movie.getId()} />
                                                            <input type="submit" class="btn btn-outline-danger"
                                                                value="Delete" />
                                                        </form>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="element-center" style="height: 50%;">
                                <div class="text-center">
                                    <h5>No movies</h5>
                                    <span>Movies created will appear here</span>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
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
</body>

</html>