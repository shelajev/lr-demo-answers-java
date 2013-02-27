package com.zeroturnaround.rebelanswers.service.impl;

import com.zeroturnaround.rebelanswers.dao.QuestionDao;
import com.zeroturnaround.rebelanswers.domain.Question;
import com.zeroturnaround.rebelanswers.service.QuestionService;
import org.springframework.transaction.annotation.Transactional;

public class DaoQuestionService implements QuestionService {

  private final QuestionDao questionDao;

  public DaoQuestionService(final QuestionDao questionDao) {
    this.questionDao = questionDao;
  }

  @Transactional(readOnly = false)
  public boolean store(final Question question) {
    if (null == question) {
      return false;
    }

    questionDao.persistOrMerge(question);
    return true;
  }
}
