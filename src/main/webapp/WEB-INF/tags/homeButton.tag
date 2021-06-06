<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="homeRef" scope="request" value="${pageContext.request.contextPath}/main/home.jsp"/>
<c:if test="${pageContext.request.requestURI != homeRef}">
    <a href="${homeRef}" class="back-button">
        <button class="medium-text">
            <img src="${pageContext.request.contextPath}/resources/back.png" class="button-img" alt="enter-button"/>
            Home
        </button>
    </a>
</c:if>

