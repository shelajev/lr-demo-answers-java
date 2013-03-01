package com.zeroturnaround.rebelanswers.service;

import com.zeroturnaround.rebelanswers.domain.Question;

import java.util.Collection;

public interface QuestionService {

  public Question getQuestionById(Long id);

  public Collection<Question> getAllQuestions();

  public Collection<Question> getQuestionsWithoutAnswers();

  public Collection<Question> getUnansweredQuestions();

  public Collection<Question> getUnansweredQuestionsWithoutAnswers();

  public Collection<Question> searchQuestions(String search);

  public boolean store(Question question);

}