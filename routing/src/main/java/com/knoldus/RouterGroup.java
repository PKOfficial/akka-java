package com.knoldus;

import java.util.*;
import java.util.Random;

import akka.actor.AbstractActor;

public class RouterGroup extends AbstractActor {

  private List<String> routees;

  RouterGroup(List<String> routees) {
    this.routees = routees;
  }

  @Override public Receive createReceive() {
    return receiveBuilder().match(Worker.Work.class, m -> {
      Integer random = new Random().nextInt(routees.size());
      System.out.println("[Router Group] : Received Message");
      context().actorSelection(routees.get(random)).forward(m, context());
    }).build();
  }
}
