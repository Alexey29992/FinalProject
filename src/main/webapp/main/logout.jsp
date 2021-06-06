<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${pageContext.request.session.invalidate()}
<c:redirect url="/main/home.jsp"/>
