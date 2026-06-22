package io.github.tewyss.developer_dashboard.todo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {

    List<TodoTask> findAllByOrderByCreatedAtDesc();

    long countByStatus(TodoStatus status);

    long countByStatusNot(TodoStatus status);
}
