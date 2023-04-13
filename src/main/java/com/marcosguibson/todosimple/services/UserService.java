package com.marcosguibson.todosimple.services;

import com.marcosguibson.todosimple.models.User;
import com.marcosguibson.todosimple.models.enuns.ProfileEnum;
import com.marcosguibson.todosimple.repositories.TaskRepository;
import com.marcosguibson.todosimple.repositories.UserRepository;
import com.marcosguibson.todosimple.security.UserSpringSecurity;
import com.marcosguibson.todosimple.services.exceptions.AuthorizationException;
import com.marcosguibson.todosimple.services.exceptions.DataBindingViolationException;
import com.marcosguibson.todosimple.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Autowired e como se fosse o construtor no spring
    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity) ||
                !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())) {
            throw new AuthorizationException("Acesso negado!");
        }
        Optional<User> user = this.userRepository.findById(id);
        // Se o usuario existir, ele retorna o user, caso contrario ele retornar um
        // exception
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuario nao encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    // Usar sempre que for salvar alguma coisa no banco, para ter um controle melhor
    // (criar, atualizar)
    @Transactional
    public User create(User obj) {
        obj.setId(null);
        // ?ENCRIPTAR SENHA
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);

        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        // ?ENCRIPTAR SENHA
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));

        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Nao e possivel excluir pois ha entidades relacionadas");

        }
    }

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }

    }

}
