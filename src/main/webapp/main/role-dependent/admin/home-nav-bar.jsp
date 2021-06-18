<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageUri" value="${pageContext.request.contextPath}/main/role-dependent/admin/home-nav-bar.jsp"/>
<c:if test="${pageContext.request.requestURI eq pageUri}">
    <c:redirect url="/main/home.jsp"/>
</c:if>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<li class="dropdown">
    <c:set var="link1" value="${pageContext.request.contextPath}/main/role-dependent/manager/manager-requests.jsp"/>
    <c:set var="link2" value="${pageContext.request.contextPath}/main/role-dependent/manager/request-info.jsp"/>
    <span class="drop-btn ${pageContext.request.requestURI eq link1 ||
                            pageContext.request.requestURI eq link2 ? 'active' : ''}">
        <fmt:message key="nav_bar.manager.request_management"/>
    </span>
    <div class="dropdown-content">
        <a href="${link1}"><fmt:message key="nav_bar.manager.request_table"/></a>
        <a href="${link2}"><fmt:message key="nav_bar.manager.request_info"/></a>
    </div>
</li>
<li class="dropdown">
    <c:set var="link1" value="${pageContext.request.contextPath}/main/role-dependent/admin/admin-users.jsp"/>
    <c:set var="link2" value="${pageContext.request.contextPath}/main/role-dependent/manager/user-info.jsp"/>
    <c:set var="link3" value="${pageContext.request.contextPath}/main/role-dependent/admin/create-user.jsp"/>
    <span class="drop-btn ${pageContext.request.requestURI eq link1 ||
                            pageContext.request.requestURI eq link2 ||
                            pageContext.request.requestURI eq link3 ? 'active' : ''}">
        <fmt:message key="nav_bar.admin.user_management"/>
    </span>
    <div class="dropdown-content">
        <a href="${link1}"><fmt:message key="nav_bar.admin.user_table"/></a>
        <a href="${link2}"><fmt:message key="nav_bar.manager.user_info"/></a>
        <a href="${link3}"><fmt:message key="nav_bar.admin.create_user"/></a>
    </div>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/profile-settings.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.common.profile_settings"/>
    </a>
</li>

