package org.shelajev.concurrencydemo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
        AtomicReference<byte[]> result = new AtomicReference<>();
        ActorSystem system = ActorSystem.create("Search");

        for(int i = 0; i < urls.length; i++) {
          String url = urls[i];
          char c = (char) ('a' + i);
          final ActorRef q = system.actorOf(
            Actors.UrlFetcher.props(restTemplate, result), "loading_"+ c);

          Patterns.ask(q, new Actors.Url2Fetch(url), new Timeout(Duration.create(5, TimeUnit.SECONDS)));
        }
        while(result.get() == null) {
          Misc.sleep(200);
        }
        return result.get();
      };
  }

  private byte[] getBytes(String url) {
    stall();
    return restTemplate.getForObject(url, byte[].class);
  }
}
