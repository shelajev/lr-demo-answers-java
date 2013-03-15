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
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

  private final UserService service;
  private final ConnectionFactoryRegistry connectionRepository;

  @Autowired
  public LoginController(final UserService service, final ConnectionFactoryRegistry connectionRepository) {
    this.service = service;
    this.connectionRepository = connectionRepository;
  }

  /*
   * Login
   */

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView loginHandler(final ModelMap modelMap, final HttpServletRequest request, final HttpSession session) {
    final ModelAndView mav = new ModelAndView("authentication/login");
    modelMap.addAttribute("registrationData", new RegistrationData());

    final boolean loginError = request.getParameter("login_error") != null;
    if (loginError) {
      mav.addObject("loginError", loginError);
      Exception e = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      if (e instanceof BadCredentialsException) {
        //noinspection deprecation
        Authentication auth = ((BadCredentialsException) e).getAuthentication();
        Object principal = auth.getPrincipal();
        mav.addObject("lastUser", principal);
      }
    }

    return mav;
  }

  /*
   * Registration
   */

  private void loginUser(User user) {
    UserDetailsWrapper principal = new UserDetailsWrapper(user);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
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

      loginUser(user);
      return "redirect:/";
    }
  }

  /*
   * Facebook connect
   */

  @RequestMapping(value = "/facebook/connect")
  public View facebookConnect() {
    String connected_uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/facebook/connected").build().toUriString();

    FacebookConnectionFactory connectionFactory = (FacebookConnectionFactory) connectionRepository.getConnectionFactory("facebook");
    OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
    OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
    oAuth2Parameters.setRedirectUri(connected_uri);
    String authorizeUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
    return new RedirectView(authorizeUrl);
  }

  @RequestMapping(value = "/facebook/connected")
  public String facebookConnected(@RequestParam final String code) {
    String connected_uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/facebook/connected").build().toUriString();

    FacebookConnectionFactory connectionFactory = (FacebookConnectionFactory) connectionRepository.getConnectionFactory("facebook");
    OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
    AccessGrant accessGrant = oauthOperations.exchangeForAccess(code, connected_uri, null);
    Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);
    if (connection != null) {
      Facebook facebook = connection.getApi();
      FacebookProfile userProfile = facebook.userOperations().getUserProfile();
      String facebook_id = userProfile.getId();

      User user = service.findByFacebookId(facebook_id);
      if (null == user) {
        user = new User()
            .name(userProfile.getName())
            .facebookId(facebook_id);
        if (!service.store(user)) {
          throw new UserStorageErrorException(user);
        }
      }
      loginUser(user);
    }
    return "redirect:/";
  }
}