package rei.javaee.todo.service;

import rei.javaee.todo.entity.ToDo;
import rei.javaee.todo.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Stateless
public class QueryService {

    @Inject
    private EntityManager entityManager;
    @Inject
    private SecurityUtil securityUtil;
    @Context
    private SecurityContext securityContext;

    public User findUserByEmail(String email) {
        List<User> userList = entityManager.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList();
        if (!userList.isEmpty())
            return userList.get(0);
        return null;
    }

    public List countUserByEmail(String email) {
       return entityManager.createNativeQuery("SELECT count(id) FROM todo_user WHERE exists(SELECT id FROM todo_user WHERE email = ?)")
                .setParameter(1, email).getResultList();
    }

    public boolean authenticateUser(String email, String password) {
        User user = findUserByEmail(email);
        if (user == null) {
            return false;
        }
        return securityUtil.passwordsMatch(user.getPassword(), user.getSalt(), password);
    }

    public ToDo findToDoById(Long id) {
        List<ToDo> resultList = entityManager.createNamedQuery(ToDo.FIND_TODO_BY_ID, ToDo.class)
                .setParameter("id", id)
                .setParameter("email", securityContext.getUserPrincipal().getName())
                .getResultList();
        if (!resultList.isEmpty())
            return resultList.get(0);
        return null;
    }

    public List<ToDo> getAllToDo() {
        return entityManager.createNamedQuery(ToDo.FIND_ALL_TODO_BY_USER, ToDo.class)
                .setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();
    }

    public List<ToDo> getAllToDoByTask(String task) {
        return entityManager.createNamedQuery(ToDo.FIND_TODO_BY_TASK, ToDo.class)
                .setParameter("email", securityContext.getUserPrincipal().getName())
                .setParameter("task", "%" + task + "%")
                .getResultList();
    }
}
