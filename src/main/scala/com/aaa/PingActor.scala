package com.aaa

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by borisbondarenko on 13.02.17.
  */
class PingActor extends Actor with ActorLogging {

  import PingActor._

  var counter = 0
  var threshold = 10

  override def preRestart(reason: Throwable, message: Option[Any]): Unit =
    log.info("Time to restart ping")

  override def receive: Receive = {
    case PingRequest =>
      counter = counter + 1
      sender ! Ping
      if(counter > threshold) throw new Exception("I am tired...")
  }
}

object PingActor {

  def props: Props = Props(new PingActor)

  case object PingRequest
  case object Ping
}
