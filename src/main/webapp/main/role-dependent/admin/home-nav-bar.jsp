<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul>
    <li class="dropdown">
        <c:set var="link1" value="${pageContext.request.contextPath}/main/role-dependent/manager/manager-requests.jsp"/>
        <c:set var="link2" value="${pageContext.request.contextPath}/main/role-dependent/manager/request-info.jsp"/>
        <span class="drop-btn
                <c:if test="${pageContext.request.requestURI.equals(link1)
                           || pageContext.request.requestURI.equals(link2)}">
                    active
                </c:if>">
            Request Management
        </span>
        <div class="dropdown-content">
            <a href="${link1}">Request Table</a>
            <a href="${link2}">Request Info</a>
        </div>
    </li>
    <li class="dropdown">
        <c:set var="link1" value="${pageContext.request.contextPath}/main/role-dependent/admin/admin-users.jsp"/>
        <c:set var="link2" value="${pageContext.request.contextPath}/main/role-dependent/manager/user-info.jsp"/>
        <c:set var="link3" value="${pageContext.request.contextPath}/main/role-dependent/admin/create-user.jsp"/>
        <span class="drop-btn
                <c:if test="${pageContext.request.requestURI.equals(link1)
                           || pageContext.request.requestURI.equals(link2)
                           || pageContext.request.requestURI.equals(link3)}">
                    active
                </c:if>">
            User Management
        </span>
        <div class="dropdown-content">
            <a href="${link1}">User Table</a>
            <a href="${link2}">User Info</a>
            <a href="${link3}">Create User</a>
        </div>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/profile-settings.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            Profile Settings
        </a>
    </li>
    <li style="float: right">
        <span>
            ${sessionScope.user.role}: ${sessionScope.user.login}
        </span>
    </li>
</ul>