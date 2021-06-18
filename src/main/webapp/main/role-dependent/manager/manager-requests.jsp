<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/manager-requests.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.manager.request_management"/></title>
    <jsp:include page="/WEB-INF/jspf/pagination-scripts.jspf"/>
    <jsp:include page="/WEB-INF/jspf/modal-box-scripts.jspf"/>
</head>
<body>
<my:navBar/>
<ralib:onRequest command="get-manager-requests"/>
<div class="row">
    <div class="column-middle">
        <div class="column-middle-left">
            <form method="get" action="${pageContext.request.requestURI}" id="request-query">
                <input type="hidden" name="command" value="get-manager-requests"/>
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
                                <option value="new" ${filterStatus eq 'new' ? 'selected' : ''}>
                                    <fmt:message key="request.status.new"/>
                                </option>
                                <option value="wait-for-payment" ${filterStatus eq 'wait-for-payment' ? 'selected' : ''}>
                                    <fmt:message key="request.status.wait_for_payment"/>
                                </option>
                                <option value="paid" ${filterStatus eq 'paid' ? 'selected' : ''}>
                                    <fmt:message key="request.status.paid"/>
                                </option>
                                <option value="cancelled" ${filterStatus eq 'cancelled' ? 'selected' : ''}>
                                    <fmt:message key="request.status.cancelled"/>
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
                <div class="text-filters-outer">
                    <div class="text-filters">
                        <div class="filter-master-frame">
                            <label>
                                <fmt:message key="manager.request_table.filter.master"/>
                                <input type="text" name="filter-master"
                                       placeholder="<fmt:message key="manager.request_table.filter.master.placeholder"/>"
                                       value="${param['filter-master']}"/>
                            </label>
                        </div>
                        <div class="filter-client-frame">
                            <label>
                                <fmt:message key="manager.request_table.filter.client"/>
                                <input type="text" name="filter-client"
                                       placeholder="<fmt:message key="manager.request_table.filter.client.placeholder"/>"
                                       value="${param['filter-client']}"/>
                            </label>
                        </div>
                    </div>
                    <div class="refresh-button-frame">
                        <label>
                            <button class="refresh-button">
                                <img src="${pageContext.request.contextPath}/resources/refresh.png" alt="refresh"/>
                            </button>
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
                <th scope="col"><fmt:message key="request.client"/></th>
                <th scope="col"><fmt:message key="request.master"/></th>
                <th scope="col"><fmt:message key="request.price"/></th>
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
                    <td>
                        <form method="get"
                              action="${pageContext.request.contextPath}/main/role-dependent/manager/user-info.jsp">
                            <input type="hidden" name="command" value="get-user">
                            <label>
                                <button class="table-cell-button" name="user-id" value="${row.clientId}">
                                        ${row.clientLogin}
                                </button>
                            </label>
                        </form>
                    </td>
                    <td>
                        <c:if test="${row.masterId != 0}">
                            <form method="get"
                                  action="${pageContext.request.contextPath}/main/role-dependent/manager/user-info.jsp">
                                <input type="hidden" name="command" value="get-user">
                                <label>
                                    <button class="table-cell-button" name="user-id" value="${row.masterId}">
                                            ${row.masterLogin}
                                    </button>
                                </label>
                            </form>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${row.price > 0}">
                            ${row.price}$
                        </c:if>
                    </td>
                    <td>
                        <fmt:message key="request.status.${row.status.toLowerCaseString()}" var="localizedStatus"/>
                        <c:choose>
                            <c:when test="${row.status eq 'CANCELLED' && not empty row.cancelReason}">
                                <my:modal content="${row.cancelReason}"
                                          buttonLable="${localizedStatus}"
                                          buttonStyle="table-cell-button"/>
                            </c:when>
                            <c:otherwise>
                                ${localizedStatus}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><ralib:formatDate pattern="dd-MM-yyyy HH:mm:ss" dateTime="${row.creationDate}"/></td>
                    <td><ralib:formatDate pattern="dd-MM-yyyy HH:mm:ss" dateTime="${row.completionDate}"/></td>
                    <td><my:modal content="${row.description}" buttonStyle="table-cell-button btn-left"/></td>
                    <td><my:modal content="${row.userReview}" buttonStyle="table-cell-button btn-left"/></td>
                    <td>
                        <form method="get"
                              action="${pageContext.request.contextPath}/main/role-dependent/manager/request-info.jsp">
                            <input type="hidden" name="command" value="get-request">
                            <label>
                                <button class="action-button" name="request-id" value="${row.id}">
                                    <fmt:message key="button.edit"/>
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