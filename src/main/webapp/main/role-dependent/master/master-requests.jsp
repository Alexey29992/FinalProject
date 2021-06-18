<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<ralib:onRequest command="get-master-requests"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/master-requests.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.master.assigned_requests"/></title>
    <jsp:include page="/WEB-INF/jspf/pagination-scripts.jspf"/>
    <jsp:include page="/WEB-INF/jspf/modal-box-scripts.jspf"/>
</head>
<body>
<my:navBar/>

<div class="row">
    <div class="column-middle">
        <div class="column-middle-left">
            <form method="get" action="${pageContext.request.requestURI}" id="request-query">
                <input type="hidden" name="command" value="get-master-requests"/>
                <input type="hidden" id="size" name="size" value="${param.size}"/>
                <input type="hidden" id="page" name="page" value="${requestScope.page}"/>
                <div class="dropdown-filters-outer">
                    <div class="filter-status-frame">
                        <label>
                            <fmt:message key="common.filter.status"/>
                            <select name="filter-status" class="select-css" onchange="this.form.submit()">
                                <c:set var="filterStatus" value="${param['filter-status']}"/>
                                <option value="none" ${filterStatus eq 'none' ? 'selected' : ''}>
                                    <fmt:message key="common.none"/>
                                </option>
                                <option value="paid" ${filterStatus eq 'paid' ? 'selected' : ''}>
                                    <fmt:message key="request.status.paid"/>
                                </option>
                                <option value="in-process" ${filterStatus eq 'in-process' ? 'selected' : ''}>
                                    <fmt:message key="request.status.in_process"/>
                                </option>
                                <option value="done" ${filterStatus eq 'done' ? 'selected' : ''}>
                                    <fmt:message key="request.status.done"/>
                                </option>
                            </select>
                        </label>
                    </div>
                    <div class="sort-frame">
                        <label>
                            <fmt:message key="common.sort_label"/>
                            <select name="sort-factor" class="select-css" onchange="this.form.submit()">
                                <c:set var="sortFactor" value="${param['sort-factor']}"/>
                                <option value="id" ${sortFactor eq 'id' ? 'selected' : ''}>
                                    <fmt:message key="common.id"/>
                                </option>
                                <option value="creation-date" ${sortFactor eq 'creation-date' ? 'selected' : ''}>
                                    <fmt:message key="request.creation_date"/>
                                </option>
                                <option value="status" ${sortFactor eq 'status' ? 'selected' : ''}>
                                    <fmt:message key="request.status"/>
                                </option>
                                <option value="price" ${sortFactor eq 'price' ? 'selected' : ''}>
                                    <fmt:message key="request.price"/>
                                </option>
                                <option value="completion-date" ${sortFactor eq 'completion-date' ? 'selected' : ''}>
                                    <fmt:message key="request.completion_date"/>
                                </option>
                            </select>
                        </label>
                    </div>
                </div>
                <div class="order-frame toggle-radio">
                    <my:tableSortOrder/>
                </div>
            </form>
        </div>
        <div class="column-middle-right table-control">
            <div class="page-frame">
                <my:tablePageNav/>
            </div>
            <div class="size-frame">
                <my:tablePageSize/>
            </div>
        </div>
        <table>
            <caption></caption>
            <tr>
                <th scope="col"><fmt:message key="common.id"/></th>
                <th scope="col"><fmt:message key="request.status"/></th>
                <th scope="col"><fmt:message key="request.creation_date"/></th>
                <th scope="col"><fmt:message key="request.completion_date"/></th>
                <th scope="col"><fmt:message key="request.description"/></th>
                <th scope="col"><fmt:message key="request.feedback"/></th>
                <th scope="col"><fmt:message key="common.actions"/></th>
            </tr>
            <c:forEach var="row" items="${requestScope.requests}">
                <tr>
                    <td>${row.id}</td>
                    <td><fmt:message key="request.status.${row.status.toLowerCaseString()}"/></td>
                    <td><ralib:formatDate pattern="dd-MM-yyyy HH:mm:ss" dateTime="${row.creationDate}"/></td>
                    <td><ralib:formatDate pattern="dd-MM-yyyy HH:mm:ss" dateTime="${row.completionDate}"/></td>
                    <td><my:modal content="${row.description}" buttonStyle="table-cell-button btn-left"/></td>
                    <td><my:modal content="${row.userReview}" buttonStyle="table-cell-button btn-left"/></td>
                    <td>
                        <form method="get"
                              action="${pageContext.request.contextPath}/main/role-dependent/master/request-info.jsp">
                            <input type="hidden" name="command" value="get-request">
                            <label>
                                <button class="action-button" name="request-id" value="${row.id}">
                                    <c:choose>
                                        <c:when test="${row.status eq 'DONE'}">
                                            <fmt:message key="button.info"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="button.edit"/>
                                        </c:otherwise>
                                    </c:choose>
                                </button>
                            </label>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>