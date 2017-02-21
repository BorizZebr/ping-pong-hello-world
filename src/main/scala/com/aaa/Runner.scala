package com.aaa

import akka.actor.ActorSystem
import scala.io._

/**
  * Created by borisbondarenko on 13.02.17.
  */
object Runner extends App {

  val system = ActorSystem("ping-pong-system")
  system.actorOf(SupervisorActor.props(500))

  StdIn.readLine()
  system.terminate()
  println("terminated")
}
