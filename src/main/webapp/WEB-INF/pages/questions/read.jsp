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

        <div class="row question">
          <div class="span1 voting">
            <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">
              <spring:url var="voteQuestionUp" value="/question/voteup/{id}">
                <spring:param name="id" value="${question.id}"/>
              </spring:url>
              <c:set var="userVotedQuestionUp" value="${question.autenticatedUserVote eq 1 ? ' label-success' : ''}"/>
              <span class="label vote-up${userVotedQuestionUp}"><a href="${voteQuestionUp}">+</a></span>
            </sec:authorize>
            <span class="votes"><c:out value="${question.voteCount}"/></span>
            <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">
              <spring:url var="voteQuestionDown" value="/question/votedown/{id}">
                <spring:param name="id" value="${question.id}"/>
              </spring:url>
              <c:set var="userVotedQuestionDown" value="${question.autenticatedUserVote eq -1 ? ' label-inverse' : ''}"/>
              <span class="label vote-down${userVotedQuestionDown}"><a href="${voteQuestionDown}">-</a></span>
            </sec:authorize>
          </div>

          <div class="span11">
            <div class="question">
              <ra:markdownToHtml text="${question.content}"/>
            </div>
          </div>
          <div class="note">
            Asked by <c:out value="${question.author.name}"/>&nbsp;
            <ra:prettytime date="${question.created}"/>
          </div>

          <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">
            <sec:authorize access="principal.delegate.id eq #question.author.id">
              <c:set var="isQuestionAuthor" value="${true}"/>
            </sec:authorize>
          </sec:authorize>

          <c:if test="${isQuestionAuthor}">
            <spring:url var="reviseUrl" value="/question/revise/{id}">
              <spring:param name="id" value="${question.id}"/>
            </spring:url>
            <div class="note">
              <a href="${reviseUrl}">Revise</a></li>
            </div>
          </c:if>
        </div>

        <c:forEach var="answer" items="${question.answers}">
          <div class="row answer">
            <div class="span1 voting">
              <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">
                <spring:url var="voteAnswerUp" value="/answer/voteup/{id}">
                  <spring:param name="id" value="${answer.id}"/>
                </spring:url>
                <c:set var="userVotedAnswerUp" value="${answer.autenticatedUserVote eq 1 ? ' label-success' : ''}"/>
                <span class="label vote-up${userVotedAnswerUp}"><a href="${voteAnswerUp}">+</a></span>
              </sec:authorize>
              <span class="votes"><c:out value="${answer.voteCount}"/></span>
              <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">
                <spring:url var="voteAnswerDown" value="/answer/votedown/{id}">
                  <spring:param name="id" value="${answer.id}"/>
                </spring:url>
                <c:set var="userVotedAnswerDown" value="${answer.autenticatedUserVote eq -1 ? ' label-inverse' : ''}"/>
                <span class="label vote-down${userVotedAnswerDown}"><a href="${voteAnswerDown}">-</a></span>
              </sec:authorize>
            </div>

            <div class="span11">
              <c:if test="${question.acceptedAnswer eq answer}">
                <span class="label label-success">Accepted answer</span>
              </c:if>

              <div>
                <ra:markdownToHtml text="${answer.content}"/>
              </div>

              <div class="note">
                Answered by <c:out value="${answer.author.name}"/>&nbsp;
                <ra:prettytime date="${answer.created}"/>
              </div>

              <div class="note">
                <c:if test="${isQuestionAuthor and question.acceptedAnswer ne answer}">
                  <spring:url var="acceptUrl" value="/answer/accept/{id}">
                    <spring:param name="id" value="${answer.id}"/>
                  </spring:url>
                  <a href="${acceptUrl}" class="btn">Accept as answer</a>
                </c:if>

                <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">
                  <sec:authorize access="principal.delegate.id eq #answer.author.id">
                    <spring:url var="reviseAnswerUrl" value="/answer/revise/{id}">
                      <spring:param name="id" value="${answer.id}"/>
                    </spring:url>
                    <a href="${reviseAnswerUrl}" class="btn">Revise</a>
                  </sec:authorize>
                </sec:authorize>
              </div>
            </div>

          </div>

        </c:forEach>

        <sec:authorize ifAllGranted="<%=StandardAuthorities.USER%>">

          <c:if test="${not hasAnswered}">

            <spring:url var="answerUrl" value="/question/answer/{id}">
              <spring:param name="id" value="${question.id}"/>
            </spring:url>
            <form:form modelAttribute="answerData" action="${answerUrl}" cssClass="form form-horizontal">

              <legend>Your answer</legend>

              <%@ include file="../answers/form.jspf" %>

              <div class="control-group">
                <div class="controls">
                  <button type="submit" class="btn">Post your answer</button>
                </div>

              </div>

            </form:form>

            <%@ include file="../common/markdown.jspf" %>
          </c:if>
        </sec:authorize>

      </div>
    </div>
  </div>
</div>

</body>
</html>