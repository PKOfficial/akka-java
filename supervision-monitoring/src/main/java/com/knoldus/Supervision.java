package com.knoldus;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.Terminated;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.escalate;

/**
 * Author prabhat
 * Created on 5/9/17
 */
public class Supervision {

  public static void main(String[] args) throws InterruptedException {
    ActorSystem system = ActorSystem.create("supervision-monitoring");
    ActorRef parent = system.actorOf(Props.create(Parent.class), "parent");
    system.eventStream().subscribe(parent, DeadLetter.class);
    final Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

    Patterns.ask(parent, new Parent.ActorCreator(Props.create(Child.class), "child-stop"), timeout).onSuccess(
        new OnSuccess<Object>() {
          @Override public void onSuccess(Object actorObject) throws Throwable {
            if(actorObject instanceof ActorRef){
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

class Child extends AbstractActor implements Exceptions {

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  @Override public void preStart() {
    ActorPath actorPath = self().path();
    log.info("Child preStart with pathL " + actorPath);
  }

  @Override public void preRestart(Throwable reason, Optional<Object> message) {
    log.info("Child preRestart - " + message + " - " + reason);
  }

  @Override public void postStop() {
    log.info("Child Stop");
  }

  @Override public Receive createReceive() {
    return receiveBuilder().match(Stop.class, m -> {
      throw new StopException();
    }).match(Restart.class, m -> {
      throw new RestartException();
    }).match(Resume.class, m -> {
      throw new ResumeException();
    }).build();
  }

  static class Stop {
  }

  static class Restart {
  }

  static class Resume {
  }

}

class Parent extends AbstractActor implements Exceptions {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  private static SupervisorStrategy strategy =
      new OneForOneStrategy(10, Duration.create(1, TimeUnit.MINUTES), DeciderBuilder.
          match(ResumeException.class, e -> resume()).
          match(StopException.class, e -> stop()).
          match(RestartException.class, e -> restart()).
          matchAny(o -> escalate()).build());

  @Override public Receive createReceive() {
    return receiveBuilder().match(Terminated.class, actor -> context().unwatch(actor.actor()))
        .match(ActorCreator.class, actor -> {
          ActorRef child = context().actorOf(actor.props, actor.name);
          sender().tell(child, ActorRef.noSender());
          context().watch(child);
        })
        .build();
  }

  static class ActorCreator {

    private Props props;
    private String name;

    ActorCreator(Props props, String name) {
      this.props = props;
      this.name = name;
    }

  }

}

interface Exceptions {

  class ResumeException extends Exception {

  }

  class StopException extends Exception {

  }

  class RestartException extends Exception {

  }

}
