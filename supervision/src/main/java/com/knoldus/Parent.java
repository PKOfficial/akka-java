package com.knoldus;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.*;

class Parent extends AbstractActor implements Exceptions {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(10, Duration.create(1, TimeUnit.MINUTES), DeciderBuilder.
                    match(ResumeException.class, e -> resume()).
                    match(StopException.class, e -> stop()).
                    match(RestartException.class, e -> restart()).
                    matchAny(o -> escalate()).build());

    @Override
    public Receive createReceive() {
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
