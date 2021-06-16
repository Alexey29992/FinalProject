<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/info.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>Request Info</title>
    <jsp:include page="/WEB-INF/jspf/modal-box-scripts.jspf"/>
</head>
<body>
<my:navBar/>
<ralib:onRequest/>
<c:if test="${not empty sessionScope.requestId && empty requestScope.currentRequest}">
    <jsp:forward page="/controller?command=get-request&request-id=${sessionScope.requestId}"/>
</c:if>
<div class="global-frame">
    <div class="text-form">
        <form method="get" action="${pageContext.request.requestURI}">
            <input type="hidden" name="command" value="get-request"/>
            <label>
                Find by id:<br/>
                <input type="text" name="request-id" placeholder="Request id..."
                       value="${requestScope.currentRequest.id}"/>
            </label>
            <input type="submit" value="Find">
        </form>
    </div>
    <c:set var="req" value="${requestScope.currentRequest}"/>
    <c:if test="${not empty req}">
        <div class="result">
            <table>
                <tr>
                    <td>Request:</td>
                    <td>#${req.id}</td>
                </tr>
                <tr>
                    <td>Client:</td>
                    <td>${req.clientLogin}#${req.clientId}</td>
                </tr>
                <tr>
                    <td>Master:</td>
                    <td>
                        <c:if test="${req.masterId != 0}">
                            ${req.masterLogin}#${req.masterId}
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Price:</td>
                    <td>
                        <c:if test="${req.price != 0}">
                            ${req.price}
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Creation date:</td>
                    <td>${req.creationDate}</td>
                </tr>
                <tr>
                    <td>Completion date:</td>
                    <td>${req.completionDate}</td>
                </tr>
                <tr>
                    <td>Status:</td>
                    <td>${req.status}</td>
                </tr>
                <tr>
                    <td>Description:</td>
                    <td><my:modal content="${req.description}" buttonStyle="table-cell-button"/></td>
                </tr>
                <tr>
                    <td>Feedback:</td>
                    <td><my:modal content="${req.userReview}" buttonStyle="table-cell-button"/></td>
                </tr>
            </table>
            <c:if test="${req.status.name().equals('PAID') || req.status.name().equals('IN_PROCESS')}">
                <form method="post" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="set-status-master"/>
                    <input type="hidden" name="request-id" value="${req.id}"/>
                    <label>
                        Set status:<br/>
                        <select name="status">
                            <option value="" selected>none</option>
                            <option value="in-process">In process</option>
                            <option value="done">Done</option>
                        </select>
                    </label>
                    <input type="submit" value="Set">
                </form>
            </c:if>
        </div>
    </c:if>
</div>
<my:error/>
</body>
</html>

