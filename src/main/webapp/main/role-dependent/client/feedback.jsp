<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/new-request.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>Leave feedback</title>
</head>
<body>
<my:navBar/>
<form method="post" action="${pageContext.request.contextPath}/controller" class="text-form">
    <input type="hidden" name="command" value="feedback"/>
    <input type="hidden" name="request-id" value="${pageContext.request.getParameter('request-id')}">
    <label>
        Feedback:<br/>
        <textarea id="user-input" name="feedback" class="textarea"
                  placeholder="Share your opinion about our service..."></textarea><br/>
    </label>
    <input type="submit" value="Submit" class="submit-button">
</form>
</body>
</html>

