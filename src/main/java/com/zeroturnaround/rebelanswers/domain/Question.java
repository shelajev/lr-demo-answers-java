package com.zeroturnaround.rebelanswers.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question implements Serializable {

  @Id
  @GeneratedValue
  private Long id;
  @Column(name = "title")
  private String title;
  @Column(name = "content")
  private String content;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User author;
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Answer.class, mappedBy = "question")
  private List<Answer> answers;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "answer_id")
  private Answer acceptedAnswer;
  @Column(name = "created_at")
  private Date created;

  public Question() {
    setAnswers(new ArrayList<Answer>(0));
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Question id(final Long id) {
    this.setId(id);
    return this;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public Question title(final String title) {
    setTitle(title);
    return this;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public Question content(final String content) {
    setContent(content);
    return this;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(final User author) {
    this.author = author;
  }

  public Question author(final User author) {
    setAuthor(author);
    return this;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(final List<Answer> answers) {
    this.answers = new ArrayList<Answer>(answers);
  }

  public Question answers(final List<Answer> answerList) {
    setAnswers(answerList);
    return this;
  }

  public Answer getAcceptedAnswer() {
    return acceptedAnswer;
  }

  public void setAcceptedAnswer(final Answer acceptedAnswer) {
    this.acceptedAnswer = acceptedAnswer;
  }

  public Question acceptedAnswer(final Answer acceptedAnswer) {
    setAcceptedAnswer(acceptedAnswer);
    return this;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Question created(Date created) {
    setCreated(created);
    return this;
  }

  @Override
  public String toString() {
    return "Question(" + getId() + ") : " + (getAnswers() == null ? "0" : getAnswers().size()) + " answers: '" + getContent() + "'";
  }
}
