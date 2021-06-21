<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<ralib:onRequest command="get-admin-users"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/admin-users.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.admin.user_management"/></title>
    <jsp:include page="/WEB-INF/jspf/pagination-scripts.jspf"/>
    <jsp:include page="/WEB-INF/jspf/modal-box-scripts.jspf"/>
</head>
<body>
<my:navBar/>
<div class="row">
    <div class="column-middle">
        <div class="column-middle-left">
            <form method="get" action="${pageContext.request.requestURI}" id="request-query">
                <input type="hidden" name="command" value="get-admin-users"/>
                <input type="hidden" id="size" name="size" value="${param.size}"/>
                <input type="hidden" id="page" name="page" value="${requestScope.page}"/>
                <div class="dropdown-filters-outer">
                    <div class="filter-role-frame">
                        <label>
                            <fmt:message key="common.filter.role"/>
                            <select name="filter-role" class="select-css" onchange="this.form.submit()">
                                <c:set var="filter" value="${param['filter-role']}"/>
                                <option value="none" ${filter eq 'none' ? 'selected' : ''}>
                                    <fmt:message key="common.none"/>
                                </option>
                                <option value="client" ${filter eq 'client' ? 'selected' : ''}>
                                    <fmt:message key="user.role.client"/>
                                </option>
                                <option value="master" ${filter eq 'master' ? 'selected' : ''}>
                                    <fmt:message key="user.role.master"/>
                                </option>
                                <option value="manager" ${filter eq 'manager' ? 'selected' : ''}>
                                    <fmt:message key="user.role.manager"/>
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
                                <option value="login" ${sortFactor eq 'login' ? 'selected' : ''}>
                                    <fmt:message key="user.login"/>
                                </option>
                                <option value="role" ${sortFactor eq 'id' ? 'selected' : ''}>
                                    <fmt:message key="user.role"/>
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
                <th scope="col"><fmt:message key="user.login"/></th>
                <th scope="col"><fmt:message key="user.role"/></th>
                <th scope="col"><fmt:message key="user.ph_number"/></th>
                <th scope="col"><fmt:message key="user.balance"/></th>
                <th scope="col"><fmt:message key="common.actions"/></th>
            </tr>
            <c:forEach var="row" items="${requestScope.users}">
                <c:if test="${row.role != 'ADMIN'}">
                    <tr>
                        <td>${row.id}</td>
                        <td>
                            <form method="get"
                                  action="${pageContext.request.contextPath}/main/role-dependent/manager/user-info.jsp">
                                <input type="hidden" name="command" value="get-user">
                                <label>
                                    <button class="table-cell-button" name="user-id" value="${row.id}">
                                            ${row.login}
                                    </button>
                                </label>
                            </form>
                        </td>
                        <td><fmt:message key="user.role.${row.role.toLowerCaseString()}"/></td>
                        <td>${row.role eq 'CLIENT' ? row.phNumber : ''}</td>
                        <td>${row.role eq 'CLIENT' ? row.balance : ''}</td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/controller">
                                <input type="hidden" name="command" value="remove-user">
                                <label>
                                    <button class="action-button delete-button" name="user-id" value="${row.id}">
                                        <fmt:message key="button.delete"/>
                                    </button>
                                </label>
                            </form>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>