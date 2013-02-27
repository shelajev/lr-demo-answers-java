package com.zeroturnaround.rebelanswers.mvc.controller;

import com.zeroturnaround.rebelanswers.domain.User;
import com.zeroturnaround.rebelanswers.mvc.exceptions.UserStorageErrorException;
import com.zeroturnaround.rebelanswers.mvc.model.RegistrationData;
import com.zeroturnaround.rebelanswers.security.UserDetailsWrapper;
import com.zeroturnaround.rebelanswers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

  private final UserService service;

  @Autowired
  public LoginController(final UserService service) {
    this.service = service;
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView loginHandler(final ModelMap modelMap, final HttpServletRequest request, final HttpSession session) {
    final ModelAndView result = new ModelAndView("authentication/login");
    modelMap.addAttribute("registrationData", new RegistrationData());

    final boolean loginError = request.getParameter("login_error") != null;
    if (loginError) {
      result.addObject("loginError", loginError);
      Exception e = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      if (e instanceof BadCredentialsException) {
        //noinspection deprecation
        Authentication auth = ((BadCredentialsException) e).getAuthentication();
        Object principal = auth.getPrincipal();
        result.addObject("lastUser", principal);
      }
    }

    return result;
  }

  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  public String signup(@ModelAttribute @Valid final RegistrationData registrationData, BindingResult result) {
    if (result.hasErrors()) {
      return "authentication/login";
    }
    else {
      User user = new User()
          .email(registrationData.getEmail())
          .name(registrationData.getName())
          .setAndEncodePassword(registrationData.getPassword());
      if (!service.store(user)) {
        throw new UserStorageErrorException(user);
      }

      UserDetailsWrapper principal = new UserDetailsWrapper(user);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);

      return "redirect:/welcome.do";
    }
  }
}