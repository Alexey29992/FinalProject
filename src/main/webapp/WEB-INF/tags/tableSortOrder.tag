<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="sortOrder" value="${pageContext.request.getParameter('sort-order')}"/>
<input type="radio" id="desc" name="sort-order" value="desc" onclick="this.form.submit()"
        <c:if test="${sortOrder == 'desc' || empty sortOrder}">
            checked
        </c:if>/>
<label for="desc">DSC:</label>
<input type="radio" id="asc" name="sort-order" value="asc" onclick="this.form.submit()"
        <c:if test="${sortOrder == 'asc'}">
            checked
        </c:if>/>
<label for="asc">ASC:</label>