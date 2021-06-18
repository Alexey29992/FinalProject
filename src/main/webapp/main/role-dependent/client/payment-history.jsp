<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<ralib:onRequest command="get-payment-records"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/client-payments.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.client.payment_history"/></title>
    <jsp:include page="/WEB-INF/jspf/pagination-scripts.jspf"/>
</head>
<body>
<my:navBar/>

<div class="row">
    <div class="column-middle">
        <div class="column-middle-left">
            <form method="get" action="${pageContext.request.requestURI}" id="request-query">
                <input type="hidden" name="command" value="get-payment-records"/>
                <input type="hidden" id="size" name="size" value="${param.size}"/>
                <input type="hidden" id="page" name="page" value="${requestScope.page}"/>
                <div class="sort-frame">
                    <label>
                        <fmt:message key="common.sort_label"/>
                        <select name="sort-factor" class="select-css" onchange="this.form.submit()">
                            <c:set var="sortFactor" value="${param['sort-factor']}"/>
                            <option value="id" ${sortFactor eq 'id' ? 'selected' : ''}>
                                <fmt:message key="common.id"/>
                            </option>
                            <option value="date" ${sortFactor eq 'date' ? 'selected' : ''}>
                                <fmt:message key="client.payment_history.date"/>
                            </option>
                            <option value="sum" ${sortFactor eq 'sum' ? 'selected' : ''}>
                                <fmt:message key="client.payment_history.sum"/>
                            </option>
                        </select>
                    </label>
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
        <table class="req-table">
            <caption></caption>
            <tr>
                <th scope="col"><fmt:message key="common.id"/></th>
                <th scope="col"><fmt:message key="client.payment_history.sum"/></th>
                <th scope="col"><fmt:message key="client.payment_history.date"/></th>
                <th scope="col"><fmt:message key="client.payment_history.destination"/></th>
            </tr>
            <c:forEach var="row" items="${requestScope.paymentRecords}">
                <tr>
                    <td>${row.id}</td>
                    <td>${row.sum}$</td>
                    <td><ralib:formatDate pattern="dd-MM-yyyy HH:mm:ss" dateTime="${row.date}"/></td>
                    <td>${row.destination}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>