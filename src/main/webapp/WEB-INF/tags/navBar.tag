<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<div class="nav-bar">
    <header class="head">
        <h1>Welcome to repairing agency!</h1>
    </header>
    <my:logInOutButton/>
    <my:homeButton/>
    <c:choose>
        <c:when test="${empty sessionScope.user}">
            <c:set var="role" scope="request" value="guest"/>
        </c:when>
        <c:when test="${not empty sessionScope.user}">
            <c:set var="role" scope="request" value="${sessionScope.user.role.toLowerCaseString()}"/>
        </c:when>
    </c:choose>
    <jsp:include page="/main/role-dependent/${role}/home-nav-bar.jsp"/>
</div>