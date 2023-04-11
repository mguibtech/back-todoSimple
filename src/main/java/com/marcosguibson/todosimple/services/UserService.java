package com.marcosguibson.todosimple.services;

import com.marcosguibson.todosimple.models.User;
import com.marcosguibson.todosimple.repositories.TaskRepository;
import com.marcosguibson.todosimple.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
//    Autowired e como se fosse o construtor no spring
    @Autowired
    private UserRepository userRepository;



    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
//        Se o usuario existir, ele retorna o user, caso contrario ele retornar um exception
        return user.orElseThrow(()-> new RuntimeException(
                "Usuario nao encontrado! Id: " + id + ", Tipo: "+ User.class.getName()
        ));
    }
//    Usar sempre que for salvar alguma coisa no banco, para ter um controle melhor (criar, atualizar)
    @Transactional
    public User create(User obj){
        obj.setId(null);
        obj = this.userRepository.save(obj);

        return obj;
    }

    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());

        return this.userRepository.save(newObj);
    }

    public  void delete(Long id){
        findById(id);
        try {
            this.userRepository.deleteById(id);
        }catch (Exception e){
            throw  new RuntimeException("Nao e possivel excluir pois ha entidades relacionadas");

        }
    }


}
