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
        response.sendRedirect(S.HOME_PAGE);
    }

    request.setAttribute("movieId", session.getAttribute(S.MOVIE_EDIT_ID));
    request.setAttribute("titleInput", session.getAttribute(S.MOVIE_EDIT_TITLE));
    request.setAttribute("releaseDateInput", session.getAttribute(S.MOVIE_EDIT_RELEASE_DATE));
    request.setAttribute("durationInput", session.getAttribute(S.MOVIE_EDIT_DURATION));
    request.setAttribute("trailerInput", session.getAttribute(S.MOVIE_EDIT_TRAILER));
    request.setAttribute("descriptionInput", session.getAttribute(S.MOVIE_EDIT_DESCRIPTION));

    request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
    session.removeAttribute(S.ERROR_MESSAGE);
%>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
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
                <h1 class="display-3 text-center">Update Movie Information</h1>
                <hr>
                <a class="inputAsLink" href="./${S.MANAGE_MOVIE_PAGE}">&#8249;
                    <span>Back</span>
                </a>
                <div class="row">
                    <div class="col-lg-3"></div>
                    <div class="col-lg">
                        <!-- Error message -->
                        <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                        <!-- Edit movie information form -->
                        <form id="editMovieForm" action="MovieEdit" method="POST" enctype="multipart/form-data"
                            onsubmit="return validateMovieForm(this)">
                            <!-- Save hook -->
                            <div class="form-group">
                                <input type="hidden" name="action" value="save" />
                            </div>
                            <!-- Movie id -->
                            <div class="form-group">
                                <input type="hidden" name="movieId" value="${movieId}" />
                            </div>
                            <!-- Input title -->
                            <div class="form-group">
                                <label>Title</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="title" type="text" placeholder="Enter title"
                                    value="${titleInput}" />
                            </div>
                            <!-- Input release date -->
                            <div class="form-group">
                                <label>Release Date</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="releaseDate" type="date" value="${releaseDateInput}" />
                            </div>
                            <!-- Input duration -->
                            <div class="form-group">
                                <label>Duration</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="duration" type="text" placeholder="Enter duration in minutes"
                                    value="${durationInput}" />
                            </div>
                            <!-- Input trailer -->
                            <div class="form-group">
                                <label>Trailer Source</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="trailer" type="text" placeholder="Enter trailer source..."
                                    value="${trailerInput}" />
                            </div>
                            <!-- Input poster -->
                            <div class="form-group">
                                <label>Poster</label><br>
                                <input class="inputbox" name="poster" type="file" />
                            </div>
                            <!-- Input description -->
                            <div class="form-group">
                                <label>Description</label><span class="errormessage">*</span><br>
                                <textarea class="inputbox" name="description" cols="60" rows="5" maxlength="1000" style="resize: none;"
                                    placeholder="Enter movie description...">${descriptionInput}</textarea>
                            </div>
                        </form>
                        <!-- Cancel form -->
                        <form id="cancelMovieForm" action="MovieEdit" method="POST">
                            <!-- Cancel hook -->
                            <div class="form-group">
                                <input type="hidden" name="action" value="cancel" />
                            </div>
                        </form>
                        <div class="text-center">
                            <div class="button">
                                <input form="editMovieForm" type="submit" class="btn btn-outline-info" value="Save">
                            </div>
                            <div class="button">
                                <input form="cancelMovieForm" type="submit" class="btn btn-outline-info" value="Cancel" />
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3"></div>
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
</body>

</html>