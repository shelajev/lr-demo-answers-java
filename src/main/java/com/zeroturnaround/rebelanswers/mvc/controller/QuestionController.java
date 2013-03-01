package com.zeroturnaround.rebelanswers.mvc.controller;

import com.zeroturnaround.rebelanswers.domain.Answer;
import com.zeroturnaround.rebelanswers.domain.Question;
import com.zeroturnaround.rebelanswers.domain.User;
import com.zeroturnaround.rebelanswers.mvc.exceptions.QuestionStorageErrorException;
import com.zeroturnaround.rebelanswers.mvc.model.AnswerData;
import com.zeroturnaround.rebelanswers.mvc.model.QuestionData;
import com.zeroturnaround.rebelanswers.mvc.taglib.JspUtils;
import com.zeroturnaround.rebelanswers.security.SecurityTools;
import com.zeroturnaround.rebelanswers.security.StandardAuthorities;
import com.zeroturnaround.rebelanswers.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Collection;

@Controller
public class QuestionController {

  private final QuestionService service;
  private final SecurityTools tools;

  @Autowired
  public QuestionController(final QuestionService service, final SecurityTools tools) {
    this.service = service;
    this.tools = tools;
  }

  protected QuestionController() {
    // CGLib AOP needs a protected default constructor
    this.service = null;
    this.tools = null;
  }

  /**
   * Ask question
   */

  public ModelAndView getAskModelAndView() {
    final ModelAndView mav = new ModelAndView("questions/ask");
    mav.addObject("section", "ask");
    return mav;
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/questions/ask", method = RequestMethod.GET)
  public ModelAndView ask() {
    final ModelAndView mav = getAskModelAndView();
    mav.addObject(new QuestionData());
    return mav;
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/questions/ask", method = RequestMethod.POST)
  public ModelAndView asked(@ModelAttribute @Valid final QuestionData questionData, final BindingResult result) {
    final ModelAndView mav = getAskModelAndView();
    if (result.hasErrors()) {
      return mav;
    }
    else {
      Question question = new Question()
          .title(questionData.getTitle())
          .content(questionData.getContent())
          .author(tools.getAuthenticatedUser());
      if (!service.store(question)) {
        throw new QuestionStorageErrorException(question);
      }

      return new ModelAndView("redirect:/questions/all");
    }
  }

  /**
   * List questions
   */

  public ModelAndView getQuestionsModelAndView(final Filter filterBy) {
    final ModelAndView mav = new ModelAndView("questions/all");
    mav.addObject("subSection", filterBy);
    return mav;
  }

  @RequestMapping(value = "/questions/all", method = RequestMethod.GET)
  public ModelAndView allQuestions(@RequestParam(defaultValue = "newest") final Filter filterBy) {
    final ModelAndView mav = getQuestionsModelAndView(filterBy);
    mav.addObject("section", "questions");
    switch (filterBy) {
      case newest:
        mav.addObject("questions", service.getAllQuestions());
        break;
      case noanswers:
        mav.addObject("questions", service.getQuestionsWithoutAnswers());
        break;
    }
    return mav;
  }

  @RequestMapping(value = "/questions/unanswered", method = RequestMethod.GET)
  public ModelAndView unansweredQuestions(@RequestParam(defaultValue = "newest") final Filter filterBy) {
    final ModelAndView mav = getQuestionsModelAndView(filterBy);
    mav.addObject("section", "unanswered");
    switch (filterBy) {
      case newest:
        mav.addObject("questions", service.getUnansweredQuestions());
        break;
      case noanswers:
        mav.addObject("questions", service.getUnansweredQuestionsWithoutAnswers());
        break;
    }
    return mav;
  }

  /**
   * Show question
   */

  public ModelAndView getShowModelAndView(Long questionId) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = new ModelAndView("questions/read");
    Question question = service.getQuestionById(questionId);
    if (null == question) {
      throw new NoSuchRequestHandlingMethodException("showQuestion", this.getClass());
    }
    mav.addObject(question);

    boolean hasAnswered = false;
    User user = tools.getAuthenticatedUser();
    if (user != null) {
      for (Answer answer : question.getAnswers()) {
        if (answer.getAuthor().equals(user)) {
          hasAnswered = true;
          break;
        }
      }
    }
    mav.addObject("hasAnswered", hasAnswered);

    return mav;
  }

  @RequestMapping(value = "/question/{questionId}/{questionTitle}", method = RequestMethod.GET)
  public ModelAndView showQuestion(@PathVariable final Long questionId) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = getShowModelAndView(questionId);
    mav.addObject(new AnswerData());
    return mav;
  }

  /**
   * Revise question
   */

  public ModelAndView getReviseModelAndView(final Long questionId) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = new ModelAndView("questions/revise");
    Question question = service.getQuestionById(questionId);
    if (null == question) {
      throw new NoSuchRequestHandlingMethodException("reviseQuestion", this.getClass());
    }
    User user = tools.getAuthenticatedUser();
    if (null == user || !user.equals(question.getAuthor())) {
      throw new AccessDeniedException("Not the author of the question");
    }

    mav.addObject(question);

    return mav;
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/question/revise/{questionId}", method = RequestMethod.GET)
  public ModelAndView reviseQuestion(@PathVariable final Long questionId) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = getReviseModelAndView(questionId);
    mav.addObject(new QuestionData((Question) mav.getModelMap().get("question")));

    return mav;
  }

  @RolesAllowed({ StandardAuthorities.USER })
  @RequestMapping(value = "/question/revise/{questionId}", method = RequestMethod.POST)
  public ModelAndView revisedQuestion(@PathVariable final Long questionId, @ModelAttribute @Valid final QuestionData questionData, final BindingResult result) throws NoSuchRequestHandlingMethodException {
    final ModelAndView mav = getReviseModelAndView(questionId);
    if (result.hasErrors()) {
      return mav;
    }
    else {
      Question question = ((Question) mav.getModelMap().get("question"))
          .title(questionData.getTitle())
          .content(questionData.getContent());
      if (!service.store(question)) {
        throw new QuestionStorageErrorException(question);
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
   * Search questions
   */

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public ModelAndView searchQuestions(@RequestParam final String q) {
    final ModelAndView mav = new ModelAndView("questions/search");
    mav.addObject("q", q);
    Collection<Question> questions = service.searchQuestions(q);
    mav.addObject("questions", questions);
    return mav;
  }

  static enum Filter {newest, noanswers}
}