package com.knoldus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import akka.routing.RoundRobinPool;

public class RoundRobin {

  public static void main(String[] args) throws InterruptedException {

    ActorSystem system = ActorSystem.create("round-robin-router");

    ActorRef routerPool = system.actorOf(new RoundRobinPool(5).props(Props.create(Worker.class)), "round-robin-pool");

    routerPool.tell(new Worker.Work(), ActorRef.noSender());
    routerPool.tell(new Worker.Work(), ActorRef.noSender());
    routerPool.tell(new Worker.Work(), ActorRef.noSender());
    routerPool.tell(new Worker.Work(), ActorRef.noSender());

    Thread.sleep(1000);

    system.actorOf(Props.create(Worker.class), "worker-1");
    system.actorOf(Props.create(Worker.class), "worker-2");
    system.actorOf(Props.create(Worker.class), "worker-3");

    ActorRef rounterGroup = system.actorOf(new FromConfig().props(), "round-robin-group");

    rounterGroup.tell(new Worker.Work(), ActorRef.noSender());
    rounterGroup.tell(new Worker.Work(), ActorRef.noSender());
    rounterGroup.tell(new Worker.Work(), ActorRef.noSender());
    rounterGroup.tell(new Worker.Work(), ActorRef.noSender());

    Thread.sleep(1000);

    system.terminate();

  }

}
