package com.zeroturnaround.rebelanswers.mvc.controller;

import com.zeroturnaround.rebelanswers.dao.AnswerDao;
import com.zeroturnaround.rebelanswers.domain.Answer;
import com.zeroturnaround.rebelanswers.domain.Comment;
import com.zeroturnaround.rebelanswers.domain.Question;
import com.zeroturnaround.rebelanswers.domain.User;
import com.zeroturnaround.rebelanswers.mvc.exceptions.CommentStorageErrorException;
import com.zeroturnaround.rebelanswers.mvc.tools.ThreadSafePegDownProcessor;
import com.zeroturnaround.rebelanswers.security.SecurityTools;
import com.zeroturnaround.rebelanswers.security.StandardAuthorities;
import com.zeroturnaround.rebelanswers.service.CommentService;
import com.zeroturnaround.rebelanswers.service.QuestionService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

  private final QuestionService questionService;
  private final CommentService commentService;
  private final ThreadSafePegDownProcessor pegDown;
  private final PrettyTime prettyTime;
  private final AnswerDao answerService;

  @Autowired
  public CommentController(final QuestionService questionService, final CommentService commentService, final AnswerDao answerService) {
    this.questionService = questionService;
    this.commentService = commentService;
    this.answerService = answerService;
    this.pegDown = new ThreadSafePegDownProcessor();
    this.prettyTime = new PrettyTime();
  }

  protected CommentController() {
    // CGLib AOP needs a protected default constructor
    this.questionService = null;
    this.commentService = null;
    this.answerService = null;
    this.pegDown = null;
    prettyTime = null;
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/question/comment/{questionId}", method = RequestMethod.POST)
  public @ResponseBody Map<String, String> commentQuestion(@PathVariable final Long questionId, @RequestParam final String comment) throws NoSuchRequestHandlingMethodException {
    Question question = questionService.getQuestionById(questionId);
    if (null == question) {
      throw new NoSuchRequestHandlingMethodException("commentQuestion", this.getClass());
    }
    return performComment(comment, Comment.ParentType.QUESTION, question.getId());
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/answer/comment/{answerId}", method = RequestMethod.POST)
  public @ResponseBody Map<String, String> answerQuestion(@PathVariable final Long answerId, @RequestParam final String comment) throws NoSuchRequestHandlingMethodException {
    Answer answer = answerService.getAnswerById(answerId);
    if (null == answer) {
      throw new NoSuchRequestHandlingMethodException("commentAnswer", this.getClass());
    }
    return performComment(comment, Comment.ParentType.ANSWER, answer.getId());
  }

  private Map<String, String> performComment(String comment, Comment.ParentType parent_type, Long parent_id) {
    if (null == comment || comment.trim().isEmpty()) {
      return Collections.emptyMap();
    }
    User user = SecurityTools.getAuthenticatedUser();
    Comment new_comment = new Comment()
        .parentType(parent_type)
        .parentId(parent_id)
        .content(comment)
        .author(user);
    if (!commentService.store(new_comment)) {
      throw new CommentStorageErrorException(new_comment);
    }

    Map<String, String> result = new HashMap<String, String>();
    result.put("content", "<div>" +
        "<div class=\"post\">" + pegDown.markdownToHtml(new_comment.getContent()) + "</div>" +
        "<div class=\"note\">Commented by " + HtmlUtils.htmlEscape(user.getName()) + "&nbsp;" + HtmlUtils.htmlEscape(prettyTime.format(new Date())) + "</div>" +
        "</div>" +
        "<hr/>"
    );
    return result;
  }
}