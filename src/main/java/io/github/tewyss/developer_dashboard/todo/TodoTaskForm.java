package io.github.tewyss.developer_dashboard.todo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class TodoTaskForm {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Status is required")
    private TodoStatus status = TodoStatus.TODO;

    @NotNull(message = "Priority is required")
    private TodoPriority priority = TodoPriority.MEDIUM;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;
}
