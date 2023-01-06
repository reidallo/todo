package rei.javaee.todo.service;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CDIProducer {

    @Produces
    @PersistenceContext(name = "todoPU")
    private EntityManager entityManager;
}
