<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty param.lang}">
    <c:set var="lang" value="${param.lang}" scope="session"/>
</c:if>
<c:if test="${empty sessionScope.lang}">
    <c:set var="lang" value="${applicationScope.locale}" scope="session"/>
</c:if>
