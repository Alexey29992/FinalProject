<a href="${pageContext.request.contextPath}/main/role-dependent/client/request-form.jsp">New Request</a> |
<a href="${pageContext.request.contextPath}/main/role-dependent/client/client-requests.jsp">My Requests</a> |
<a href="${pageContext.request.contextPath}/main/role-dependent/client/payment-history.jsp">Payment History</a> |
<a href="${pageContext.request.contextPath}/main/role-dependent/client/profile-settings.jsp">Profile Settings</a> |
Entered as: ${sessionScope.user.login} |
<!-- should be replaced to db query -->
Balance: ${sessionScope.user.balance} $