<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty sessionScope.user.phNumber}">
    <c:set var="action" value="client-no-phone" scope="session"/>
    <c:redirect url="/main/home.jsp"/>
</c:if>
<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/new-request.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.client.new_request"/></title>
</head>
<body>
<my:navBar/>
<form method="post" action="${pageContext.request.contextPath}/controller" class="text-form">
    <input type="hidden" name="command" value="create-request"/>
    <label>
        <fmt:message key="client.new_request.desc.label"/><br/>
        <textarea id="user-input" name="description" class="textarea"
                  placeholder="<fmt:message key="client.new_request.desc.placeholder"/>"></textarea>
        <br/>
    </label>
    <input type="submit" value="<fmt:message key="button.submit"/>" class="submit-button">
</form>
</body>
</html>
