package com.marcosguibson.todosimple.repositories;

import com.marcosguibson.todosimple.models.Task;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

//    Optional<Task> findById(Long id); ja tem no jpaRepository

    List<Task> findByUser_Id(Long id);

    //Usando jpql
//    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
//    List<Task> findByUser_Id(@Param("id") Long id);

    //Usando o SQL nativo
//    @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
//    List<Task> findByUser_Id(@Param("id") Long id);
}
