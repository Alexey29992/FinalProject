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
            document.getElementById("submit").value = "Sign in";
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
<my:homeButton/>
<form method="post" action="${pageContext.request.contextPath}/controller" class="login-form">
    <input type="hidden" name="command" value="login"/>
    <label for="login">Login:</label><br/>
    <input type="text" id="login" name="login" class="input-text-form"/><br/>
    <label for="pass">Password:</label><br/>
    <input type="password" id="pass" name="password" class="input-text-form"/><br/>
    <label for="visibility">Visible:</label>
    <input type="checkbox" id="visibility" onclick="togglePassVisibility()"/><br/><br/>
    <label for="sign-up">Sign Up:</label>
    <input type="radio" id="sign-up" name="type" value="sign-up" onclick="setButtonLabel('Sign up');"/>
    <label for="sign-in">Sign In:</label>
    <input type="radio" id="sign-in" name="type" value="sign-in" onclick="setButtonLabel('Sign in');"/>
    <br/>
    <input type="submit" id="submit" value="Submit" class="submit-button"/>
</form>
<my:error/>
</body>
</html>
