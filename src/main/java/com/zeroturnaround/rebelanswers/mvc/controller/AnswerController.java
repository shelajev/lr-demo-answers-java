package com.zeroturnaround.rebelanswers.mvc.controller;

import com.zeroturnaround.rebelanswers.domain.Answer;
import com.zeroturnaround.rebelanswers.domain.Question;
import com.zeroturnaround.rebelanswers.domain.User;
import com.zeroturnaround.rebelanswers.mvc.exceptions.AnswerStorageErrorException;
import com.zeroturnaround.rebelanswers.mvc.exceptions.QuestionStorageErrorException;
import com.zeroturnaround.rebelanswers.mvc.model.AnswerData;
import com.zeroturnaround.rebelanswers.mvc.taglib.JspUtils;
import com.zeroturnaround.rebelanswers.security.SecurityTools;
import com.zeroturnaround.rebelanswers.security.StandardAuthorities;
import com.zeroturnaround.rebelanswers.service.AnswerService;
import com.zeroturnaround.rebelanswers.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Controller
public class AnswerController {

  private final QuestionController questionController;
  private final QuestionService questionService;
  private final AnswerService answerService;
  private final SecurityTools tools;

  @Autowired
  public AnswerController(final QuestionController questionController, final QuestionService questionService, final AnswerService answerService, final SecurityTools tools) {
    this.questionController = questionController;
    this.questionService = questionService;
    this.answerService = answerService;
    this.tools = tools;
  }

  protected AnswerController() {
    // CGLib AOP needs a protected default constructor
    this.questionController = null;
    this.questionService = null;
    this.answerService = null;
    this.tools = null;
  }

  /**
   * Answer question
   */

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/question/answer/{questionId}", method = RequestMethod.POST)
  public ModelAndView answerQuestion(@PathVariable final Long questionId, @ModelAttribute @Valid final AnswerData answerData, final BindingResult result) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = questionController.getShowModelAndView(questionId);
    if ((boolean) mav.getModelMap().get("hasAnswered")) {
      throw new AccessDeniedException("Already answered this question");
    }

    if (result.hasErrors()) {
      return mav;
    }
    else {
      Question question = ((Question) mav.getModelMap().get("question"));
      Answer answer = new Answer()
          .question(question)
          .content(answerData.getContent())
          .author(tools.getAuthenticatedUser());
      if (!answerService.store(answer)) {
        throw new AnswerStorageErrorException(answer);
      }

      UriComponents uriComponents =
          UriComponentsBuilder.newInstance()
              .scheme("redirect").path("/question/{id}/{title}").build()
              .expand(questionId, JspUtils.sanitizeForUrl(question.getTitle()))
              .encode();
      return new ModelAndView(uriComponents.toUriString());
    }
  }

  /**
   * Accept answer
   */

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/answer/accept/{answerId}", method = RequestMethod.GET)
  public String acceptAnswer(@PathVariable final Long answerId) throws NoSuchRequestHandlingMethodException {
    Answer answer = answerService.getAnswerById(answerId);
    if (null == answer) {
      throw new NoSuchRequestHandlingMethodException("acceptAnswer", this.getClass());
    }

    User user = tools.getAuthenticatedUser();
    if (null == user || !user.equals(answer.getQuestion().getAuthor())) {
      throw new AccessDeniedException("Not the author of the question");
    }

    Question question = answer.getQuestion();
    question.setAcceptedAnswer(answer);
    if (!questionService.store(question)) {
      throw new QuestionStorageErrorException(question);
    }

    UriComponents uriComponents =
        UriComponentsBuilder.newInstance()
            .scheme("redirect").path("/question/{id}/{title}").build()
            .expand(question.getId(), JspUtils.sanitizeForUrl(question.getTitle()))
            .encode();
    return uriComponents.toUriString();
  }

  /**
   * Revise answer
   */

  public ModelAndView getReviseModelAndView(final Long answerId) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = new ModelAndView("answers/revise");
    Answer answer = answerService.getAnswerById(answerId);
    if (null == answer) {
      throw new NoSuchRequestHandlingMethodException("reviseAnswer", this.getClass());
    }
    User user = tools.getAuthenticatedUser();
    if (null == user || !user.equals(answer.getAuthor())) {
      throw new AccessDeniedException("Not the author of the answer");
    }

    mav.addObject(answer);
    mav.addObject(answer.getQuestion());

    return mav;
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/answer/revise/{answerId}", method = RequestMethod.GET)
  public ModelAndView reviseAnswer(@PathVariable final Long answerId) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = getReviseModelAndView(answerId);
    mav.addObject(new AnswerData((Answer) mav.getModelMap().get("answer")));

    return mav;
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/answer/revise/{answerId}", method = RequestMethod.POST)
  public ModelAndView revisedAnswer(@PathVariable final Long answerId, @ModelAttribute @Valid final AnswerData answerData, final BindingResult result) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = getReviseModelAndView(answerId);
    if (result.hasErrors()) {
      return mav;
    }
    else {
      Answer answer = ((Answer) mav.getModelMap().get("answer"))
          .content(answerData.getContent());
      if (!answerService.store(answer)) {
        throw new AnswerStorageErrorException(answer);
      }

      UriComponents uriComponents =
          UriComponentsBuilder.newInstance()
              .scheme("redirect").path("/question/{id}/{title}").build()
              .expand(answer.getQuestion().getId(), JspUtils.sanitizeForUrl(answer.getQuestion().getTitle()))
              .encode();
      return new ModelAndView(uriComponents.toUriString());
    }
  }
}