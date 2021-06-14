<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<label>
    <button onclick="setPage(0)"
            <c:if test="${!requestScope.hasPrevPage}">
                disabled
            </c:if>>
        1
    </button>
</label>
<label>
    <button onclick="prevPage()"
            <c:if test="${!requestScope.hasPrevPage}">
                disabled
            </c:if>>
        <
    </button>
</label>
<div class="page-number">
    ${requestScope.page + 1}
</div>
<label>
    <button onclick="nextPage()"
            <c:if test="${!requestScope.hasNextPage}">
                disabled
            </c:if>>
        >
    </button>
</label>