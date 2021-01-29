<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="moviebuddy.util.S" %>
<form id="theatreLocationForm" action="${S.LOCATION}" method="POST" class="form-inline"
    onsubmit="return validateZipCodeForm(this)">
    <div class="input-group input-group-sm pr-2">
        <div class="input-group-prepend">
            <span class="input-group-text"><b>${theatrename}</b></span>
        </div>
        <c:choose>
            <c:when test="${fn:length(zipInput) > 0}">
                <input class="form-control" type="text" name="${S.ZIP_PARAM}" placeholder="Zip Code" value="${zipInput}"
                    style="max-width: 100px;">
            </c:when>
            <c:otherwise>
                <input class="form-control" type="text" name="${S.ZIP_PARAM}" placeholder="Zip Code" value="${zipcode}"
                    style="max-width: 100px;">
            </c:otherwise>
        </c:choose>
        <div class="input-group-append">
            <input type="submit" class="btn btn-outline-info" value="Change">
        </div>
    </div>
    <!-- Zip code error -->
    <span id="zipError" class="errormessage">${errorMessage}</span>
</form>