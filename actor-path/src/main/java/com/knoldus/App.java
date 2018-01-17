package com.knoldus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

class Watch {

    public static void main(String[] args) throws InterruptedException {

        final ActorSystem actorSystem = ActorSystem.create("Watch-actor-selection");
        final ActorRef counter = actorSystem.actorOf(Props.create(Counter.class), "counter");
        final ActorRef watcher = actorSystem.actorOf(Props.create(Watcher.class), "watcher");
        Thread.sleep(1000);
        actorSystem.terminate();
    }
}
