package com.zeroturnaround.rebelanswers.service;

import com.zeroturnaround.rebelanswers.domain.User;

public interface UserService {

  public User findByUsername(String username);

  public User findByFacebookId(String facebookId);

  public User findByEmail(String email);

  public boolean store(User user);

}