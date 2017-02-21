package com.aaa

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}
import com.aaa.PingActor._
import com.aaa.PongActor._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by borisbondarenko on 13.02.17.
  */
class SupervisorActor(velocity: Long)
  extends Actor
    with ActorLogging {

  def getPing = context.actorOf(PingActor.props)
  def getPong = context.actorOf(PongActor.props)

  val pingActor: ActorRef = getPing
  val pongActor: ActorRef = getPong

  val timeout = velocity.milliseconds
  val scheduler = context.system.scheduler

  scheduler.scheduleOnce(timeout, pingActor, PingRequest)

  override def preStart(): Unit =
    log.info("It's time to have some ping-pong session!!")

  override def receive: Receive = pingAwait

  def pingAwait: Receive = ({
    case Ping =>
      log.info("PING!")
      scheduler.scheduleOnce(timeout, pongActor, PongRequest)
      context become pongAwait
  }: Receive) orElse defaultMsgHandler

  def pongAwait: Receive = ({
    case Pong =>
      log.info("PONG!")
      scheduler.scheduleOnce(timeout, pingActor, PingRequest)
      context become pingAwait
  }: Receive) orElse defaultMsgHandler

  def defaultMsgHandler: Receive = {
    case _ => log.warning("WTF??!")
  }

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.second) {
      case _: Exception => Restart
    }
}

object SupervisorActor {
  def props(v: Long): Props = Props(new SupervisorActor(v))
}
