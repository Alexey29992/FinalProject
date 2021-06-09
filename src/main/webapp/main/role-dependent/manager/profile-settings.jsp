<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/client-settings.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>Profile Settings</title>
</head>
<body>
<my:navBar/>
<form method="post" action="${pageContext.request.contextPath}/controller" class="settings-form">
    <input type="hidden" name="command" value="change-password"/>
    Change password:<br/>
    <label>
        <input type="password" name="old-password" class="input-text-form" placeholder="Old password..."/>
    </label><br/>
    <label>
        <input type="password" name="new-password" class="input-text-form" placeholder="New password..."/>
    </label><br/>
    <input type="submit" value="Submit" class="submit-button">
</form>
<my:error/>
</body>
</html>