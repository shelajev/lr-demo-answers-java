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
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User author;
  @Column(name = "created_at")
  private Date created;

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

  @Override
  public String toString() {
    return "Answer(" + id + ") : " + content;
  }
}
