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

        <h3><c:out value="${question.title}"/></h3>

        <c:out value="${questionHtml}" escapeXml="false"/> <br/>
        Asked by <c:out value="${question.author.name}"/>&nbsp;
        <ra:prettytime date="${question.created}"/>

        <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">
          <sec:authorize access="principal.delegate.id == #question.author.id">
            <spring:url var="reviseUrl" value="/questions/revise/{id}">
              <spring:param name="id" value="${question.id}"/>
            </spring:url>
            <a href="${reviseUrl}">Revise</a></li>
          </sec:authorize>
        </sec:authorize>
        <hr/>

      </div>

    </div>
  </div>
</div>

</body>
</html>