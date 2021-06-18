<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageUri" value="${pageContext.request.contextPath}/main/role-dependent/client/home-nav-bar.jsp"/>
<c:if test="${pageContext.request.requestURI eq pageUri}">
    <c:redirect url="/main/home.jsp"/>
</c:if>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/client/request-form.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.client.new_request"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/client/client-requests.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.client.request_history"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/client/payment-history.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.client.payment_history"/>
    </a>
</li>
<li>
    <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/profile-settings.jsp"/>
    <a href="${link}" ${pageContext.request.requestURI eq link ? 'class="active"' : ''}>
        <fmt:message key="nav_bar.common.profile_settings"/>
    </a>
</li>
<li style="float: right">
    <span>
        <fmt:message key="user.balance"/>: ${sessionScope.user.balance}$
    </span>
</li>


