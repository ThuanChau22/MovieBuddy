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

    request.setAttribute("theatreId", session.getAttribute(S.ROOM_THEATRE_ID));
    request.setAttribute("theatreName", session.getAttribute(S.ROOM_THEATRE_NAME));

    request.setAttribute("roomId", session.getAttribute(S.ROOM_EDIT_ID));
    request.setAttribute("roomNumberInput", session.getAttribute(S.ROOM_EDIT_NUMBER));
    request.setAttribute("sectionsInput", session.getAttribute(S.ROOM_EDIT_SECTIONS));
    request.setAttribute("seatsInput", session.getAttribute(S.ROOM_EDIT_SEATS));

    request.setAttribute("errorMessage", session.getAttribute(S.ERROR_MESSAGE));
    session.removeAttribute(S.ERROR_MESSAGE);
%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
        integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/style.css">
    <title>Movie Buddy | Manage Theatre</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./${S.NAV_BAR_PAGE}" />
    <div style="min-height: 60px;"></div>
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <!-- Current theatre name -->
                <h3>Theatre: ${theatreName}</h3>
                <hr>
                <a class="inputAsLink" href="./${S.MANAGE_ROOM_PAGE}">&#8249;
                    <span>Back</span>
                </a>
                <h1 class="display-3 text-center">Update room</h1>
                <hr>
                <div class="row">
                    <div class="col"></div>
                    <div class="col-4">
                        <!-- Error message -->
                        <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                        <!-- Edit room information form -->
                        <form id="editRoomForm" action="RoomEdit" method="POST" onsubmit="return validateRoomForm(this)">
                            <!-- Save hook -->
                            <div class="form-group">
                                <input type="hidden" name="action" value="save" />
                            </div>
                            <!-- Theatre id -->
                            <div class="form-group">
                                <input id="theatreId" type="hidden" name="theatreId" value="${theatreId}" />
                            </div>
                            <!-- Room id -->
                            <div class="form-group">
                                <input type="hidden" name="roomId" value="${roomId}" />
                            </div>
                            <!-- Input room number -->
                            <div class="form-group">
                                <label>Room Number</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="roomNumber" type="number" min="1" placeholder="Enter room number" onkeyup="checkRoomNumber('theatreId', this, 'roomNumberError')" value="${roomNumberInput}" />
                                <br>
                                <!-- Room number error -->
                                <span id="roomNumberError" class="errormessage"></span>
                            </div>
                            <!-- Input number of sections -->
                            <div class="form-group">
                                <label>Sections</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="sections" type="number" min="1" placeholder="Number of sections"
                                    value="${sectionsInput}" />
                            </div>
                            <!-- Input seats per section -->
                            <div class="form-group">
                                <label>Seats</label><span class="errormessage">*</span><br>
                                <input class="inputbox" name="seats" type="number" min="1" placeholder="Seats per section"
                                    value="${seatsInput}" />
                            </div>
                        </form>
                        <!-- Cancel form -->
                        <form id="cancelRoomForm" action="RoomEdit" method="POST">
                            <!-- Cancel hook -->
                            <div class="form-group">
                                <input type="hidden" name="action" value="cancel" />
                            </div>
                        </form>
                        <div class="text-center">
                            <div class="button">
                                <input form="editRoomForm" type="submit" class="btn btn-outline-info" value="Save">
                            </div>
                            <div class="button">
                                <input form="cancelRoomForm" type="submit" class="btn btn-outline-info" value="Cancel" />
                            </div>
                        </div>
                    </div>
                    <div class="col"></div>
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