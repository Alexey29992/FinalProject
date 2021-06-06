<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/client-requests.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>Request History</title>
    <script>
        function processCheckbox() {
            let x = document.getElementById("desc");
            if (x.value === "desc") {
                x.value = "asc";
            } else {
                x.value = "desc";
            }

        }
        window.onload = function () {
            document.getElementById("${pageContext.request.getParameter("filter-status")}").selected = true;
            document.getElementById("${pageContext.request.getParameter("sorting-factor")}").selected = true;
            document.getElementById("${pageContext.request.getParameter("sorting-order")}").checked = true;
        }
        <c:if test="${requestScope.requestList == null}">
        window.onload = function () {
            document.getElementById("request-query").submit();
        }
        </c:if>
    </script>
</head>
<body>
<my:navBar/>
${pageContext.request.getParameter("sorting-order")}
<form method="get" action="${pageContext.request.contextPath}/controller" class="settings-form" id="request-query">
    <input type="hidden" name="command" value="get-request-list">
    <label>
        Filter by status:
        <select name="filter-status" onchange="this.form.submit()">
            <option id="none" value="NONE">none</option>
            <option id="new" value="NEW">New</option>
            <option id="wait-for-payment" value="WAIT_FOR_PAYMENT">Wait for payment</option>
            <option id="paid" value="PAID">Paid</option>
            <option id="cancelled" value="CANCELLED">Cancelled</option>
            <option id="in-process" value="IN_PROCESS">In process</option>
            <option id="done" value="DONE">Done</option>
        </select>
    </label>
    <label>
        Sort by:
        <select name="sorting-factor" onchange="this.form.submit()">
            <option id="id" value="id">ID</option>
            <option id="creation-date" value="creation_date">Creation Date</option>
            <option id="status" value="status">Status</option>
            <option id="price" value="price">Price</option>
            <option id="completion-date" value="completion_date">Completion Date</option>
        </select>
    </label>
    <label>
        <input type="checkbox" id="desc" name="sorting-order" value="desc" onclick="processCheckbox()"/>
    </label>
    <label>
        <button name="next-page" value="-1">
            <
        </button>
    </label>
    ${requestScope.currentPage + 1}
    <label>
        <button name="next-page" value="1">
            >
        </button>
    </label>
    <label>
        <button name="next-page" value="first">
            to first
        </button>
    </label>
    Rows per page:
    <label>
        <button name="rows-per-page" value="5">
            5
        </button>
    </label>
    <label>
        <button name="rows-per-page" value="10">
            10
        </button>
    </label>
    <label>
        <button name="rows-per-page" value="20">
            20
        </button>
    </label>
    <label>
        <button name="rows-per-page" value="40">
            40
        </button>
    </label>
</form>
<table>
    <tr>
        <th>Id</th>
        <th>Creation Date</th>
        <th>Status</th>
        <th>Description</th>
        <th>Price</th>
        <th>Completion Date</th>
        <th>Feedback</th>
    </tr>
    <c:forEach var="row" items="${requestScope.requestList}">
        <tr>
            <td>${row.id}</td>
            <td>${row.creationDate}</td>
            <td>${row.status}</td>
            <td>${row.description}</td>
            <td>${row.price}</td>
            <td>${row.completionDate}</td>
            <td>${row.userReview}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
