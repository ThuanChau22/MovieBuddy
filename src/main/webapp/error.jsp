<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="moviebuddy.util.S" %>
<%
    // ${returnLink}
%>
<html>

<head>
    <!-- Header -->
    <jsp:include page="./components/header.jsp" />
</head>

<body>
    <div class="custom-scroll">
        <div class="main">
            <div class="container">
                <div class="element-center">
                    <div class="row">
                        <div class="col-md-6">
                            <img src="./images/error.png" class="rounded mx-auto w-100" alt="saitama">
                        </div>
                        <div class="col-md-6">
                            <div class="element-center">
                                <div class="text-center">
                                    <h1>Oops!!!</h1>
                                    <span>It seems like something just went wrong.</span><br>
                                    <span>Please <a class="error-link" href="${returnLink}">click here</a> to
                                        return.</span>
                                </div>
                            </div>
                        </div>
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