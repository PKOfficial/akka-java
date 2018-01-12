package com.knoldus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;

import java.util.concurrent.TimeUnit;

public class Supervision {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("supervision-monitoring");
        ActorRef parent = system.actorOf(Props.create(Parent.class), "parent");
        system.eventStream().subscribe(parent, DeadLetter.class);
        final Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

        Patterns.ask(parent, new Parent.ActorCreator(Props.create(Child.class), "child-stop"), timeout).onSuccess(
                new OnSuccess<Object>() {
                    @Override
                    public void onSuccess(Object actorObject) throws Throwable {
                        if (actorObject instanceof ActorRef) {
                            ActorRef child = (ActorRef) actorObject;
                            child.tell(new Child.Stop(), ActorRef.noSender());
                        }
                    }
                }, system.dispatcher());

        Thread.sleep(1000);

    /*Patterns.ask(parent, new Parent.ActorCreator(Props.create(Child.class), "child-resume"), timeout).onSuccess(
        new OnSuccess<Object>() {
          @Override public void onSuccess(Object actorObject) throws Throwable {
            if(actorObject instanceof ActorRef){
              ActorRef child = (ActorRef) actorObject;
              child.tell(new Child.Resume(), ActorRef.noSender());
            }
          }
        }, system.dispatcher());*/

    /*Patterns.ask(parent, new Parent.ActorCreator(Props.create(Child.class), "child-restart"), timeout).onSuccess(
        new OnSuccess<Object>() {
          @Override public void onSuccess(Object actorObject) throws Throwable {
            if(actorObject instanceof ActorRef){
              ActorRef child = (ActorRef) actorObject;
              child.tell(new Child.Restart(), ActorRef.noSender());
            }
          }
        }, system.dispatcher());*/

        Thread.sleep(1000);
        system.terminate();
    }

}





