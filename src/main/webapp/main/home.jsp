<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/home.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.common.home"/></title>
</head>
<body>
<my:navBar/>
<ralib:processAction var="actionMessage"/>
<c:if test="${not empty actionMessage}">
    <h3 class="home-form">
        <fmt:message key="${actionMessage}"/>
    </h3>
</c:if>
</body>