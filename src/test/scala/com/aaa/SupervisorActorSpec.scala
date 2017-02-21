package com.aaa

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.aaa.PingActor.{Ping, PingRequest}
import com.aaa.PongActor.{Pong, PongRequest}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

/**
  * Created by borisbondarenko on 14.02.17.
  */
class SupervisorActorSpec
  extends TestKit(ActorSystem("test"))
    with WordSpecLike
    with BeforeAndAfterAll
    with DefaultTimeout
    with ImplicitSender {

  val velosity = 1000

  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "Supervisor actor" should {

    trait scope {
      val ping = TestProbe()
      val pong = TestProbe()

      val supervisor = TestActorRef(new SupervisorActor(velosity) {
        override def getPing: ActorRef = ping.ref
        override def getPong: ActorRef = pong.ref
      })
    }

    "should send ping request to ping actor" in new scope {
      ping expectMsg PingRequest
    }

    "should send pong request after receiving ping" in new scope {
      supervisor ! Ping
      pong expectMsg PongRequest
    }

    "should send only one ping request after receiving multiple pongs" in new scope {
      supervisor ! Pong
      ping expectMsg PingRequest
      supervisor ! Pong
      ping expectNoMsg
    }
  }
}
