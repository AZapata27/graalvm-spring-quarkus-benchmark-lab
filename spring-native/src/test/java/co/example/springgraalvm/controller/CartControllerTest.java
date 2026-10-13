package co.example.springgraalvm.controller;

import co.example.springgraalvm.model.entity.Cart;
import co.example.springgraalvm.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @MockBean is not supported by Spring AOT test processing (processTestAot / nativeTest).
@DisabledInAotMode
@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private static Cart cart(long id, long userId) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserId(userId);
        cart.setItems(List.of());
        return cart;
    }

    @Test
    void getAllCartsReturnsList() throws Exception {
        when(cartService.getAllCarts()).thenReturn(List.of(cart(1, 10), cart(2, 20)));

        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].userId").value(20));
    }

    @Test
    void getCartByIdReturnsCart() throws Exception {
        when(cartService.getCartById(1L)).thenReturn(Optional.of(cart(1, 10)));

        mockMvc.perform(get("/api/carts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10));
    }

    @Test
    void getCartByIdReturns404WhenMissing() throws Exception {
        when(cartService.getCartById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/carts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCartReturns201WithSavedCart() throws Exception {
        when(cartService.saveCart(any(Cart.class))).thenReturn(cart(5, 50));

        mockMvc.perform(post("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 50, \"items\": []}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.userId").value(50));
    }

    @Test
    void deleteCartReturns204() throws Exception {
        mockMvc.perform(delete("/api/carts/3"))
                .andExpect(status().isNoContent());

        verify(cartService).deleteCart(3L);
    }
}
