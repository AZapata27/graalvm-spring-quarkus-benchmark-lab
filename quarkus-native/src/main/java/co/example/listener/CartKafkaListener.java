package co.example.listener;

import co.example.model.Cart;
import co.example.service.CartService;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.header.Headers;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@RequiredArgsConstructor
public class CartKafkaListener {

    private final CartService cartService;

    //@Incoming("carts-in")
    public void onMessage(Cart cart) {
        cartService.processCart(cart);
    }


    @Incoming("carts-in")
    @Outgoing("carts-out")
    public Cart processAndForward(Cart cart) {
        cartService.processCart(cart);
        cart.setStatus("PROCESSED");
        return cart;
    }

    //@Incoming("carts-in")
    public void processBatch(List<Cart> carts) {
        cartService.processBatch(carts);
    }



    //@Incoming("carts-in")
    public CompletionStage<Void> processWithAck(Message<Cart> message) {
        try {
            cartService.processCart(message.getPayload());
            return message.ack();
        } catch (Exception e) {
            return message.nack(e);
        }
    }


    //@Incoming("carts-in")
    public Uni<Void> processAsync(Cart cart) {
        return Uni.createFrom().item(cart)
                  .onItem().transform(this::processCart)
                  .onFailure().recoverWithItem(this::handleError);
    }

    //@Incoming("carts-in")
    public Uni<Void> processRawMessage(KafkaRecord<String, String> record) {
        String key = record.getKey();
        String value = record.getPayload();
        Headers headers = record.getHeaders();

        return null;
    }


    private Void handleError() {

        return null;
    }

    private Void processCart(Cart cart) {
        return null;
    }
}