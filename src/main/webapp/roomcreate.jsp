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

    // ${theatreId}
    // ${theatreName}
    // ${roomNumberInput}
    // ${sectionsInput}
    // ${seatsInput}
    // ${errorMessage}
%>
<html lang="en">

<head>
    <!-- Header -->
    <jsp:include page="./components/header.jsp" />
    <title>Movie Buddy | Manage Theatre</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./components/navbar.jsp" />
    <div class="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <!-- Current theatre name -->
                <h3>Theatre: ${theatreName}</h3>
                <hr>
                <a class="custom-link"
                    href="./${S.ROOM}?${S.THEATRE_ID_PARAM}=${theatreId}">&lsaquo;<span>Back</span></a>
                <h1 class="display-4 text-center">Upload Room Information</h1>
                <hr>
                <!-- Error message -->
                <p id="errorMessage" class="text-center errormessage">${errorMessage}</p>
                <div class="row justify-content-center">
                    <div class="col-md-6 col-lg-5">
                        <!-- Upload room information form -->
                        <form id="createRoomForm" action="${S.ROOM_CREATE}" method="POST"
                            onsubmit="return validateRoomForm(this)">
                            <!-- Theatre id -->
                            <div class="form-group">
                                <input id="theatreId" type="hidden" name="${S.THEATRE_ID_PARAM}" value="${theatreId}" />
                            </div>
                            <!-- Input room number -->
                            <div class="form-group">
                                <label>Room Number</label><span class="errormessage">*</span><br>
                                <div style="position: relative">
                                    <input class="form-control" name="${S.ROOM_NUMBER_PARAM}" type="text"
                                        placeholder="Enter room number" onkeyup="checkRoomNumber(this)"
                                        value="${roomNumberInput}" style="padding-right: 30px;" />
                                    <div class="spinner-wrapper">
                                        <span id="roomNumberSpinner"></span>
                                    </div>
                                </div>
                                <!-- Room number error -->
                                <span id="roomNumberError" class="errormessage"></span>
                            </div>
                            <!-- Input number of sections -->
                            <div class="form-group">
                                <label>Sections</label><span class="errormessage">*</span><br>
                                <input class="form-control" name="${S.SECTIONS_PARAM}" type="text"
                                    placeholder="Enter number of sections" onkeyup="checkSections(this)"
                                    value="${sectionsInput}" />
                                <!-- Sections error -->
                                <span id="sectionsError" class="errormessage"></span>
                            </div>
                            <!-- Input seats per section -->
                            <div class="form-group">
                                <label>Seats</label><span class="errormessage">*</span><br>
                                <input class="form-control" name="${S.SEATS_PARAM}" type="text"
                                    placeholder="Enter seats per section" onkeyup="checkSeats(this)"
                                    value="${seatsInput}" />
                                <!-- Seats error -->
                                <span id="seatsError" class="errormessage"></span>
                            </div>
                            <div class="text-center">
                                <input type="submit" class="btn btn-outline-info" value="Upload">
                            </div>
                        </form>
                    </div>
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