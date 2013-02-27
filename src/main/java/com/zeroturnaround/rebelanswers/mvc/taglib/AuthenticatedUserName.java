package com.zeroturnaround.rebelanswers.mvc.taglib;

import com.zeroturnaround.rebelanswers.domain.User;
import com.zeroturnaround.rebelanswers.security.UserDetailsWrapper;
import com.zeroturnaround.rebelanswers.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class AuthenticatedUserName extends TagSupport {
  @Override
  public int doStartTag() throws JspException {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
        UserService userService = ctx.getBean(UserService.class);
        if (userService != null) {
          UserDetailsWrapper principal = (UserDetailsWrapper) auth.getPrincipal();
          User user = userService.findByEmail(principal.getUsername());
          if (user != null) {
            pageContext.getOut().print(user.getName());
          }
        }
      }
    }
    catch (final IOException e) {
      throw new JspException("Error: IOException while writing to client" + e.getMessage());
    }
    return SKIP_BODY;
  }
}