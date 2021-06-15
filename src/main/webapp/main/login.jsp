<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/login.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <script type="text/javascript">
        window.onload = function () {
            document.getElementById("sign-in").checked = true;
            document.getElementById("visibility").checked = false;
        }

        function setButtonLabel(x) {
            document.getElementById("submit").value = x;
        }

        function togglePassVisibility() {
            let x = document.getElementById("pass");
            if (x.type === "password") {
                x.type = "text";
            } else {
                x.type = "password";
            }
        }
    </script>
    <title>Login page</title>
</head>
<body>
<div class="nav-bar">
    <my:homeButton/>
</div>
<div class="login-form">
    <form method="post" action="${pageContext.request.contextPath}/controller">
        <input type="hidden" name="command" value="login"/>
        <label for="login">Login:</label><br/>
        <input type="text" id="login" name="login" placeholder="Login..."/><br/>
        <label for="pass">Password:</label><br/>
        <input type="password" id="pass" name="password" placeholder="Password..."/><br/>
        <label for="visibility">Visible:</label>
        <input type="checkbox" id="visibility" onclick="togglePassVisibility()"/><br/><br/>
        <label for="sign-up">Sign Up:</label>
        <input type="radio" id="sign-up" name="type" value="sign-up" onclick="setButtonLabel('Sign up');"/>
        <label for="sign-in">Sign In:</label>
        <input type="radio" id="sign-in" name="type" value="sign-in" onclick="setButtonLabel('Sign in');" checked/>
        <br/>
        <input type="submit" id="submit" value="Sign in"/>
    </form>
</div>
<my:error/>
</body>
</html>
