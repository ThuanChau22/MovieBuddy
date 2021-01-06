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
    // ${ticketPriceList}
    // ${startTimeInput}
    // ${priceInput}
    // ${errorMessage}
%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
        integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/style.css">
    <link rel="icon" href="./images/MovieBuddy.ico">
    <title>Movie Buddy | Ticket Price</title>
</head>

<body>
    <!-- Navigation bar -->
    <jsp:include page="./${S.NAV_BAR_PAGE}" />
    <div id="custom-scroll">
        <div class="main">
            <!-- Page content -->
            <div class="container">
                <!-- Current theatre name -->
                <h3>Theatre: ${theatreName}</h3>
                <hr>
                <a class="inputAsLink" href="./${S.THEATRE}">&lsaquo;<span>Back</span>
                </a>
                <h1 class="display-3 text-center">Ticket Prices</h1>
                <hr>
                <!-- Error message -->
                <div class="errormessagePadding">
                    <div class="errormessageWrapper">
                        <p class="text-center errormessage" id="errorMessage">${errorMessage}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md"></div>
                    <div class="col-md">
                        <table>
                            <tr>
                                <th>Start time</th>
                                <th>Price</th>
                                <th>Actions</th>
                            </tr>
                            <tr>
                                <td class="inputCell">
                                    <!-- Input start time -->
                                    <input form="addTicketPriceForm" style="width: 80px;" name="${S.START_TIME_PARAM}"
                                        type="time" value="${startTimeInput}" />
                                </td>
                                <td class="inputCell">
                                    <!-- Input price -->
                                    <input form="addTicketPriceForm" style="width: 80px;" name="${S.PRICE_PARAM}"
                                        type="number" min="0" step="0.01" ; placeholder="11.50" value="${priceInput}" />
                                </td>
                                <td class="inputCell">
                                    <!-- Add ticket price -->
                                    <form id="addTicketPriceForm" action="${S.TICKET_PRICE_CREATE}" method="POST"
                                        class="button" onsubmit="return validateTicketPriceForm(this)">
                                        <input type="hidden" name="${S.THEATRE_ID_PARAM}" value="${theatreId}" />
                                        <input type="submit" class="btn btn-outline-info" value="Add" />
                                    </form>
                                </td>
                            </tr>
                            <!-- List of ticket prices -->
                            <c:forEach items="${ticketPriceList}" var="ticketPrice">
                                <tr>
                                    <!-- Start time -->
                                    <td>${ticketPrice.getStartTime()}</td>
                                    <!-- Ticket price -->
                                    <td>$${ticketPrice.displayPrice()}</td>
                                    <td>
                                        <!-- Delete ticket price -->
                                        <form action="${S.TICKET_PRICE_DELETE}" method="POST" class="button">
                                            <input type="hidden" name="${S.THEATRE_ID_PARAM}" value="${theatreId}" />
                                            <input type="hidden" name="${S.START_TIME_PARAM}"
                                                value="${ticketPrice.getStartTime()}" />
                                            <input type="submit" class="btn btn-outline-danger" value="Delete" />
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                    <div class="col-md"></div>
                </div>
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