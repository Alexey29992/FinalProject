<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty sessionScope.error}">
    <div class="error">
        <img src="${pageContext.request.contextPath}/resources/error.png" alt="error-sign" class="error-img"/>
        <br/>
            ${sessionScope.error}
        <c:remove var="error" scope="session"/>
    </div>
</c:if>