package com.marcosguibson.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marcosguibson.todosimple.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  
  
}