<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<ralib:onRequest/>
<c:if test="${not empty sessionScope.userId && empty requestScope.currentUser}">
    <jsp:forward page="/controller">
        <jsp:param name="command" value="get-user"/>
        <jsp:param name="user-id" value="${sessionScope.userId}"/>
    </jsp:forward>
</c:if>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/info.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.manager.user_info"/></title>
</head>
<body>
<my:navBar/>
<div class="global-frame">
    <div class="text-form">
        <form method="get" action="${pageContext.request.requestURI}">
            <input type="hidden" name="command" value="get-user"/>
            <label>
                <fmt:message key="manager.user_info.find_login.label"/><br/>
                <input type="text" name="user-login"
                       placeholder="<fmt:message key="manager.user_info.find_login.placeholder"/>"
                       value="${requestScope.currentUser.login}"/>
            </label>
            <input type="submit" value="<fmt:message key="button.find"/>">
        </form>
        <form method="get" action="${pageContext.request.requestURI}">
            <input type="hidden" name="command" value="get-user"/>
            <label>
                <fmt:message key="manager.user_info.find_id.label"/><br/>
                <input type="text" name="user-id"
                       placeholder="<fmt:message key="manager.user_info.find_id.placeholder"/>"
                       value="${requestScope.currentUser.id}"/>
            </label>
            <input type="submit" value="<fmt:message key="button.find"/>">
        </form>
    </div>
    <c:set var="currentUser" value="${requestScope.currentUser}"/>
    <c:if test="${not empty currentUser}">
        <div class="result">
            <table>
                <tr>
                    <td><fmt:message key="user.label"/>:</td>
                    <td>#${currentUser.id}</td>
                </tr>
                <tr>
                    <td><fmt:message key="user.login"/>:</td>
                    <td>${currentUser.login}</td>
                </tr>
                <tr>
                    <td><fmt:message key="user.role"/>:</td>
                    <td><fmt:message key="user.role.${currentUser.role.toLowerCaseString()}"/></td>
                </tr>
                <c:if test="${currentUser.role eq 'CLIENT'}">
                    <tr>
                        <td><fmt:message key="user.ph_number"/>:</td>
                        <td>${currentUser.phNumber}</td>
                    </tr>
                    <tr>
                        <td><fmt:message key="user.balance"/>:</td>
                        <td>${currentUser.balance}$</td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${currentUser.role eq 'CLIENT'}">
                <form method="post" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="top-up-balance"/>
                    <input type="hidden" name="client-id" value="${currentUser.id}"/>
                    <label>
                        <fmt:message key="manager.user_info.top_up.label"/><br/>
                        <input type="text" name="amount"
                               placeholder="<fmt:message key="manager.user_info.top_up.placeholder"/>"
                               autocomplete="off"/>
                    </label>
                    <input type="submit" value="<fmt:message key="button.top_up"/>">
                </form>
            </c:if>
        </div>
    </c:if>
</div>
<my:error/>
</body>
</html>
