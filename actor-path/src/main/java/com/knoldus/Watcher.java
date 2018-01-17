package com.knoldus;

import akka.actor.*;

class Watcher extends AbstractActor {

    public Watcher() {

        final Integer identifyId = 1;
        ActorSelection selection = context().actorSelection("/user/counter");
        selection.tell(new Identify(identifyId), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ActorIdentity.class, ref -> ref.getRef() != null, ref ->
                System.out.println("Actor Reference for counter is " + ref.getRef()))
                .match(ActorIdentity.class, ref -> ref.getRef() == null, ref ->
                        System.out.println("Actor selection for actor doesn't live :( "))
                .build();
    }
}
