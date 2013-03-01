package com.zeroturnaround.rebelanswers.dao;

import com.zeroturnaround.rebelanswers.domain.Answer;
import com.zeroturnaround.rebelanswers.domain.Question;

import java.util.Collection;

public interface AnswerDao {

  public Answer getAnswerById(Long id);

  public Answer persistOrMerge(Answer answer);
}
