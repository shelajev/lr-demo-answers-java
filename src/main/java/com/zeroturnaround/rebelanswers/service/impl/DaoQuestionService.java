package com.zeroturnaround.rebelanswers.service.impl;

import com.zeroturnaround.rebelanswers.dao.QuestionDao;
import com.zeroturnaround.rebelanswers.domain.Question;
import com.zeroturnaround.rebelanswers.service.QuestionService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class DaoQuestionService implements QuestionService {

  private final QuestionDao questionDao;

  public DaoQuestionService(final QuestionDao questionDao) {
    this.questionDao = questionDao;
  }

  @Transactional(readOnly = true)
  public Question getQuestionById(Long id)
  {
    return questionDao.getQuestionById(id);
  }

  @Transactional(readOnly = true)
  public Collection<Question> getAllQuestions() {
    return questionDao.getAllQuestions();
  }

  @Transactional(readOnly = true)
  public Collection<Question> getQuestionsWithoutAnswers() {
    return questionDao.getQuestionsWithoutAnswers();
  }

  @Transactional(readOnly = true)
  public Collection<Question> getUnansweredQuestions() {
    return questionDao.getUnansweredQuestions();
  }

  @Transactional(readOnly = true)
  public Collection<Question> getUnansweredQuestionsWithoutAnswers() {
    return questionDao.getUnansweredQuestionsWithoutAnswers();
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
