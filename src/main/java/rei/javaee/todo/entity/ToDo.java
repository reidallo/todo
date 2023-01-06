package rei.javaee.todo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "todo")
@NamedQuery(name = ToDo.FIND_TODO_BY_TASK, query = "SELECT t FROM ToDo t WHERE t.task LIKE :task AND t.toDoOwner.email = :email")
@NamedQuery(name = ToDo.FIND_ALL_TODO_BY_USER, query = "SELECT t FROM ToDo t WHERE t.toDoOwner.email = :email")
@NamedQuery(name = ToDo.FIND_TODO_BY_ID , query = "SELECT t FROM ToDo t WHERE t.id = :id AND t.toDoOwner.email =: email")
public class ToDo extends AbstractEntity{

    public static final String FIND_TODO_BY_TASK = "ToDo.findByTask";
    public static final String FIND_ALL_TODO_BY_USER = "ToDo.findByUser";
    public static final String FIND_TODO_BY_ID = "ToDo.findById";

    @NotEmpty(message = "Task must not be empty!")
    private String task;

    @Column(name = "due_date")
    @NotNull(message = "Due date must not be null!")
    @FutureOrPresent(message = "Due date must not be in the past!")
    // Format that will be the same in JSON and in JAVA
    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate dueDate;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "completed")
    private boolean isCompleted;

    @ManyToOne
    private User toDoOwner;

    // this method will be invoked just before this entity is persisted into the database **Lifecycle Callback**
    @PrePersist
    private void init() {
        setCreatedDate(LocalDate.now());
    }
}

