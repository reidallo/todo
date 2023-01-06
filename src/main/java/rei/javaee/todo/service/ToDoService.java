package rei.javaee.todo.service;

import rei.javaee.todo.entity.ToDo;
import rei.javaee.todo.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Stateless
public class ToDoService {

    @Inject
    private EntityManager entityManager;
    @Inject
    private QueryService queryService;
    @Inject
    private SecurityUtil securityUtil;
    @Context
    private SecurityContext securityContext;

//    private String email;
//
//    @PostConstruct
//    private void init() {
//        email = "";
//    }

    // if there is another transaction happening, it will be added to this transaction
    // if not, a new transaction will be created ** REQUIRED **
//    @Transactional(REQUIRED)
    public ToDo createToDo(ToDo toDo) {

        Principal principal = securityContext.getUserPrincipal();
        User user = queryService.findUserByEmail(securityContext.getUserPrincipal().getName());
        if (user != null) {
            toDo.setToDoOwner(user);
            entityManager.persist(toDo);
        }
        return toDo;
    }

    public User saveUser(User user) {

        Long count = (Long) queryService.countUserByEmail(user.getEmail()).get(0);

        // check if this email already exists on the database
        if (user.getId() == null && count == 0) {
            Map<String, String> credentialsMap = securityUtil.hashPassword(user.getPassword());
            // set the hashed password to the user
            user.setPassword(credentialsMap.get(SecurityUtil.HASHED_PASSWORD_KEY));
            // set the salt key to the user
            user.setSalt(credentialsMap.get(SecurityUtil.SALT_KEY));

            entityManager.persist(user);
            credentialsMap.clear();
        } else {
            throw new SecurityException("User already exists!");
        }
        return user;
    }

//    @Transactional(REQUIRED)
    public ToDo updateToDo(ToDo toDo) {
        entityManager.merge(toDo);
        return toDo;
    }

    public ToDo findToDo(Long id) {
        return queryService.findToDoById(id);
    }

    public List<ToDo> findAll() {
        return queryService.getAllToDo();
    }

//    @Transactional(REQUIRED)
    public ToDo completeTask(Long id) {
        ToDo toDo = queryService.findToDoById(id);
        toDo.setCompleted(true);
        toDo = updateToDo(toDo);
        return toDo;
    }
}
