<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/settings.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.common.profile_settings"/></title>
</head>
<body>
<my:navBar/>
<form method="post" action="${pageContext.request.contextPath}/controller" class="settings-form">
    <input type="hidden" name="command" value="change-password"/>
    <fmt:message key="profile_settings.change_password.label"/><br/>
    <label>
        <input type="password" name="old-password" class="input-text-form"
               placeholder="<fmt:message key="profile_settings.change_password.old.placeholder"/>"/>
    </label><br/>
    <label>
        <input type="password" name="new-password" class="input-text-form"
               placeholder="<fmt:message key="profile_settings.change_password.new.placeholder"/>"/>
    </label><br/>
    <input type="submit" value="<fmt:message key="button.submit"/>" class="submit-button">
</form>
<c:if test="${sessionScope.user.role eq 'CLIENT'}">
    <form method="post" action="${pageContext.request.contextPath}/controller" class="settings-form">
        <input type="hidden" name="command" value="change-client-settings"/>
        <fmt:message key="profile_settings.phone_number.label"/><br/>
        <label>
            <input type="text" name="ph-number" class="input-text-form"
                   value="${sessionScope.user.phNumber}"
                   placeholder="<fmt:message key="profile_settings.phone_number.placeholder"/>"/>
        </label><br/>
        <input type="submit" value="<fmt:message key="button.submit"/>" class="submit-button">
    </form>
</c:if>
<my:error/>
</body>
</html>