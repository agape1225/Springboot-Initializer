package com.springboot.initialize_project.data.repository;

import com.springboot.initialize_project.data.entity.ToDo;
import com.springboot.initialize_project.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Page<ToDo> findAllByUserAndState(User user, boolean state, Pageable pageable);
    Page<ToDo> findAllByUser(User user, Pageable pageable);

}
