package com.zeroturnaround.rebelanswers.service;

import com.zeroturnaround.rebelanswers.domain.Answer;

public interface AnswerService {

  public Answer getAnswerById(Long id);

  public boolean store(Answer answer);

}