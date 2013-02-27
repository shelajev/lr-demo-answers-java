package com.zeroturnaround.rebelanswers.dao;

import com.zeroturnaround.rebelanswers.domain.User;

public interface UserDao {

  public User findByEmail(String email);

  public User persistOrMerge(User user);
}
