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
        function submitForm() {
            document.getElementById("request-query").submit();
        }

        function setPage(x) {
            document.getElementById("page").value = x;
            submitForm();
        }

        function nextPage() {
            let page = document.getElementById("page").value;
            page++;
            document.getElementById("page").value = page;
            submitForm();
        }

        function prevPage() {
            let page = document.getElementById("page").value;
            page--;
            document.getElementById("page").value = page;
            submitForm();
        }

        function setSize(x) {
            document.getElementById("size").value = x;
            document.getElementById("page").value = 0;
            submitForm();
        }

        <c:if test="${requestScope.requestList == null}">
        window.onload = function () {
            submitForm();
        }
        </c:if>
    </script>
    <jsp:include page="/WEB-INF/jspf/modal-box-scripts.jspf"/>
</head>
<body>
<my:navBar/>

<c:if test="${not empty pageContext.request.getParameter('command')}">
    <c:if test="${empty requestScope.forwarded}">
        <jsp:forward page="/controller"/>
    </c:if>
</c:if>
<div class="row">
    <div class="column-middle">
        <div class="column-middle-left">
            <form method="get" action="${pageContext.request.requestURI}" id="request-query">
                <input type="hidden" name="command" value="get-client-requests"/>
                <input type="hidden" id="size" name="size" value="${pageContext.request.getParameter('size')}"/>
                <input type="hidden" id="page" name="page" value="${pageContext.request.getAttribute('page')}"/>
                <div class="filter-frame">
                    <label>
                        Filter by status:
                        <select name="filter-status" class="select-css" onchange="this.form.submit()">
                            <c:set var="filterStatus" value="${pageContext.request.getParameter('filter-status')}"/>
                            <option value="none"
                                    <c:if test="${filterStatus == 'none'}">
                                        selected
                                    </c:if>>
                                none
                            </option>
                            <option value="new"
                                    <c:if test="${filterStatus == 'new'}">
                                        selected
                                    </c:if>>
                                New
                            </option>
                            <option value="wait-for-payment"
                                    <c:if test="${filterStatus == 'wait-for-payment'}">
                                        selected
                                    </c:if>>
                                Wait for payment
                            </option>
                            <option value="paid"
                                    <c:if test="${filterStatus == 'paid'}">
                                        selected
                                    </c:if>>
                                Paid
                            </option>
                            <option value="cancelled"
                                    <c:if test="${filterStatus == 'cancelled'}">
                                        selected
                                    </c:if>>
                                Cancelled
                            </option>
                            <option value="in-process"
                                    <c:if test="${filterStatus == 'in-process'}">
                                        selected
                                    </c:if>>
                                In process
                            </option>
                            <option value="done"
                                    <c:if test="${filterStatus == 'done'}">
                                        selected
                                    </c:if>>
                                Done
                            </option>
                        </select>
                    </label>
                </div>
                <div class="sort-frame">
                    <label>
                        Sort by:
                        <select name="sort-factor" class="select-css" onchange="this.form.submit()">
                            <c:set var="sortFactor" value="${pageContext.request.getParameter('sort-factor')}"/>
                            <option value="id"
                                    <c:if test="${sortFactor == 'id'}">
                                        selected
                                    </c:if>>
                                ID
                            </option>
                            <option value="creation-date"
                                    <c:if test="${sortFactor == 'creation-date'}">
                                        selected
                                    </c:if>>
                                Creation Date
                            </option>
                            <option value="status"
                                    <c:if test="${sortFactor == 'status'}">
                                        selected
                                    </c:if>>
                                Status
                            </option>
                            <option value="price"
                                    <c:if test="${sortFactor == 'price'}">
                                        selected
                                    </c:if>>
                                Price
                            </option>
                            <option value="completion-date"
                                    <c:if test="${sortFactor == 'completion-date'}">
                                        selected
                                    </c:if>>
                                Completion Date
                            </option>
                        </select>
                    </label>
                </div>
                <div class="order-frame toggle-radio">
                    <c:set var="sortOrder" value="${pageContext.request.getParameter('sort-order')}"/>
                    <input type="radio" id="desc" name="sort-order" value="desc" onclick="this.form.submit()"
                            <c:if test="${sortOrder == 'desc' || sortOrder == null}">
                                checked
                            </c:if>/>
                    <label for="desc">DSC:</label>
                    <input type="radio" id="asc" name="sort-order" value="asc" onclick="this.form.submit()"
                            <c:if test="${sortOrder == 'asc'}">
                                checked
                            </c:if>/>
                    <label for="asc">АSC:</label>
                </div>
            </form>
        </div>
        <div class="column-middle-right table-control">
            <div class="page-frame">
                <label>
                    <button onclick="setPage(0)">
                        1
                    </button>
                </label>
                <label>
                    <button onclick="prevPage()"
                            <c:if test="${!requestScope.hasPrevPage}">
                                disabled
                            </c:if>>
                        <
                    </button>
                </label>
                <div class="page-number">
                    ${requestScope.page + 1}
                </div>
                <label>
                    <button onclick="nextPage()"
                            <c:if test="${!requestScope.hasNextPage}">
                                disabled
                            </c:if>>
                        >
                    </button>
                </label>
            </div>
            <div class="size-frame">
                <label>
                    <button onclick="setSize(5)">
                        5
                    </button>
                </label>
                <label>
                    <button onclick="setSize(10)">
                        10
                    </button>
                </label>
                <label>
                    <button onclick="setSize(20)">
                        20
                    </button>
                </label>
                <label>
                    <button onclick="setSize(40)">
                        40
                    </button>
                </label>
            </div>
        </div>
        <table class="req-table">
            <caption>Request history:</caption>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">Price</th>
                <th scope="col">Status</th>
                <th scope="col">Creation Date</th>
                <th scope="col">Completion Date</th>
                <th scope="col">Description</th>
                <th scope="col">Feedback</th>
            </tr>
            <c:forEach var="row" items="${requestScope.requestList}">
                <tr>
                    <td>${row.id}</td>
                    <td>
                        <c:if test="${row.price > 0}">
                            <form method="post" action="controller">
                                <input type="hidden" name="command" value="make-payment"/>
                                <input type="hidden" name="request-id" value="${row.id}"/>
                                <button>${row.price}</button>
                            </form>
                        </c:if>
                    </td>
                    <td>${row.status}</td>
                    <td>${row.creationDate}</td>
                    <td>${row.completionDate}</td>
                    <td><my:modalShow content="${row.description}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty row.userReview}">
                                <my:modalShow content="${row.userReview}"/>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${row.status.name() == 'DONE' && empty row.userReview}">
                                    <form method="get" action="feedback.jsp">
                                        <input type="hidden" name="request-id" value="${row.id}"/>
                                        <button>Leave Feedback</button>
                                    </form>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
