package com.zeroturnaround.rebelanswers.dao;

import com.zeroturnaround.rebelanswers.domain.Question;

import java.util.Collection;

public interface QuestionDao {

  public Question getQuestionById(Long id);

  public Collection<Question> getAllQuestions();

  public Collection<Question> getQuestionsWithoutAnswers();

  public Collection<Question> getUnansweredQuestions();

  public Collection<Question> getUnansweredQuestionsWithoutAnswers();

  public Question persistOrMerge(Question question);
}
