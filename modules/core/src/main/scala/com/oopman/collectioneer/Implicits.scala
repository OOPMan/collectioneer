package com.oopman.collectioneer

import akka.actor.ActorSystem

// TODO: Maybe this should be configured more...
object Implicits:
  lazy implicit val actorSystem: ActorSystem = ActorSystem("collectioneer")
