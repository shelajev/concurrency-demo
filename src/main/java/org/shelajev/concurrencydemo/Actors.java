package org.shelajev.concurrencydemo;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;

public class Actors {
  public static class Url2Fetch {
    public String url;
    Url2Fetch(String url) {this.url = url;}
  }

  public static class Result {
    public byte[] result;
    Result(byte[] result) {this.result = result;}
  }

  public static class UrlFetcher extends AbstractActor {

    private final RestTemplate restTemplate;
    private AtomicReference<byte[]> result;

    public UrlFetcher(RestTemplate restTemplate, AtomicReference<byte[]> result) {
      this.restTemplate = restTemplate;
      this.result = result;
    }

    static Props props(RestTemplate restTemplate, AtomicReference<byte[]> result) {
      return Props.create(UrlFetcher.class, () -> new UrlFetcher(restTemplate, result));
    }

    @Override public Receive createReceive() {
      return receiveBuilder()
        .match(Url2Fetch.class, u -> {
          byte[] bytes = restTemplate.getForObject(u.url, byte[].class);
          result.compareAndSet(null, bytes);
          getSender().tell(new Result(bytes), getSelf());
        })
        //        .matchAny()
        .build();
    }
  }


}
