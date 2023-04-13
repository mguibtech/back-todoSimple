package com.marcosguibson.todosimple.services.exceptions;


import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// public class DataBindingViolationException {
  
// }

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataBindingViolationException extends DataIntegrityViolationException {

  public DataBindingViolationException(String message){
    super(message);
  }
  
}
