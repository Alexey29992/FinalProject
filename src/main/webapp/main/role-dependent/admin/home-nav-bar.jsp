<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageUri" value="${pageContext.request.contextPath}/main/role-dependent/admin/home-nav-bar.jsp"/>
<c:if test="${pageContext.request.requestURI eq pageUri}">
    <c:redirect url="/main/home.jsp"/>
</c:if>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/manager/manager-requests.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.manager.request_table"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/manager/request-info.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.manager.request_info"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/admin/admin-users.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.admin.user_table"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/manager/user-info.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.manager.user_info"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/admin/create-user.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.admin.create_user"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/profile-settings.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.common.profile_settings"/>
    </a>
</li>

