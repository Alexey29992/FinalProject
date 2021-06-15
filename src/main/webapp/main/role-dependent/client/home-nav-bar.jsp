<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/client/request-form.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            New Request
        </a>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/client/client-requests.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            Request History
        </a>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/client/payment-history.jsp"/>
        <a href="${link}"
                <c:if test="${pageContext.request.requestURI.equals(link)}">
                    class="active"
                </c:if>>
            Payment History
        </a>
    </li>
    <li>
        <c:set var="link" value="${pageContext.request.contextPath}/main/role-dependent/client/profile-settings.jsp"/>
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
    <li style="float: right">
        <span>
            Balance: ${sessionScope.user.balance}$
        </span>
    </li>
</ul>

