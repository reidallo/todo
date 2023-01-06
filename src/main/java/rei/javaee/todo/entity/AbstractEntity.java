package rei.javaee.todo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

// A class designated with the MappedSuperclass annotation can be mapped in the same way as an entity except that
// the mappings will apply only to its subclasses since no table exists for the mapped superclass itself
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
