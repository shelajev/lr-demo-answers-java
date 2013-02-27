<%@ include file="../fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <%@include file="../fragments/head.jspf" %>
  <title>Rebel Answers - Ask your question</title>
</head>
<body>

<div class="container">
  <div class="row">
    <div class="span12">
      <%@include file="../fragments/navbar.jspf" %>

      <div class="content">

        <form:form modelAttribute="questionData" cssClass="form form-horizontal">

          <%@ include file="../fragments/errors.jspf" %>

          <div class="control-group">
            <form:label path="title" cssClass="control-label">Title</form:label>
            <div class="controls">
              <form:input path="title" cssClass="input-xxlarge"/>
            </div>
          </div>
          <div class="control-group">
            <div class="controls">
              <div class="wmd-panel">
                <div id="wmd-button-bar"></div>
                <form:textarea path="content" cssClass="wmd-input input-xxlarge" id="wmd-input" rows="15"/>
              </div>
            </div>
          </div>
          <div class="control-group">
            <div class="controls">
              <div id="wmd-preview" class="wmd-panel wmd-preview input-xxlarge"></div>
            </div>
          </div>
          <div class="control-group">
            <div class="controls">
              <button type="submit" class="btn">Post your question</button>
            </div>
          </div>
        </form:form>

      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  /*<![CDATA[*/
  jQuery(function ($) {
    var converter = Markdown.getSanitizingConverter();
    var editor = new Markdown.Editor(converter);
    editor.run();
  });
  /*]]>*/
</script>

</body>
</html>