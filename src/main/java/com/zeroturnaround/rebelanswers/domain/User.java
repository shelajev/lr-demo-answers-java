package com.zeroturnaround.rebelanswers.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
public class User implements Serializable {
  @Id
  @GeneratedValue
  private Long id;
  @NotEmpty @Email
  @Column(name = "email", unique = true)
  private String email;
  @NotEmpty @Size(max = 255)
  @Column(name = "name")
  private String name;
  @NotEmpty @Size(min = 6, max = 255)
  @Column(name = "password")
  private String password;
  @Column(name = "created_at")
  private Date created;

  public User() {
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public User email(final String email) {
    setEmail(email);
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public User name(final String name) {
    setName(name);
    return this;
  }

  public String encodeProvidedPasswordForUser(final String passwordToEncode) {
    if (null == passwordToEncode) {
      return null;
    }

    return new ShaPasswordEncoder().encodePassword(passwordToEncode, email);
  }

  public User setAndEncodePassword(final String password) {
    if (null == password) {
      this.password = null;
    }

    this.password = encodeProvidedPasswordForUser(password);
    return this;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public User password(final String password) {
    setPassword(password);
    return this;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public User created(Date created) {
    setCreated(created);
    return this;
  }

  @Override
  public String toString() {
    final StringBuilder result = new StringBuilder("User(");
    result
        .append(id).append(";")
        .append(email).append(";")
        .append(name)
        .append(")");
    return result.toString();
  }

  @Override
  public int hashCode() {
    return (email == null) ? 0 : email.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof User)) return false;

    final User o = (User) other;
    return !(email != null ? !email.equals(o.email) : o.email != null);
  }

}
