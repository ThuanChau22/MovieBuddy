<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="moviebuddy.util.Passwords" %>
<%@ page import="moviebuddy.util.S" %>
<jsp:include page="/HomeGet" />
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
            <!-- Page Content -->
            <div class="container">
                <h1 class="display-1 text-center">Movie Buddy</h1>
                <div id="searchBar">
                    <br>
                    <form class="form-inline justify-content-center">
                        <div class="form-group mx-sm-3 mb-2">
                            <input type="search" class="form-control" id="searchInput" placeholder="Search">
                        </div>
                        <button type="submit" class="btn btn-outline-success mb-2">Search</button>
                    </form>
                </div>
                <hr>
                <div id="showtimes">
                    <nav>
                        <ul class="pagination justify-content-center">
                            <div class="nav nav-tabs" id="nav-tab1" role="tablist">
                                <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
        
        
        
                                <a class="page-link active" id="nav-Day1-tab1" data-toggle="tab" href="#nav-Day1" role="tab"
                                    aria-controls="nav-Day1" aria-selected="true">1/1</a>
                                <a class="page-link" id="nav-Day2-tab1" data-toggle="tab" href="#nav-Day2" role="tab"
                                    aria-controls="nav-Day2" aria-selected="false">2</a>
                                <a class="page-link" id="nav-Day3-tab1" data-toggle="tab" href="#nav-Day4" role="tab"
                                    aria-controls="nav-Day3" aria-selected="false">3</a>
                                <a class="page-link" id="nav-Day4-tab1" data-toggle="tab" href="#nav-Day4" role="tab"
                                    aria-controls="nav-Day4" aria-selected="false">4</a>
                                <a class="page-link" id="nav-Day5-tab1" data-toggle="tab" href="#nav-Day5" role="tab"
                                    aria-controls="nav-Day5" aria-selected="false">5</a>
                                <a class="page-link" id="nav-Day6-tab1" data-toggle="tab" href="#nav-Day6" role="tab"
                                    aria-controls="nav-Day6" aria-selected="false">6</a>
                                <a class="page-link" id="nav-Day7-tab1" data-toggle="tab" href="#nav-Day7" role="tab"
                                    aria-controls="nav-Day7" aria-selected="false">7</a>
                                <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Next</a>
                            </div>
                        </ul>
                    </nav>
                    <div class="tab-content" id="nav-tabContent1">
                        <!-- showtime outter loop starts -->
                        <div class="tab-pane fade show active" id="nav-Day1" role="tabpanel" aria-labelledby="nav-Day1-tab1">
                            <br>
                            <div class="container">
                                <h1 class="display-4">Showtimes</h1>
                                <!-- showtime inner loop starts -->
                                <div class="card">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col col-lg-5">
                                                <div class="text-center">
                                                    <img src="" class="rounded mx-auto w-75" alt="poster">
                                                </div>
                                            </div>
                                            <div class="col">
                                                <ul class="list-inline">
                                                    <li class="list-inline-item">
                                                        <h1>Movie 1</h1>
                                                    </li>
                                                    <li class="list-inline-item">
                                                        <p class="">Length: 00:00</p>
                                                    </li>
                                                </ul>
                                                <hr>
                                                <h3>Trailer</h3>
                                                <div class="embed-responsive embed-responsive-16by9">
                                                    <iframe class="embed-responsive-item"
                                                        src="https://www.youtube.com/embed/zpOULjyy-n8?rel=0"
                                                        allowfullscreen></iframe>
                                                </div>
                                                <hr>
                                                <h3>Description</h3>
                                                <p>Description</p>
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col">
                                                <div class="container">
                                                    <a href="./seatSelect.html"><button type="button"
                                                            class="btn btn-outline-info">00:00
                                                            am</button></a>
                                                    <a href="./seatSelect.html"><button type="button"
                                                            class="btn btn-outline-info">00:00
                                                            am</button></a>
                                                    <a href="./seatSelect.html"><button type="button"
                                                            class="btn btn-outline-info">00:00
                                                            am</button></a>
                                                    <a href="./seatSelect.html"><button type="button"
                                                            class="btn btn-outline-info">00:00
                                                            am</button></a>
                                                    <a href="./seatSelect.html"><button type="button"
                                                            class="btn btn-outline-info">00:00
                                                            am</button></a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <!-- showtime inner loop ends -->
                                <div class="card">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col col-lg-5">
                                                <div class="text-center">
                                                    <img src="" class="rounded mx-auto w-75" alt="poster">
                                                </div>
                                            </div>
                                            <div class="col">
                                                <ul class="list-inline">
                                                    <li class="list-inline-item">
                                                        <h1>Movie 1</h1>
                                                    </li>
                                                    <li class="list-inline-item">
                                                        <p class="">Length: 00:00</p>
                                                    </li>
                                                </ul>
                                                <hr>
                                                <h3>Trailer</h3>
                                                <div class="embed-responsive embed-responsive-16by9">
                                                    <iframe class="embed-responsive-item"
                                                        src="https://www.youtube.com/embed/zpOULjyy-n8?rel=0"
                                                        allowfullscreen></iframe>
                                                </div>
                                                <hr>
                                                <h3>Description</h3>
                                                <p>Description</p>
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col">
                                                <div class="container">
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- showtime outter loop ends -->
                        <div class="tab-pane fade" id="nav-Day2" role="tabpanel" aria-labelledby="nav-Day2-tab1">
                            <br>
                            <div class="container">
                                <h1 class="display-4">Showtimes</h1>
                                <!-- showtime inner loop starts -->
                                <div class="card">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col col-lg-5">
                                                <div class="text-center">
                                                    <img src="" class="rounded mx-auto w-75" alt="poster">
                                                </div>
                                            </div>
                                            <div class="col">
                                                <ul class="list-inline">
                                                    <li class="list-inline-item">
                                                        <h1>Movie 1</h1>
                                                    </li>
                                                    <li class="list-inline-item">
                                                        <p class="">Length: 00:00</p>
                                                    </li>
                                                </ul>
                                                <hr>
                                                <h3>Trailer</h3>
                                                <div class="embed-responsive embed-responsive-16by9">
                                                    <iframe class="embed-responsive-item"
                                                        src="https://www.youtube.com/embed/zpOULjyy-n8?rel=0"
                                                        allowfullscreen></iframe>
                                                </div>
                                                <hr>
                                                <h3>Description</h3>
                                                <p>Description</p>
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col">
                                                <div class="container">
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                    <button type="button" class="btn btn-outline-info">00:00 am</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <!-- showtime inner loop ends -->
                            </div>
                        </div>
                        <div class="tab-pane fade" id="nav-Day3" role="tabpanel" aria-labelledby="nav-Day3-tab1">
                            <br>
                            <div class="container">
                                <p> No Content</p>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="nav-Day4" role="tabpanel" aria-labelledby="nav-Day4-tab1">
                            <br>
                            <div class="container">
                                <p> No Content</p>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="nav-Day5" role="tabpanel" aria-labelledby="nav-Day5-tab1">
                            <br>
                            <div class="container">
                                <p> No Content</p>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="nav-Day6" role="tabpanel" aria-labelledby="nav-Day6-tab1">
                            <br>
                            <div class="container">
                                <p>No Content</p>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="nav-Day7" role="tabpanel" aria-labelledby="nav-Day7-tab1">
                            <br>
                            <div class="container">
                                <p> No Content</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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