package com.knoldus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class RouterPool extends AbstractActor {

  private List<ActorRef> routees = new ArrayList<>();

  @Override public void preStart() {
    for (int i = 0; i < 5; i++) {
      routees.add(context().actorOf(Props.create(Worker.class)));
    }
  }

  @Override public Receive createReceive() {
    return receiveBuilder()
        .match(Worker.Work.class, m -> {
          Integer random = new Random().nextInt(routees.size());
          System.out.println("[Router Pool] : Received Message");
          routees.get(random).forward(m, context());
        })
        .build();
  }

}
