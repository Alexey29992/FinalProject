<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/info.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>User Info</title>
</head>
<body>
<my:navBar/>
<ralib:onRequest/>
<c:if test="${not empty sessionScope.userId && empty requestScope.currentUser}">
    <jsp:forward page="/controller?command=get-user&user-id=${sessionScope.userId}"/>
</c:if>
<div class="global-frame">
    <div class="text-form">
        <form method="get" action="${pageContext.request.requestURI}">
            <input type="hidden" name="command" value="get-user"/>
            <label>
                Find by login:<br/>
                <input type="text" name="user-login" placeholder="User login..."
                       value="${pageContext.request.getParameter('user-login')}"/>
            </label>
            <input type="submit" value="Find">
        </form>
        <form method="get" action="${pageContext.request.requestURI}">
            <input type="hidden" name="command" value="get-user"/>
            <label>
                Find by id:<br/>
                <input type="text" name="user-id" placeholder="User id..."
                       value="${pageContext.request.getParameter('user-id')}"/>
            </label>
            <input type="submit" value="Find">
        </form>
    </div>
    <c:set var="currentUser" value="${requestScope.currentUser}"/>
    <c:if test="${not empty currentUser}">
        <div class="result">
            <table>
                <tr>
                    <td>User:</td>
                    <td>#${currentUser.id}</td>
                </tr>
                <tr>
                    <td>Login:</td>
                    <td>${currentUser.login}</td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td>${currentUser.role}</td>
                </tr>
                <c:if test="${currentUser.role == 'CLIENT'}">
                    <tr>
                        <td>Phone:</td>
                        <td>${currentUser.phNumber}</td>
                    </tr>
                    <tr>
                        <td>Balance:</td>
                        <td>${currentUser.balance}$</td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${currentUser.role == 'CLIENT'}">
                <form method="post" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="top-up-balance"/>
                    <input type="hidden" name="client-id" value="${currentUser.id}"/>
                    <label>
                        Top-up balance:<br/>
                        <input type="text" name="amount" placeholder="Sum..." autocomplete="off"/>
                    </label>
                    <input type="submit" value="Top up">
                </form>
            </c:if>
        </div>
    </c:if>
</div>
<my:error/>
</body>
</html>
