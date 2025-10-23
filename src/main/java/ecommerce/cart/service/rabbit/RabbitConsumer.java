package ecommerce.cart.service.rabbit;

import ecommerce.cart.config.RabbitMQConfig;
import ecommerce.cart.dto.RabbitMessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {

  @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
  public void receive(RabbitMessageDTO data) {
    System.out.println("Mensaje recibido: " + data);
  }
}
