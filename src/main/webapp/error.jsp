<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="moviebuddy.util.S" %>
<%
    // ${returnLink}
%>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
        integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/style.css">
    <link rel="icon" href="./images/MovieBuddy.ico">
    <title>Movie Buddy | Error</title>
    <style>
        .box-wrapper {
            position: relative;
            width: 100%;
            height: 100%;
            min-height: 100px;
        }

        .component-box {
            position: absolute;
            top: 20%;
            width: 100%;
        }

        .text-box {
            position: absolute;
            bottom: 0%;
            width: 100%;
        }

        .link {
            background-color: transparent;
            border: none;
            color: #007bff;
            cursor: pointer;
            font-size: 1rem;
            text-decoration: none;
        }

        .link:hover {
            color: #0056b3;
            text-decoration: none;
        }

        .link:focus {
            outline: none;
        }
    </style>
</head>

<body>
    <div id="custom-scroll">
        <div class="main">
            <div class="container">
                <div class="box-wrapper">
                    <div class="component-box">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="text-center">
                                    <img src="./images/error.png" class="rounded mx-auto w-100" alt="saitama">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="box-wrapper">
                                    <div class="text-box text-center">
                                        <h1>Oops!!!</h1>
                                        It seems like something just went wrong.<br>
                                        Please <a class="link" href="${returnLink}">click here</a> to return.
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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
</body>

</html>