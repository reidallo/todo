package rei.javaee.todo.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import rei.javaee.todo.entity.User;
import rei.javaee.todo.service.SecurityUtil;
import rei.javaee.todo.service.ToDoService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Path("/user")
public class UserRest {

    @Inject
    private SecurityUtil securityUtil;
    @Context
    private UriInfo uriInfo;
    @Inject
    private ToDoService toDoService;

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@NotNull @QueryParam("email") String email, @NotNull @QueryParam("password") String password) {

        boolean authenticated = securityUtil.authenticateUser(email, password);
        if (!authenticated) {
            throw new SecurityException("Email/Password not valid!");
        }
        String token = generateToken(email);
        return Response.ok().header(HttpHeaders.AUTHORIZATION, SecurityUtil.BEARER + " " + token).build();
//        return Response.ok(token).build();
    }

    private String generateToken(String email) {
        Key securityKey = securityUtil.getSecurityKey();
        return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setIssuer(uriInfo.getBaseUri().toString())
                .setAudience(uriInfo.getAbsolutePath().toString())
                .setExpiration(securityUtil.toDate(LocalDateTime.now().plusMinutes(20))) //expiration
                .signWith(SignatureAlgorithm.HS512, securityKey)
                .compact();
    }

    @POST
    @Path(("/create"))
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUser(@Valid User user) {
        toDoService.saveUser(user);
        return Response.ok(user).build();
    }
}
