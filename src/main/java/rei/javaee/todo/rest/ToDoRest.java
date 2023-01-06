package rei.javaee.todo.rest;

import rei.javaee.todo.annotation.Auth;
import rei.javaee.todo.entity.ToDo;
import rei.javaee.todo.service.QueryService;
import rei.javaee.todo.service.ToDoService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;


@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Auth
public class ToDoRest {

    @Inject
    private ToDoService toDoService;
    @Inject
    private QueryService queryService;

    @POST
    @Path("/create")
    public Response createToDo(@Valid ToDo toDo, @Context UriInfo uriInfo) {
        toDo = toDoService.createToDo(toDo);
        URI uri = uriInfo.getBaseUriBuilder().path(toDo.getId().toString()).build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("/update")
    public Response updateToDo(ToDo toDo) {
        toDoService.updateToDo(toDo);
        return Response.ok(toDo).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        ToDo toDo = queryService.findToDoById(id);
        if (toDo == null)
            return Response.noContent().build();
        else
            return Response.ok(toDo).build();
    }

    @GET
    @Path("/all")
    public Response getAllToDo() {
        List<ToDo> toDoList = queryService.getAllToDo();
        if (toDoList == null)
            return Response.noContent().build();
        else
            return Response.ok(toDoList).build();
    }

    @PUT
    @Path("/status")
    public Response setStatus(@QueryParam("id") Long id) {
        return Response.ok(toDoService.completeTask(id)).build();
    }
}
