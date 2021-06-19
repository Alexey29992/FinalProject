<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<ralib:onRequest/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/create-user.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.admin.create_user"/></title>
</head>
<body>
<my:navBar/>
<div class="text-frame">
    <form method="post" action="${pageContext.request.contextPath}/controller" class="create-form">
        <input type="hidden" name="command" value="create-user"/>
        <table>
            <tr>
                <td><fmt:message key="admin.create_user.login.label"/></td>
                <td>
                    <label>
                        <input type="text" name="login"
                               placeholder="<fmt:message key="admin.create_user.login.placeholder"/>"/>
                    </label>
                </td>
            </tr>
            <tr>
                <td><fmt:message key="admin.create_user.password.label"/></td>
                <td>
                    <label>
                        <input type="password" name="password"
                               placeholder="<fmt:message key="admin.create_user.password.placeholder"/>"/>
                    </label>
                </td>
            </tr>
            <tr>
                <td><fmt:message key="user.role"/>:</td>
                <td>
                    <label>
                        <select name="role">
                            <option value="" selected><fmt:message key="common.none"/></option>
                            <option value="client"><fmt:message key="user.role.client"/></option>
                            <option value="master"><fmt:message key="user.role.master"/></option>
                            <option value="manager"><fmt:message key="user.role.manager"/></option>
                        </select>
                    </label>
                </td>
            </tr>
        </table>
        <input type="submit" value="<fmt:message key="button.create"/>">
    </form>
</div>
<my:error/>
</body>
</html>

