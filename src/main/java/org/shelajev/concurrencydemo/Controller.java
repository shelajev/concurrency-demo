package org.shelajev.concurrencydemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.shelajev.concurrencydemo.Misc.stall;

@RestController
public class Controller {

  private static final Logger log = LoggerFactory.getLogger(Controller.class);

  RestTemplate restTemplate;

  public Controller(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<byte[]> get() {
    byte[] image = getSolution().getFirst();
    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
  }

  private Problem getSolution() {
    return urls -> Arrays.asList(urls).stream().parallel().map(this::getBytes).findAny().get();
  }

  private byte[] getBytes(String url) {
    stall();
    return restTemplate.getForObject(url, byte[].class);
  }
}
