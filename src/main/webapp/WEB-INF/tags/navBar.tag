<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<div class="nav-bar">
    <header class="head">
        <h1><fmt:message key="nav_bar.common.welcome"/></h1>
    </header>
    <c:if test="${not empty sessionScope.user}">
        <form method="post" action="${pageContext.request.contextPath}/controller" class="log-in-out-button">
            <input type="hidden" name="command" value="logout">
            <button class="medium-text">
                <img src="${pageContext.request.contextPath}/resources/logout.png"
                     class="button-img" alt="out-button"/>
                <fmt:message key="button.log_out"/>
            </button>
        </form>
    </c:if>
    <c:set var="loginPage" value="${pageContext.request.contextPath}/main/login.jsp"/>
    <c:if test="${empty sessionScope.user && pageContext.request.requestURI != loginPage}">
        <a href="${pageContext.request.contextPath}/main/login.jsp" class="log-in-out-button">
            <button class="medium-text">
                <img src="${pageContext.request.contextPath}/resources/login.png"
                     class="button-img" alt="enter-button"/>
                <fmt:message key="button.log_in"/>
            </button>
        </a>
    </c:if>
    <c:set var="homePage" value="${pageContext.request.contextPath}/main/home.jsp"/>
    <c:if test="${pageContext.request.requestURI != homePage}">
        <a href="${homePage}" class="back-button">
            <button class="medium-text">
                <img src="${pageContext.request.contextPath}/resources/back.png"
                     class="button-img" alt="enter-button"/>
                <fmt:message key="button.home"/>
            </button>
        </a>
    </c:if>
    <c:choose>
        <c:when test="${empty sessionScope.user}">
            <c:set var="role" scope="request" value="guest"/>
        </c:when>
        <c:when test="${not empty sessionScope.user}">
            <c:set var="role" scope="request" value="${sessionScope.user.role.toLowerCaseString()}"/>
        </c:when>
    </c:choose>
    <ul>
        <li class="dropdown lang">
            <form method="get" action="${pageContext.request.requestURI}">
        <span class="drop-btn">
            ${sessionScope.lang.toUpperCase()}
        </span>
                <div class="dropdown-content">
                    <button name="lang" value="ru" onclick="this.form.submit()">RU</button>
                    <button name="lang" value="en" onclick="this.form.submit()">EN</button>
                </div>
            </form>
        </li>
        <jsp:include page="/main/role-dependent/${role}/home-nav-bar.jsp"/>
        <c:if test="${not empty sessionScope.user}">
            <li style="float: right">
                <span>
                    <fmt:message key="user.role.${sessionScope.user.role.toLowerCaseString()}"/>: ${sessionScope.user.login}
                </span>
            </li>
        </c:if>
    </ul>
</div>