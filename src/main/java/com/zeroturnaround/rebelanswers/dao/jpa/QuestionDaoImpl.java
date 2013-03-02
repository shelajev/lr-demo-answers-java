package com.zeroturnaround.rebelanswers.dao.jpa;

import com.zeroturnaround.rebelanswers.dao.QuestionDao;
import com.zeroturnaround.rebelanswers.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class QuestionDaoImpl implements QuestionDao {

  private final DaoTools daoTools;

  public QuestionDaoImpl(final DaoTools daoTools) {
    if (null == daoTools) throw new IllegalArgumentException("daoTools can't be null");
    this.daoTools = daoTools;
  }

  public Question getQuestionById(Long id) {
    return daoTools.findById(Question.class, id);
  }

  public Page<Question> getAllQuestions(Pageable pageable) {
    List<Question> content = daoTools.getAllEntities(Question.class, "created", DaoTools.SortOrder.DESC, pageable.getOffset(), pageable.getPageSize());
    long total = daoTools.countEntities(Question.class);
    return new PageImpl<>(content, pageable, total);
  }

  public Page<Question> getQuestionsWithoutAnswers(Pageable pageable) {
    String filter = "where size(answers) = 0";
    List<Question> content = daoTools.getFilteredEntities(Question.class, "created", DaoTools.SortOrder.DESC, filter, pageable.getOffset(), pageable.getPageSize());
    long total = daoTools.countFilteredEntities(Question.class, filter);
    return new PageImpl<>(content, pageable, total);
  }

  public Page<Question> getUnansweredQuestions(Pageable pageable) {
    String filter = "where acceptedAnswer is null";
    List<Question> content = daoTools.getFilteredEntities(Question.class, "created", DaoTools.SortOrder.DESC, filter, pageable.getOffset(), pageable.getPageSize());
    long total = daoTools.countFilteredEntities(Question.class, filter);
    return new PageImpl<>(content, pageable, total);
  }

  public Page<Question> getUnansweredQuestionsWithoutAnswers(Pageable pageable) {
    String filter = "where acceptedAnswer is null and size(answers) = 0";
    List<Question> content = daoTools.getFilteredEntities(Question.class, "created", DaoTools.SortOrder.DESC, filter, pageable.getOffset(), pageable.getPageSize());
    long total = daoTools.countFilteredEntities(Question.class, filter);
    return new PageImpl<>(content, pageable, total);
  }

  public Page<Question> searchQuestions(String search, Pageable pageable) {
    long total = daoTools.countSearchByAttribute(Question.class, "title", search);
    List<Question> content = daoTools.searchByAttribute(Question.class, "title", search, "created", DaoTools.SortOrder.DESC, pageable.getOffset(), pageable.getPageSize());
    return new PageImpl<>(content, pageable, total);
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
