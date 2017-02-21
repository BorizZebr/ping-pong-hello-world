package com.aaa

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by borisbondarenko on 13.02.17.
  */
class PongActor extends Actor with ActorLogging {

  import PongActor._

  override def receive: Receive = {
    case PongRequest =>
      sender ! Pong
      sender ! Pong
  }
}

object PongActor {

  def props: Props = Props(new PongActor)

  case object PongRequest
  case object Pong
}
