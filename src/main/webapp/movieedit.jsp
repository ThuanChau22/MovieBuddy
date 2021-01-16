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

    // ${movieId}
    // ${titleInput}
    // ${releaseDateInput}
    // ${durationInput}
    // ${trailerInput}
    // ${descriptionInput}
    // ${errorMessage}
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
                <h3>Manage Movie</h3>
                <hr>
                <a class="custom-link" href="./${S.MOVIE}#${movieId}">&lsaquo;<span>Back</span></a>
                <h1 class="display-4 text-center">Edit Movie Information</h1>
                <hr>
                <div class="row">
                    <div class="col-lg-3"></div>
                    <div class="col-lg">
                        <!-- Error message -->
                        <p id="errorMessage" class="text-center errormessage">${errorMessage}</p>
                        <!-- Edit movie information form -->
                        <form id="editMovieForm" action="${S.MOVIE_EDIT}" method="POST" enctype="multipart/form-data"
                            onsubmit="return validateMovieForm(this)">
                            <!-- Save hook -->
                            <div class="form-group">
                                <input type="hidden" name="${S.ACTION_PARAM}" value="${S.ACTION_SAVE}" />
                            </div>
                            <!-- Movie id -->
                            <div class="form-group">
                                <input type="hidden" name="${S.MOVIE_ID_PARAM}" value="${movieId}" />
                            </div>
                            <!-- Input title -->
                            <div class="form-group">
                                <label>Title</label><span class="errormessage">*</span><br>
                                <input class="form-control" name="${S.TITLE_PARAM}" type="text"
                                    placeholder="Enter title" value="${titleInput}" />
                            </div>
                            <!-- Input release date -->
                            <div class="form-group">
                                <label>Release Date</label><span class="errormessage">*</span><br>
                                <input class="form-control" name="${S.RELEASE_DATE_PARAM}" type="date"
                                    value="${releaseDateInput}" />
                            </div>
                            <!-- Input duration -->
                            <div class="form-group">
                                <label>Duration</label><span class="errormessage">*</span><br>
                                <input class="form-control" name="${S.DURATION_PARAM}" type="text"
                                    placeholder="Enter duration in minutes" onkeyup="checkDuration(this)"
                                    value="${durationInput}" />
                                <!-- Duration error -->
                                <span id="durationError" class="errormessage"></span>
                            </div>
                            <!-- Input trailer -->
                            <div class="form-group">
                                <label>Trailer Source</label><span class="errormessage">*</span><br>
                                <input class="form-control" name="${S.TRAILER_PARAM}" type="text"
                                    placeholder="Enter trailer source..." value="${trailerInput}" />
                            </div>
                            <!-- Input poster -->
                            <div class="form-group">
                                <label>Poster</label><br>
                                <input name="${S.POSTER_PARAM}" type="file" />
                            </div>
                            <!-- Input description -->
                            <div class="form-group">
                                <label>Description</label><span class="errormessage">*</span><br>
                                <textarea class="form-control" name="${S.DESCRIPTION_PARAM}" cols="60" rows="5"
                                    maxlength="1000" style="resize: none;"
                                    placeholder="Enter movie description...">${descriptionInput}</textarea>
                            </div>
                        </form>
                        <!-- Cancel form -->
                        <form id="cancelMovieForm" action="${S.MOVIE_EDIT}" method="POST">
                            <!-- Cancel hook -->
                            <div class="form-group">
                                <input type="hidden" name="${S.ACTION_PARAM}" value="${S.ACTION_CANCEL}" />
                            </div>
                            <!-- Movie id -->
                            <div class="form-group">
                                <input type="hidden" name="${S.MOVIE_ID_PARAM}" value="${movieId}" />
                            </div>
                        </form>
                        <div class="text-center">
                            <input form="editMovieForm" type="submit" class="btn btn-outline-info" value="Save">
                            <input form="cancelMovieForm" type="submit" class="btn btn-outline-info" value="Cancel" />
                        </div>
                    </div>
                    <div class="col-lg-3"></div>
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