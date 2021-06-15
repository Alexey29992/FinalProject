<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/manager/manager-requests.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            Request Management
        </a>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/manager/user-info.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            User Info
        </a>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/manager/request-info.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            Request Info
        </a>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/manager/profile-settings.jsp"/>
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