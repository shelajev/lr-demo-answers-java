package com.zeroturnaround.rebelanswers.mvc.taglib;

import com.zeroturnaround.rebelanswers.domain.User;
import com.zeroturnaround.rebelanswers.security.UserDetailsWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class AuthenticatedUserNameTag extends TagSupport {
  @Override
  public int doStartTag() throws JspException {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null) {
        UserDetailsWrapper principal = (UserDetailsWrapper) auth.getPrincipal();
        User user = principal.getDelegate();
        if (user != null) {
          pageContext.getOut().print(user.getName());
        }
      }
    }
    catch (final IOException e) {
      throw new JspException("Error: IOException while writing to client" + e.getMessage());
    }
    return SKIP_BODY;
  }
}