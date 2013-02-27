package com.zeroturnaround.rebelanswers.mvc.controller;

import com.zeroturnaround.rebelanswers.domain.Question;
import com.zeroturnaround.rebelanswers.mvc.exceptions.QuestionStorageErrorException;
import com.zeroturnaround.rebelanswers.mvc.model.QuestionData;
import com.zeroturnaround.rebelanswers.security.Authorities;
import com.zeroturnaround.rebelanswers.security.SecurityTools;
import com.zeroturnaround.rebelanswers.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

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

  @RequestMapping(value = "/welcome", method = RequestMethod.GET)
  public String welcomeHandler() {
    return "welcome";
  }

  @RolesAllowed({ Authorities.USER })
  @RequestMapping(value = "/questions/ask", method = RequestMethod.GET)
  public ModelAndView ask() {
    final ModelAndView result = new ModelAndView("questions/ask");
    result.addObject(new QuestionData());
    return result;
  }

  @RolesAllowed({ Authorities.USER })
  @RequestMapping(value = "/questions/ask", method = RequestMethod.POST)
  public String asked(@ModelAttribute @Valid final QuestionData questionData, BindingResult result) {
    if (result.hasErrors()) {
      return "questions/ask";
    }
    else {
      Question question = new Question()
          .title(questionData.getTitle())
          .content(questionData.getContent())
          .author(tools.getAuthenticatedUser());
      if (!service.store(question)) {
        throw new QuestionStorageErrorException(question);
      }

      return "redirect:/welcome.do";
    }
  }
}