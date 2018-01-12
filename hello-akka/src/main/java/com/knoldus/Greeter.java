package com.knoldus;

import akka.actor.AbstractActor;

// Define Greeter Actor
class Greeter extends AbstractActor {

    @Override public Receive createReceive() {
        return receiveBuilder().match(WhoToGreet.class, who -> System.out.println("Hello " + who.who)).build();
    }

    static class WhoToGreet {
        private String who;

        WhoToGreet(String who) {
            this.who = who;
        }

    }

}