package com.knoldus;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Author prabhat
 * Created on 5/9/17
 */

public class HelloAkka {

  public static void main(String[] args) {

    // Create the 'hello akka' actor system
    ActorSystem system = ActorSystem.create("hello-akka");

    // Create the 'greeter' actor
    ActorRef greeter = system.actorOf(Props.create(Greeter.class), "greeter");

    // Send WhoToGreet Message to actor
    greeter.tell(new Greeter.WhoToGreet("Salma"),ActorRef.noSender());

    //shutdown actorsystem
    system.terminate();

  }

}

// Define Greeter Actor
class Greeter extends AbstractActor {

  @Override public Receive createReceive() {
    return receiveBuilder().match(WhoToGreet.class, who -> System.out.println("Hello " + who.who)).build();
  }

  static class WhoToGreet {
    private String who;

    WhoToGreet(String who) {
      this.who = who;
    }

  }

}
