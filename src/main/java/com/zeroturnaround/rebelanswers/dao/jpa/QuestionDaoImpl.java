package com.zeroturnaround.rebelanswers.dao.jpa;

import com.zeroturnaround.rebelanswers.dao.QuestionDao;
import com.zeroturnaround.rebelanswers.domain.Question;

public class QuestionDaoImpl implements QuestionDao {

  private final DaoTools daoTools;

  public QuestionDaoImpl(final DaoTools daoTools) {
    if (null == daoTools) throw new IllegalArgumentException("daoTools can't be null");
    this.daoTools = daoTools;
  }

  public Question persistOrMerge(final Question question) {
    if (null == question) throw new IllegalArgumentException("question can't be null");

    if (question.getId() == null) {
      return daoTools.persist(Question.class, question);
    }
    else {
      return daoTools.merge(Question.class, question);
    }
  }
}
