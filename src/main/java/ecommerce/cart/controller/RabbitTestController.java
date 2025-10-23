package ecommerce.cart.controller;

import ecommerce.cart.dto.RabbitMessageDTO;
import ecommerce.cart.service.rabbit.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rabbit-test")
@RequiredArgsConstructor
public class RabbitTestController {
  private final RabbitPublisher publisher;

  @PostMapping("/publish")
  public ResponseEntity<Void> publish(@RequestBody RabbitMessageDTO payload) {
    publisher.send(payload);
    return ResponseEntity.ok().build();
  }
}
