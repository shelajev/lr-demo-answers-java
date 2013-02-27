package com.zeroturnaround.rebelanswers.service.impl;

import com.zeroturnaround.rebelanswers.dao.UserDao;
import com.zeroturnaround.rebelanswers.domain.User;
import com.zeroturnaround.rebelanswers.security.UserDetailsWrapper;
import com.zeroturnaround.rebelanswers.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class DaoUserService implements UserService, UserDetailsService {

  private final UserDao userDao;

  public DaoUserService(final UserDao userDao) {
    this.userDao = userDao;
  }

  @Transactional(readOnly = true)
  public User findByEmail(final String email) {
    return userDao.findByEmail(email);
  }

  @Transactional(readOnly = false)
  public boolean store(final User user) {
    if (null == user) {
      return false;
    }

    userDao.persistOrMerge(user);
    return true;
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException, DataAccessException {
    final User user = findByEmail(userName);
    if (null == user) {
      throw new UsernameNotFoundException(userName);
    }
    return new UserDetailsWrapper(user);
  }
}