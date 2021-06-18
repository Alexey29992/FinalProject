<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="size" value="${pageContext.request.getParameter('size')}"/>
<label>
    <button onclick="setSize(5)" ${size eq '5' ? 'disabled' : ''}>
        5
    </button>
</label>
<label>
    <button onclick="setSize(10)" ${size eq '10' ? 'disabled' : ''}>
        10
    </button>
</label>
<label>
    <button onclick="setSize(20)" ${size eq '20' ? 'disabled' : ''}>
        20
    </button>
</label>
<label>
    <button onclick="setSize(40)" ${size eq '40' ? 'disabled' : ''}>
        40
    </button>
</label>