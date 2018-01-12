package com.knoldus;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import akka.routing.RandomGroup;

public class Random {

  public static void main(String[] args) throws InterruptedException {

    ActorSystem system = ActorSystem.create("Random-Router");

    ActorRef routerPool =
        system.actorOf(new FromConfig().props(Props.create(Worker.class)), "random-router-pool");

    routerPool.tell(new Worker.Work(), ActorRef.noSender());
    routerPool.tell(new Worker.Work(), ActorRef.noSender());
    routerPool.tell(new Worker.Work(), ActorRef.noSender());
    routerPool.tell(new Worker.Work(), ActorRef.noSender());

    Thread.sleep(100);

    system.actorOf(Props.create(Worker.class), "worker1");
    system.actorOf(Props.create(Worker.class), "worker2");
    system.actorOf(Props.create(Worker.class), "worker3");

    List<String> paths = new ArrayList<>();
    paths.add("/user/worker1");
    paths.add("/user/worker2");
    paths.add("/user/worker3");

    ActorRef routerGroup = system.actorOf(new RandomGroup(paths).props(), "random-router-group");

    routerGroup.tell(new Worker.Work(), ActorRef.noSender());
    routerGroup.tell(new Worker.Work(), ActorRef.noSender());
    routerGroup.tell(new Worker.Work(), ActorRef.noSender());
    routerGroup.tell(new Worker.Work(), ActorRef.noSender());

    Thread.sleep(100);

    system.terminate();

  }

}
