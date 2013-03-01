package com.zeroturnaround.rebelanswers.dao.jpa;

import com.zeroturnaround.rebelanswers.dao.QuestionDao;
import com.zeroturnaround.rebelanswers.domain.Question;

import java.util.Collection;

public class QuestionDaoImpl implements QuestionDao {

  private final DaoTools daoTools;

  public QuestionDaoImpl(final DaoTools daoTools) {
    if (null == daoTools) throw new IllegalArgumentException("daoTools can't be null");
    this.daoTools = daoTools;
  }

  public Question getQuestionById(Long id) {
    return daoTools.findById(Question.class, id);
  }

  public Collection<Question> getAllQuestions() {
    return daoTools.getAllEntities(Question.class, "created", DaoTools.SortOrder.DESC);
  }

  public Collection<Question> getQuestionsWithoutAnswers() {
    return daoTools.getFilteredEntities(Question.class, "created", DaoTools.SortOrder.DESC, "where size(answers) = 0");
  }

  public Collection<Question> getUnansweredQuestions() {
    return daoTools.getFilteredEntities(Question.class, "created", DaoTools.SortOrder.DESC, "where acceptedAnswer is null");
  }

  public Collection<Question> getUnansweredQuestionsWithoutAnswers() {
    return daoTools.getFilteredEntities(Question.class, "created", DaoTools.SortOrder.DESC, "where acceptedAnswer is null and size(answers) = 0");
  }

  public Collection<Question> searchQuestions(String search) {
    return daoTools.searchByAttribute(Question.class, "title", search, "created", DaoTools.SortOrder.DESC);
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
