<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/new-request.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>New Request</title>
</head>
<body>
<my:navBar/>
<c:choose>
    <c:when test="${not empty sessionScope.user.phNumber}">
        <form method="post" action="${pageContext.request.contextPath}/controller" class="text-form">
            <input type="hidden" name="command" value="create-request"/>
            <label>
                Description:<br/>
                <textarea id="user-input" name="description" class="textarea"
                          placeholder="Describe your problem..."></textarea><br/>
            </label>
            <input type="submit" value="Create request" class="submit-button">
        </form>
    </c:when>
    <c:otherwise>
        <article class="text-form">
            You need to specify a phone number in your profile settings.<br/>
            It is required for our manager be able<br/>
            to contact you for clarifying the details.<br/>
        </article>
    </c:otherwise>
</c:choose>
</body>
</html>
