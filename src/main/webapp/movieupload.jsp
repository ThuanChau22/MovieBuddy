<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="moviebuddy.util.Passwords" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setHeader("Expires", "0"); // Proxies

    session = request.getSession();
    if (session.getAttribute("sessionId") == null) {
        session.setAttribute("sessionId", Passwords.applySHA256(session.getId()));
    }
    if (session.getAttribute("count") == null) {
        session.setAttribute("count", 0);
    } else {
        int count = (int) session.getAttribute("count");
        session.setAttribute("count", count + 1);
    }

    if(session.getAttribute("email") == null || !session.getAttribute("currentSession").equals(Passwords.applySHA256(session.getId() + request.getRemoteAddr())) || session.getAttribute("staffId") == null || !(session.getAttribute("role").equals("admin") || session.getAttribute("role").equals("manager"))){
        response.sendRedirect("home.jsp");
    }

    request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
    request.setAttribute("movieTitleUpload", session.getAttribute("movieTitleUpload"));
    request.setAttribute("movieReleaseDateUpload", session.getAttribute("movieReleaseDateUpload"));
    request.setAttribute("movieDurationUpload", session.getAttribute("movieDurationUpload"));
    request.setAttribute("movieTrailerUpload", session.getAttribute("movieTrailerUpload"));
    request.setAttribute("movieDescriptionUpload", session.getAttribute("movieDescriptionUpload"));
    session.removeAttribute("errorMessage");
    session.removeAttribute("movieTitleUpload");
    session.removeAttribute("movieReleaseDateUpload");
    session.removeAttribute("movieDurationUpload");
    session.removeAttribute("movieTrailerUpload");
    session.removeAttribute("movieDescriptionUpload");
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
    <title>Movie Buddy | Manage Movie</title>
</head>

<body style="height: 100%; display: flex; flex-direction: column;">
    <div style="flex: 1 0 auto;">
        <!-- Navigation bar -->
        <jsp:include page="/navbar.jsp" />

        <!-- Page Content -->
        <div class="container">
            <h1 class="display-3 text-center">Upload Movie Information</h1>
            <hr>
            <a class="inputAsLink" href="./managemovie.jsp">&#8249;
                <span>Back</span>
            </a>
            <div class="row">
                <div class="col"></div>
                <div class="col-6">
                    <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                    <form id="uploadMovieForm" action="MovieUpload" method="POST" enctype="multipart/form-data"
                        onsubmit="return validateMovieForm(this)">
                        <div class="form-group">
                            <label>Title</label><span class="errormessage">*</span><br>
                            <input class="inputbox" name="title" type="text" placeholder="Enter title"
                                value="${movieTitleUpload}" />
                        </div>
                        <div class="form-group">
                            <label>Release Date</label><span class="errormessage">*</span><br>
                            <input class="inputbox" name="releaseDate" type="date" value="${movieReleaseDateUpload}" />
                        </div>
                        <div class="form-group">
                            <label>Duration</label><span class="errormessage">*</span><br>
                            <input class="inputbox" name="duration" type="text" placeholder="Enter duration in minutes"
                                value="${movieDurationUpload}" />
                        </div>
                        <div class="form-group">
                            <label>Trailer Source</label><span class="errormessage">*</span><br>
                            <input class="inputbox" name="trailer" type="text" placeholder="Enter trailer source..."
                                value="${movieTrailerUpload}" />
                        </div>
                        <div class="form-group">
                            <label>Poster</label><br>
                            <input class="inputbox" name="poster" type="file" />
                        </div>
                        <div class="form-group">
                            <label>Description</label><span class="errormessage">*</span><br>
                            <textarea class="inputbox" name="description" cols="60" rows="5" maxlength="1000"
                                placeholder="Enter movie description...">${movieDescriptionUpload}</textarea>
                        </div>
                        <div class="text-center">
                            <input type="submit" class="btn btn-outline-info" value="Upload">
                        </div>
                    </form>
                </div>
                <div class="col"></div>
            </div>
        </div>
    </div>
    <div style="flex-shrink: 0;">
        <hr>
        <p class="text-center">CS157A-Section01-Team11&copy;2020</p>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
        crossorigin="anonymous"></script>

    <script src="./JS/validation.js"></script>
</body>

</html>