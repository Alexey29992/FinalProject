<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<label>
    <button onclick="setPage(0)" ${!requestScope.hasPrevPage ? 'disabled' : ''}>
        1
    </button>
</label>
<label>
    <button onclick="prevPage()" ${!requestScope.hasPrevPage ? 'disabled' : ''}>
        <
    </button>
</label>
<div class="page-number">
    ${requestScope.page + 1}
</div>
<label>
    <button onclick="nextPage()" ${!requestScope.hasNextPage ? 'disabled' : ''}>
        >
    </button>
</label>