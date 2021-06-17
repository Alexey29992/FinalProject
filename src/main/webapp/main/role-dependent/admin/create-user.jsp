<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ralib:onRequest/>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/create-user.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>Create User</title>
</head>
<body>
<my:navBar/>

<div class="text-frame">
    <form method="post" action="${pageContext.request.contextPath}/controller">
        <input type="hidden" name="command" value="create-user"/>
        <table>
            <tr>
                <td>Login:</td>
                <td>
                    <label>
                        <input type="text" name="login" placeholder="Login..."/>
                    </label>
                </td>
            </tr>
            <tr>
                <td>Password:</td>
                <td>
                    <label>
                        <input type="password" name="password" placeholder="Password..."/>
                    </label>
                </td>
            </tr>
            <tr>
                <td>Role:</td>
                <td>
                    <label>
                        <select name="role">
                            <option value="" selected>none</option>
                            <option value="client">Client</option>
                            <option value="master">Master</option>
                            <option value="manager">Manager</option>
                        </select>
                    </label>
                </td>
            </tr>
        </table>
        <input type="submit" value="Create">
    </form>
</div>
<my:error/>
</body>
</html>

