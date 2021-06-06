<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty sessionScope.user}">
    <a href="${pageContext.request.contextPath}/main/logout.jsp" class="log-in-out-button">
        <button class="medium-text">
            <img src="${pageContext.request.contextPath}/resources/out.png" class="button-img" alt="out-button"/>
            Log out
        </button>
    </a>
</c:if>
<c:if test="${empty sessionScope.user}">
    <a href="${pageContext.request.contextPath}/main/login.jsp" class="log-in-out-button">
        <button class="medium-text">
            <img src="${pageContext.request.contextPath}/resources/enter.png" class="button-img" alt="enter-button"/>
            Log in
        </button>
    </a>
</c:if>
