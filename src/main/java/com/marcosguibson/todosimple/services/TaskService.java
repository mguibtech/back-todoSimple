package com.marcosguibson.todosimple.services;

import com.marcosguibson.todosimple.models.Task;
import com.marcosguibson.todosimple.models.User;
import com.marcosguibson.todosimple.models.enuns.ProfileEnum;
import com.marcosguibson.todosimple.repositories.TaskRepository;
import com.marcosguibson.todosimple.security.UserSpringSecurity;
import com.marcosguibson.todosimple.services.exceptions.AuthorizationException;
import com.marcosguibson.todosimple.services.exceptions.DataBindingViolationException;
import com.marcosguibson.todosimple.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = userService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task)) {
            throw new AuthorizationException("Acesso negado!");
        }

        return task;
    }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = userService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Acesso negado!");
        }
        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é posível deletar pois há entidades relacionadas");
        }
    }

    public List<Task> findAllByUser() {
        UserSpringSecurity userSpringSecurity = userService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Acesso negado!");
        }
        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }

}
