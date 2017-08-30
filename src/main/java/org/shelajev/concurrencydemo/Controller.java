package org.shelajev.concurrencydemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

      byte[][] result = new byte[1][];
      PetersonLock lock = new PetersonLock();
      String url0 = urls[0];
      String url1 = urls[1];

      Thread t1 = new Thread(()-> {
        lock.flag0 = true;
        lock.turn = 1;

        while(lock.flag1 && lock.turn == 1) {
          //busy wait
        }
        //critical section
        byte[] bytes = getBytes(url0);
        if(result[0] == null) {
          result[0] = bytes;
        }

        lock.flag0 = false;
      });

      Thread t2 = new Thread(()-> {
        lock.flag1 = true;
        lock.turn = 0;

        while(lock.flag0 && lock.turn == 0) {
          //busy wait
        }
        //critical section
        byte[] bytes = getBytes(url1);
        if(result[0] == null) {
          result[0] = bytes;
        }

        lock.flag1 = false;
      });

      t1.start(); t2.start();
      while(result[0] == null) {
        Misc.sleep(200);
      }
      return result[0];
    };
  }

  private byte[] getBytes(String url) {
    stall();
    return restTemplate.getForObject(url, byte[].class);
  }
}
