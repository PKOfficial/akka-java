package com.knoldus;

import java.util.*;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class RouterApp {

  public static void main(String[] args) throws InterruptedException {

    ActorSystem system = ActorSystem.create("router");

    ActorRef router = system.actorOf(Props.create(RouterPool.class));

    router.tell(new Worker.Work(), ActorRef.noSender());
    router.tell(new Worker.Work(), ActorRef.noSender());
    router.tell(new Worker.Work(), ActorRef.noSender());
    router.tell(new Worker.Work(), ActorRef.noSender());

    Thread.sleep(100);

    system.actorOf(Props.create(Worker.class), "worker1");
    system.actorOf(Props.create(Worker.class), "worker2");
    system.actorOf(Props.create(Worker.class), "worker3");
    system.actorOf(Props.create(Worker.class), "worker4");

    List<String> workers = new ArrayList<>();
    workers.add("/user/worker1");
    workers.add("/user/worker2");
    workers.add("/user/worker3");
    workers.add("/user/worker4");

    ActorRef routerGroup = system.actorOf(Props.create(RouterGroup.class, workers));

    routerGroup.tell(new Worker.Work(),ActorRef.noSender());
    routerGroup.tell(new Worker.Work(),ActorRef.noSender());
    routerGroup.tell(new Worker.Work(),ActorRef.noSender());

    Thread.sleep(100);

    system.terminate();

  }

}
