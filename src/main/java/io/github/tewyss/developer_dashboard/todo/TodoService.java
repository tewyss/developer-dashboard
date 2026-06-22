package io.github.tewyss.developer_dashboard.todo;

import java.util.List;

import io.github.tewyss.developer_dashboard.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoTaskRepository todoTaskRepository;

    public TodoService(TodoTaskRepository todoTaskRepository) {
        this.todoTaskRepository = todoTaskRepository;
    }

    public List<TodoTask> findAll() {
        return todoTaskRepository.findAllByOrderByCreatedAtDesc();
    }

    public TodoTask getById(Long id) {
        return todoTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: id " + id));
    }

    public long count() {
        return todoTaskRepository.count();
    }

    public long countOpen() {
        return todoTaskRepository.countByStatusNot(TodoStatus.DONE);
    }

    public long countDone() {
        return todoTaskRepository.countByStatus(TodoStatus.DONE);
    }

    public TodoTaskForm getEditForm(Long id) {
        TodoTask task = getById(id);
        TodoTaskForm form = new TodoTaskForm();
        form.setId(task.getId());
        form.setTitle(task.getTitle());
        form.setDescription(task.getDescription());
        form.setStatus(task.getStatus());
        form.setPriority(task.getPriority());
        form.setDeadline(task.getDeadline());
        return form;
    }

    @Transactional
    public TodoTask create(TodoTaskForm form) {
        TodoTask task = new TodoTask();
        applyForm(task, form);
        return todoTaskRepository.save(task);
    }

    @Transactional
    public TodoTask update(Long id, TodoTaskForm form) {
        TodoTask task = getById(id);
        applyForm(task, form);
        return todoTaskRepository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        if (!todoTaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found: id " + id);
        }
        todoTaskRepository.deleteById(id);
    }

    @Transactional
    public void changeStatus(Long id, TodoStatus status) {
        TodoTask task = getById(id);
        task.setStatus(status);
        todoTaskRepository.save(task);
    }

    private void applyForm(TodoTask task, TodoTaskForm form) {
        task.setTitle(form.getTitle());
        task.setDescription(emptyToNull(form.getDescription()));
        task.setStatus(form.getStatus());
        task.setPriority(form.getPriority());
        task.setDeadline(form.getDeadline());
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
