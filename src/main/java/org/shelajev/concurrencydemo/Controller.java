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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    return urls -> {
      CompletableFuture result = CompletableFuture.anyOf(Arrays.asList(urls).stream().map(
        (url) -> CompletableFuture.supplyAsync(() -> getBytes(url))
      ).collect(Collectors.toList()).toArray(new CompletableFuture[0]));

      try {
        return (byte[]) result.get();
      }
      catch (InterruptedException | ExecutionException e) {
        Misc.rethrow(e);
      }
      return null;
    };
  }

  private byte[] getBytes(String url) {
    stall();
    return restTemplate.getForObject(url, byte[].class);
  }
}
