<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="content" type="java.lang.String" required="true" %>
<%@ attribute name="buttonStyle" type="java.lang.String" required="true" %>
<%@ attribute name="buttonLable" type="java.lang.String" required="false" %>

<c:if test="${empty requestScope.suffix}">
    <c:set var="suffix" value="0" scope="request"/>
</c:if>
<c:set var="suffix" value="${requestScope.suffix + 1}" scope="request"/>
<c:if test="${not empty content}">
    <button class="${buttonStyle}" onclick="openModal(${requestScope.suffix})">
    <c:choose>
        <c:when test="${not empty buttonLable}">
            ${buttonLable}
            </button>
            <div id="modal-button${requestScope.suffix}" hidden>${content}</div>
        </c:when>
        <c:otherwise>
            <div id="modal-button${requestScope.suffix}">${content}</div>
            </button>
        </c:otherwise>
    </c:choose>
    <div id="modal-box${requestScope.suffix}" class="modal-box">
        <div class="modal-frame">
            <div class="modal-close" onclick="closeModal(${requestScope.suffix})">
                &times;
            </div>
            <pre id="modal-content${requestScope.suffix}" class="modal-content"></pre>
        </div>
    </div>
</c:if>


