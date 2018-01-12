package com.knoldus;

import akka.actor.AbstractActor;

public class Worker extends AbstractActor {

  @Override public Receive createReceive() {
    return receiveBuilder()
        .match(Work.class, m -> System.out.println("Message Received at : " + self())).build();
  }

  static class Work {
  }

}
