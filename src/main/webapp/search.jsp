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

    // ${input}
    // ${resultCount}
    // ${movieList}
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
                <c:choose>
                    <c:when test="${resultCount > 0}">
                        <h1 style="color: #b3b3b3;">Search result for "${input}"</h1>
                        <hr>
                        <div class="row">
                            <div class="col-md-10 col-lg-8">
                                <h4 class="search-result-header found">Movies (${resultCount})</h4>
                                <c:forEach items="${movieList}" var="movie">
                                    <div class="card mb-2">
                                        <div class="card-body p-2">
                                            <div class="row">
                                                <div class="col-sm-4 col-md-3 col-lg-2">
                                                    <div class="text-center">
                                                        <!-- Movie poster -->
                                                        <img src=${movie.getPoster()} class="rounded mx-auto w-100"
                                                            alt="poster">
                                                    </div>
                                                </div>
                                                <div class="col">
                                                    <div class="row">
                                                        <div class="col-12 col-md-7 col-lg-8">
                                                            <ul class="list-inline">
                                                                <!-- Movie title -->
                                                                <h4>${movie.getTitle()}</h5>
                                                                    <!-- Movie length -->
                                                                    <i>Length: ${movie.getDuration()} minutes</i>
                                                                    <br>
                                                                    <!-- Movie release date -->
                                                                    <i>Release on: ${S.date("MM/dd/yyyy",
                                                                        movie.getReleaseDate())}</i>
                                                            </ul>
                                                        </div>
                                                        <div class="col m-auto">
                                                            <a href="./${S.SHOWTIME}?${S.MOVIE_ID_PARAM}=${movie.getId()}"
                                                                class="list-button">
                                                                <button type="button" class="btn btn-outline-info">View
                                                                    showtimes</button>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h1 style="color: #b3b3b3;">No results for "${input}"</h1>
                    </c:otherwise>
                </c:choose>
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