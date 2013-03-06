package com.zeroturnaround.rebelanswers.service;

import com.zeroturnaround.rebelanswers.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

  public Question getQuestionById(Long id);

  public Question getFullQuestionById(Long id);

  public Page<Question> getAllQuestions(Pageable pageable);

  public Page<Question> getQuestionsWithoutAnswers(Pageable pageable);

  public Page<Question> getUnansweredQuestions(Pageable pageable);

  public Page<Question> getUnansweredQuestionsWithoutAnswers(Pageable pageable);

  public Page<Question> searchQuestions(String search, Pageable pageable);

  public boolean store(Question question);

}