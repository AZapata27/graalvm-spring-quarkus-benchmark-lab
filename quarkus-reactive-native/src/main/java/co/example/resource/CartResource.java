package co.example.resource;

import co.example.model.Cart;
import co.example.service.CartService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private final CartService cartService;

    @GET
    public Uni<Response> getAll() {
        return cartService.findAll()
                .onItem().transform(carts -> Response.ok(carts).build());
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return cartService.findById(id)
                .onItem().transform(cart -> {
                    if (cart != null) {
                        return Response.ok(cart).build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                });
    }

    @POST
    public Uni<Response> create(Cart cart) {
        return cartService.create(cart)
                .onItem().transform(createdCart -> Response.status(Response.Status.CREATED).entity(createdCart).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id") Long id, Cart cart) {
        cart.setId(id);
        return cartService.update(cart)
                .onItem().transform(updatedCart -> {
                    if (updatedCart != null) {
                        return Response.ok(updatedCart).build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                });
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return cartService.delete(id)
                .onItem().transform(deleted -> Response.status(Response.Status.NO_CONTENT).build());
    }
}
