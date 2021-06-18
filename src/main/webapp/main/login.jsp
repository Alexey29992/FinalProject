<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<c:if test="${not empty sessionScope.user}">
    <c:redirect url="/main/home.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
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
    <title><fmt:message key="nav_bar.common.log_in"/></title>
</head>
<body>
<my:navBar/>
<div class="login-form">
    <form method="post" action="${pageContext.request.contextPath}/controller">
        <input type="hidden" name="command" value="login"/>
        <label for="login"><fmt:message key="user.login"/>:</label><br/>
        <input type="text" id="login" name="login"
               placeholder="<fmt:message key="user.login"/>..."/><br/>
        <label for="pass"><fmt:message key="user.password"/>:</label><br/>
        <input type="password" id="pass" name="password"
               placeholder="<fmt:message key="user.password"/>..."/><br/>
        <label for="visibility"><fmt:message key="common.visibility"/>:</label>
        <input type="checkbox" id="visibility" onclick="togglePassVisibility()"/><br/><br/>
        <label for="sign-up"><fmt:message key="login.radio.sign_up"/>:</label>
        <input type="radio" id="sign-up" name="type" value="sign-up"
               onclick="setButtonLabel('<fmt:message key="login.radio.sign_up"/>');"/>
        <label for="sign-in"><fmt:message key="login.radio.sign_in"/>:</label>
        <input type="radio" id="sign-in" name="type" value="sign-in"
               onclick="setButtonLabel('<fmt:message key="login.radio.sign_in"/>');" checked/>
        <br/>
        <input type="submit" id="submit" value="<fmt:message key="login.radio.sign_in"/>"/>
    </form>
</div>
<my:error/>
</body>
</html>
