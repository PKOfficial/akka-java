package com.knoldus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

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

