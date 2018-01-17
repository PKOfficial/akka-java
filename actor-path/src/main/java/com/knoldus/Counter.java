package com.knoldus;

import akka.actor.AbstractActor;

class Counter extends AbstractActor {
    Integer count = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Inc.class, number -> count += number.number)
                .match(Dec.class, number -> count -= number.number)
                .build();
    }

    static class Inc {
        private Integer number;

        Inc(Integer number) {
            this.number = number;
        }
    }

    static class Dec {
        private Integer number;

        Dec(Integer number) {
            this.number = number;
        }
    }
}
