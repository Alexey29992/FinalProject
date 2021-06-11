<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/user-info.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>User Info</title>
</head>
<body>
<my:navBar/>
<c:if test="${}">

</c:if>
<c:choose>
    <c:when test="${not empty requestScope.user}">
        User: ${requestScope.user.id}
        Login: ${requestScope.user.login}
        Role: ${requestScope.user.role}
        <c:if test="${requestScope.user.role == 'CLIENT'}">
            Phone: ${requestScope.user.phNumber}
            <form method="post" action="${pageContext.request.contextPath}/controller">
                <input type="hidden" name="command" value="top-up-balance"/>
                <input type="hidden" name="client-id" value="${requestScope.user.id}"/>
                <label>
                    Top-up balance:
                    <input type="text" name="amount" placeholder="Sum..."/>
                </label>
            </form>
            Active Requests
        </c:if>
    </c:when>
    <c:otherwise>
        <form method="get" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="get-user-by-login"/>
            <label>
                Find by login:
                <input type="text" name="user-login" placeholder="User login..."/>
            </label>
            <input type="submit" value="Submit">
        </form>
        <form method="get" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="get-user-by-id"/>
            <label>
                Find by id:
                <input type="text" name="user-id" placeholder="User id..."/>
            </label>
            <input type="submit" value="Submit">
        </form>
    </c:otherwise>
</c:choose>
<my:error/>
</body>
</html>