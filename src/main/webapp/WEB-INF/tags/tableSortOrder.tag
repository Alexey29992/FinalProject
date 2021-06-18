<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="sortOrder" value="${param['sort-order']}"/>
<input type="radio" id="desc" name="sort-order" value="desc"
       onclick="this.form.submit()" ${sortOrder eq 'desc' || empty sortOrder ? 'checked' : ''}>
<label for="desc">DSC</label>
<input type="radio" id="asc" name="sort-order" value="asc"
       onclick="this.form.submit()" ${sortOrder eq 'asc' ? 'checked' : ''}>
<label for="asc">ASC</label>