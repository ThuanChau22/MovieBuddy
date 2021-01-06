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

    // Set authentication status
    request.setAttribute("signedIn", false);
    request.setAttribute("isProvider", false);
    request.setAttribute("isAdmin", false);

    // Check authentication
    Object accountId = session.getAttribute(S.ACCOUNT_ID);
    Object currentSession = session.getAttribute(S.CURRENT_SESSION);
    if(accountId != null && currentSession.equals(Passwords.applySHA256(session.getId() + request.getRemoteAddr()))){
        request.setAttribute("signedIn", true);
        request.setAttribute("userName", session.getAttribute(S.USERNAME));
        request.setAttribute("zipcode", session.getAttribute(S.ZIPCODE));
        if(session.getAttribute(S.STAFF_ID) != null){
            // Authentication as admin
            if(session.getAttribute(S.ROLE).equals(S.ADMIN)){
                request.setAttribute("isProvider", true);
                request.setAttribute("isAdmin", true);
            }
            // Authentication as manager
            if(session.getAttribute(S.ROLE).equals(S.MANAGER)){
                request.setAttribute("isProvider", true);
            }
        }
    }
%>
<nav id="movieBuddyNavBar" class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggler"
        aria-controls="navbarToggler" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <a class="navbar-brand" href="./${S.HOME}"><b>Movie Buddy</b></a>
    <div class="collapse navbar-collapse" id="navbarToggler">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <!-- Provider options -->
            <c:if test="${isProvider}">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                        Manage
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <c:if test="${isAdmin}">
                            <a class="dropdown-item" href="./${S.THEATRE}">Theatre</a>
                        </c:if>
                        <a class="dropdown-item" href="./${S.MOVIE}">Movie</a>
                        <a class="dropdown-item" href="./${S.STAFF}">Staff</a>
                    </div>
                </li>
            </c:if>
            <li class="nav-item active">
                <!-- Current theatre location form -->
                <form class="form-inline my-2 my-lg-0">
                    <label class="mx-2 navbar-brand">Theatre Name</label>
                    <input class="form-control mr-sm-2" type="search" placeholder="Zip Code" value="${zipcode}">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Enter</button>
                </form>
            </li>
        </ul>
        <c:choose>
            <c:when test="${signedIn}">
                <!-- Signed In -->
                <form action="" method="POST" class="formAsLink">
                    <input class="inputAsLink" type="submit" value="${userName}">
                </form>
                <form action="${S.SIGN_OUT}" method="POST" class="formAsLink">
                    <input class="inputAsLink" type="submit" value="Sign Out">
                </form>
            </c:when>
            <c:otherwise>
                <!-- Signed Out -->
                <a class="nav-link" href="./${S.SIGN_IN}">Sign In</a>
                <a class="nav-link" href="./${S.SIGN_UP}">Sign Up</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
<div class="navbarPadding"></div>