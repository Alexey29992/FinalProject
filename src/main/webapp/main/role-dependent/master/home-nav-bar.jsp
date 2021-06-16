<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/master/master-requests.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            Assigned Requests
        </a>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/master/request-info.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            Request Info
        </a>
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