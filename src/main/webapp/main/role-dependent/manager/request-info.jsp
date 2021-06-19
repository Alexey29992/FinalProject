<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:langSwitcher/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n"/>
<ralib:onRequest/>
<c:if test="${not empty sessionScope.requestId && empty requestScope.currentRequest}">
    <jsp:forward page="/controller">
        <jsp:param name="command" value="get-request"/>
        <jsp:param name="request-id" value="${sessionScope.requestId}"/>
    </jsp:forward>
</c:if>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/info.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title><fmt:message key="nav_bar.manager.request_info"/></title>
    <script>
        function hideCancelArea(x) {
            document.getElementById('cancel-area').hidden = x;
        }
    </script>
    <jsp:include page="/WEB-INF/jspf/modal-box-scripts.jspf"/>
</head>
<body>
<my:navBar/>
<div class="global-frame">
    <div class="text-form">
        <form method="get" action="${pageContext.request.requestURI}">
            <input type="hidden" name="command" value="get-request"/>
            <label>
                <fmt:message key="manager.request_info.find_id.label"/><br/>
                <input type="text" name="request-id"
                       placeholder="<fmt:message key="manager.request_info.find_id.placeholder"/>"
                       value="${requestScope.currentRequest.id}"/>
            </label>
            <input type="submit" value="<fmt:message key="button.find"/>">
        </form>
    </div>
    <c:set var="req" value="${requestScope.currentRequest}"/>
    <c:if test="${not empty req}">
        <div class="result">
            <table>
                <tr>
                    <td><fmt:message key="request.label"/>:</td>
                    <td>#${req.id}</td>
                </tr>
                <tr>
                    <td><fmt:message key="request.client"/>:</td>
                    <td>${req.clientLogin}#${req.clientId}</td>
                </tr>
                <tr>
                    <td><fmt:message key="request.master"/>:</td>
                    <td>
                        <c:if test="${req.masterId != 0}">
                            ${req.masterLogin}#${req.masterId}
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="request.price"/>:</td>
                    <td>
                        <c:if test="${req.price != 0}">
                            ${req.price}$
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="request.creation_date"/>:</td>
                    <td><ralib:formatDate pattern="dd-MM-yyyy HH:mm:ss" dateTime="${req.creationDate}"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="request.completion_date"/>:</td>
                    <td><ralib:formatDate pattern="dd-MM-yyyy HH:mm:ss" dateTime="${req.completionDate}"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="request.status"/>:</td>
                    <td><fmt:message key="request.status.${req.status.toLowerCaseString()}"/></td>
                </tr>
                <c:if test="${req.status eq 'CANCELLED'}">
                    <tr>
                        <td><fmt:message key="request.cancel_reason"/>:</td>
                        <td><my:modal content="${req.cancelReason}" buttonStyle="table-cell-button"/></td>
                    </tr>
                </c:if>
                <tr>
                    <td><fmt:message key="request.description"/>:</td>
                    <td><my:modal content="${req.description}" buttonStyle="table-cell-button"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="request.feedback"/>:</td>
                    <td><my:modal content="${req.userReview}" buttonStyle="table-cell-button"/></td>
                </tr>
            </table>
            <c:if test="${req.status eq 'PAID'}">
                <form method="post" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="set-master"/>
                    <input type="hidden" name="request-id" value="${req.id}"/>
                    <label>
                        <fmt:message key="manager.request_info.assign_master"/><br/>
                        <select name="master-id">
                            <option value="" selected><fmt:message key="common.none"/></option>
                            <c:forEach var="master" items="${applicationScope.masterMap}">
                                <option value="${master.key}" ${req.masterId eq master.key ? 'selected' : ''}>
                                        ${master.value}
                                </option>
                            </c:forEach>
                        </select>
                    </label>
                    <input type="submit" value="<fmt:message key="button.assign"/>">
                </form>
            </c:if>
            <c:if test="${req.status eq 'NEW'}">
                <form method="post" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="set-price"/>
                    <input type="hidden" name="request-id" value="${req.id}"/>
                    <label>
                        <fmt:message key="manager.request_info.set_price.label"/><br/>
                        <input type="text" name="price"
                               placeholder="<fmt:message key="manager.request_info.set_price.placeholder"/>"/>
                    </label>
                    <input type="submit" value="<fmt:message key="button.set"/>">
                </form>
            </c:if>
            <c:if test="${req.status eq 'NEW'
                        || req.status eq 'WAIT_FOR_PAYMENT'
                        || sessionScope.user.role eq 'ADMIN'}">
                <form method="post" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="set-status-manager"/>
                    <input type="hidden" name="request-id" value="${req.id}"/>
                    <label>
                        <fmt:message key="manager.request_info.set_status"/><br/>
                        <select name="status">
                            <option value="new"
                                ${req.status eq 'NEW' ? 'selected' : ''}
                                    onclick="hideCancelArea(true)">
                                <fmt:message key="request.status.new"/>
                            </option>
                            <option value="wait-for-payment"
                                ${req.status eq 'WAIT_FOR_PAYMENT' ? 'selected' : ''}
                                    onclick="hideCancelArea(true)">
                                <fmt:message key="request.status.wait_for_payment"/>
                            </option>
                            <option value="paid"
                                ${req.status eq 'PAID' ? 'selected' : ''}
                                    onclick="hideCancelArea(true)">
                                <fmt:message key="request.status.paid"/>
                            </option>
                            <option value="cancelled"
                                ${req.status eq 'CANCELLED' ? 'selected' : ''}
                                    onclick="hideCancelArea(false)">
                                <fmt:message key="request.status.cancelled"/>
                            </option>
                            <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                                <option value="in-process"
                                    ${req.status eq 'IN_PROCESS' ? 'selected' : ''}
                                        onclick="hideCancelArea(true)">
                                    <fmt:message key="request.status.in_process"/>
                                </option>
                                <option value="done"
                                    ${req.status eq 'DONE' ? 'selected' : ''}
                                        onclick="hideCancelArea(true)">
                                    <fmt:message key="request.status.done"/>
                                </option>
                            </c:if>
                        </select>
                    </label>
                    <input type="submit" value="<fmt:message key="button.set"/>">
                    <label>
                        <textarea id="cancel-area" name="cancel-reason"
                                  placeholder="<fmt:message key="manager.request_info.cancel_reason.placeholder"/>"
                                  hidden></textarea>
                    </label>
                </form>
            </c:if>
        </div>
    </c:if>
</div>
<my:error/>
</body>
</html>

