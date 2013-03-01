package com.zeroturnaround.rebelanswers.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "answers")
public class Answer implements Serializable {
  @Id
  @GeneratedValue
  private Long id;
  @Column(name = "content")
  private String content;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User author;
  @Column(name = "created_at")
  private Date created;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "question_id")
  private Question question;

  public Answer() {
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public Answer content(final String content) {
    setContent(content);
    return this;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(final User author) {
    this.author = author;
  }

  public Answer author(final User author) {
    setAuthor(author);
    return this;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Answer created(Date created) {
    setCreated(created);
    return this;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public Answer question(Question question) {
    setQuestion(question);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Answer answer = (Answer) o;

    if (id != null ? !id.equals(answer.id) : answer.id != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Answer(" + id + ") : " + content;
  }
}
