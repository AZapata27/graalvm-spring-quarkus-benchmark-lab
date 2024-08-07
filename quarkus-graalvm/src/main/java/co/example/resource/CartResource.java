package co.example.resource;

import co.example.model.Cart;
import co.example.service.CartService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {


    private final CartService cartService;

    public CartResource(CartService cartService) {
        this.cartService = cartService;
    }

    @GET
    public List<Cart> getAll() {
        return cartService.findAll();
    }

    @GET
    @Path("/{id}")
    public Cart getById(@PathParam("id") Long id) {
        return cartService.findById(id);
    }

    @POST
    public Response create(Cart cart) {
        cartService.create(cart);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Cart cart) {
        Cart existingCart = cartService.findById(id);
        if (existingCart == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        cart.setId(id);
        cartService.update(cart);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        cartService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
