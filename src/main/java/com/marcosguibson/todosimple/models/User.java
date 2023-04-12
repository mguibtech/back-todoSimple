package com.marcosguibson.todosimple.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
// Anotacoes do spring
public class User {

  public interface CreateUser {
  }

  public interface UpdateUser {
  }

  public static final String TABLE_NAME = "user";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true)
  private Long id;

  @Column(name = "username", length = 100, nullable = false, unique = true)
  @NotNull(groups = CreateUser.class)
  @NotEmpty(groups = CreateUser.class)
  @Size(groups = CreateUser.class, min = 2, max = 100)
  private String username;

  @JsonProperty(access = Access.WRITE_ONLY) 
  @Column(name = "password", length = 60, nullable = false)
  @NotNull(groups = { CreateUser.class, UpdateUser.class })
  @NotEmpty(groups = { CreateUser.class, UpdateUser.class })
  @Size(groups = { CreateUser.class, UpdateUser.class }, min = 8, max = 60)
  private String password;


  @OneToMany(mappedBy = "user")
  @JsonProperty(access = Access.WRITE_ONLY)
  private List<Task> tasks = new ArrayList<Task>();


}
