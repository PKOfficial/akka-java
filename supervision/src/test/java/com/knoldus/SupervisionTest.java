package com.knoldus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

public class SupervisionTest {

    private static ActorSystem system;

    @BeforeClass public static void setup() {
        system = ActorSystem.create("Tester");
    }

    @AfterClass public static void tearDown() {
        TestKit.shutdownActorSystem(system);
    }

    @Test public void testEr() throws Exception {
        Duration timeout = Duration.Inf();
        TestActorRef<Parent> supervisor = TestActorRef.create(system, Props.create(Parent.class));
        ActorRef child = (ActorRef) Await.result(Patterns
                .ask(supervisor, new Parent.ActorCreator(Props.create(Child.class), "testChild"), 5000), timeout);
        child.tell(new Child.Restart(), ActorRef.noSender());
    }

}