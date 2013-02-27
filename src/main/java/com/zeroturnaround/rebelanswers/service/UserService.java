package com.zeroturnaround.rebelanswers.service;

import com.zeroturnaround.rebelanswers.domain.User;

public interface UserService {

  public User findByEmail(String email);

  public boolean store(User user);

}