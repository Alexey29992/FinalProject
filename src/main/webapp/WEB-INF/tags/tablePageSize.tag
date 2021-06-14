<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="size" value="${pageContext.request.getParameter('size')}"/>
<label>
    <button onclick="setSize(5)"
            <c:if test="${size == '5'}">
                disabled
            </c:if>>
        5
    </button>
</label>
<label>
    <button onclick="setSize(10)"
            <c:if test="${size == '10'}">
                disabled
            </c:if>>
        10
    </button>
</label>
<label>
    <button onclick="setSize(20)"
            <c:if test="${size == '20' || empty size}">
                disabled
            </c:if>>
        20
    </button>
</label>
<label>
    <button onclick="setSize(40)"
            <c:if test="${size == '40'}">
                disabled
            </c:if>>
        40
    </button>
</label>