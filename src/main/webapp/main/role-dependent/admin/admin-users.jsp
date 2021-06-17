<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ralib" uri="http://repairagency.com/taglib" %>

<ralib:onRequest command="get-admin-users"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/styles/admin-users.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/title.png" type="image/icon">
    <title>User Management</title>
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
    </script>
    <jsp:include page="/WEB-INF/jspf/modal-box-scripts.jspf"/>
</head>
<body>
<my:navBar/>

<div class="row">
    <div class="column-middle">
        <div class="column-middle-left">
            <form method="get" action="${pageContext.request.requestURI}" id="request-query">
                <input type="hidden" name="command" value="get-admin-users"/>
                <input type="hidden" id="size" name="size" value="${pageContext.request.getParameter('size')}"/>
                <input type="hidden" id="page" name="page" value="${pageContext.request.getAttribute('page')}"/>
                <div class="dropdown-filters-outer">
                    <div class="filter-role-frame">
                        <label>
                            Filter by role:
                            <select name="filter-role" class="select-css" onchange="this.form.submit()">
                                <c:set var="filter" value="${pageContext.request.getParameter('filter-role')}"/>
                                <option value="none"
                                        <c:if test="${filter == 'none'}">
                                            selected
                                        </c:if>>
                                    none
                                </option>
                                <option value="client"
                                        <c:if test="${filter == 'client'}">
                                            selected
                                        </c:if>>
                                    Client
                                </option>
                                <option value="master"
                                        <c:if test="${filter == 'master'}">
                                            selected
                                        </c:if>>
                                    Master
                                </option>
                                <option value="manager"
                                        <c:if test="${filter == 'manager'}">
                                            selected
                                        </c:if>>
                                    Manager
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
                                <option value="login"
                                        <c:if test="${sortFactor == 'login'}">
                                            selected
                                        </c:if>>
                                    Login
                                </option>
                                <option value="role"
                                        <c:if test="${sortFactor == 'role'}">
                                            selected
                                        </c:if>>
                                    Role
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
            <caption>Users</caption>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">Login</th>
                <th scope="col">Role</th>
                <th scope="col">Phone number</th>
                <th scope="col">Balance</th>
                <th scope="col">Actions</th>
            </tr>
            <c:forEach var="row" items="${requestScope.users}">
                <c:if test="${!row.role.name().equals('ADMIN')}">
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
                        <td>${row.role}</td>
                        <td>
                            <c:if test="${row.role.name().equals('CLIENT')}">
                                ${row.phNumber}
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${row.role.name().equals('CLIENT')}">
                                ${row.balance}
                            </c:if>
                        </td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/controller">
                                <input type="hidden" name="command" value="remove-user">
                                <label>
                                    <button class="action-button" name="user-id" value="${row.id}">
                                        Delete
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