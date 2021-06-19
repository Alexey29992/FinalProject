<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>

<c:if test="${not empty sessionScope.error}">
    <div class="error">
        <img src="${pageContext.request.contextPath}/resources/error.png" alt="error-sign" class="error-img"/>
        <br/>
            <fmt:message key="${sessionScope.error}"/>
        <c:remove var="error" scope="session"/>
    </div>
</c:if>