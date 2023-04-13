package com.marcosguibson.todosimple.models.enuns;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileEnum {


  // ! Cria os tipos de usuario
  ADMIN(1, "ROLE_ADMIN"),
  USER(2, "ROLE_USER");

  private Integer code;
  private String description;

  // *Cria uma funcao para verificar o code de cada user para transformar o codigo no tipo de usuario
  public static ProfileEnum toEnum(Integer code) {
    if(Objects.isNull(code)){
      return null;
    }
    for(ProfileEnum x : ProfileEnum.values()){
      if(code.equals(x.getCode())){
        return x;
      }
    }

    throw new IllegalArgumentException("Invalide code: " + code);
  }
  
}
