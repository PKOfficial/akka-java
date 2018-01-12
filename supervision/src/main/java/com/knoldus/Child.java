package com.knoldus;

import akka.actor.AbstractActor;
import akka.actor.ActorPath;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Optional;

class Child extends AbstractActor implements Exceptions {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public void preStart() {
        ActorPath actorPath = self().path();
        log.info("Child preStart with pathL " + actorPath);
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) {
        log.info("Child preRestart - " + message + " - " + reason);
    }

    @Override
    public void postStop() {
        log.info("Child Stop");
    }

    @Override
    public Receive createReceive() {
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
