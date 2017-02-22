package com.trax.fsm.trip

import akka.actor._
import com.typesafe.scalalogging.Logger

// define context
object TripContext {
  val logger = Logger("TripContext Logger")
  val system = ActorSystem("trip")
  
  var waitforTermination: Boolean = false
  var activeActors: Set[Int] = Set()
  var running: Boolean = true
  
  def startTrip(rider_id: Int): ActorRef = synchronized {
    var trip: ActorRef = null
    
    // check if rider is already on a trip
    if(!activeActors.contains(rider_id)) {
      trip = system.actorOf(Props(classOf[TripFSM], rider_id), s"$rider_id")
      activeActors += rider_id
    } else {
      logger.info(s"Trip with rider $rider_id already in progress.")
    }
    
    trip
  }
  
  def onTripTerminated(rider_id: Int) = synchronized {
    // delete rider from active list
    if(activeActors.contains(rider_id)) {
      logger.info(s"Trip [$rider_id] has stopped.")
      activeActors -= rider_id
      
      if(waitforTermination && activeActors.size == 0) {
        shutdown
        running = false
      }
    }
  }
  
  def shutdown {
    logger.info("No more active trips. Terminating trip fsm context...")
    system.terminate
    logger.info("done.")
  }
  
  def awaitTermination {
    waitforTermination = true
  }
  
  def isRunning: Boolean = {
    running
  }
}