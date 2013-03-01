<%@ include file="../common/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <%@ include file="../common/head.jspf" %>
  <title>Rebel Answers - Questions</title>
</head>
<body>

<div class="container">
  <div class="row">
    <div class="span12">
      <%@ include file="../common/navbar.jspf" %>

      <div class="content">

        <h1>Search results for '<c:out value="${q}"/>'</h1>

        <c:forEach var="question" items="${questions}">
          <%@ include file="summary.jspf" %>
        </c:forEach>

        <c:if test="${empty questions}">
          <p>No questions found.</p>
        </c:if>

      </div>
    </div>
  </div>
</div>

</body>
</html>