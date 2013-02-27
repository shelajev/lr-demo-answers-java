package com.zeroturnaround.rebelanswers.dao;

import com.zeroturnaround.rebelanswers.domain.Question;

public interface QuestionDao {

  public Question persistOrMerge(Question user);
}
