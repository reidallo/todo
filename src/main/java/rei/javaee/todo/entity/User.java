package rei.javaee.todo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "todo_user")
@NamedQuery(name = User.FIND_ALL_USERS, query = "SELECT u FROM User u ORDER BY u.fullName")
@NamedQuery(name = User.FIND_USER_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email")
@NamedQuery(name = User.FIND_USER_BY_PASSWORD, query = "SELECT u FROM User u WHERE u.password = :password")
public class User extends AbstractEntity{

    public static final String FIND_ALL_USERS = "User.findAllUsers";
    public static final String FIND_USER_BY_EMAIL = "User.findByEmail";
    public static final String FIND_USER_BY_PASSWORD = "User.findByPassword";

    @NotNull(message = "Full name must be set!")
//    @Pattern(regexp = "", message = "Full name must be alphabets")
    @Column(name = "full_name")
    private String fullName;

    @NotNull(message = "Email must be set!")
    @Email(message = "Email must be in the form user@domain.com")
    private String email;

    @NotNull(message = "Password must be set!")
    @Size(min = 8, message = "Password must have at least 8 characters!")
//    @Pattern(regexp = "", message = "Password must be a combination of alphabets, numbers and special characters!")
    private String password;

    private String salt;

//    @OneToMany
//    private final Collection<ToDo> toDos = new ArrayList<>();
}
