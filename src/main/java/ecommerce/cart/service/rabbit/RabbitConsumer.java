package ecommerce.cart.service.rabbit;

import ecommerce.cart.config.RabbitMQConfig;
import ecommerce.cart.model.ShoppingCart;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {

  @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
  public void receive(ShoppingCart data) {
    System.out.println("Mensaje recibido: " + data);
  }
}
