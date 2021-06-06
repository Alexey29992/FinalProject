<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/home.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>Home page</title>
</head>
<body>
<my:navBar/>
<article class="home-form">
    <ralib:processAction/>
</article>
<%--
<div class="home-img-pos">
    <img src="${pageContext.request.contextPath}/resources/home.png" class="home-img" alt="out-button"/>
</div>
--%>
</body>