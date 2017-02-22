package com.trax.fsm.trip

import akka.actor._

import akka.persistence.fsm._
import scala.reflect._

import com.typesafe.scalalogging.Logger

class TripFSM(rider_id: Integer) extends PersistentFSM[State, Data, DomainEvent] {

  val logger = Logger(s"Trip Logger [${self.path}]")

  override def persistenceId: String = s"persistence-trip-$rider_id"

  override def domainEventClassTag: ClassTag[DomainEvent] = classTag[DomainEvent]

  override def applyEvent(domainEvent: DomainEvent, data: Data): Data = {
    domainEvent match {
      case SetVehicleEvent(vehicle_id) => {
        data.asInstanceOf[Detail].copy(vehicle_id = vehicle_id, state_name = stateName.identifier)
      }
      case ContinueRidingEvent => {
        data.asInstanceOf[Detail].copy(state_name = stateName.identifier)
      }
      case EndTripEvent => {
        data.asInstanceOf[Detail].copy(vehicle_id = null, state_name = stateName.identifier)
      }
    }
  }
  
  override def preStart {
    super.preStart
    logger.info(s"Starting new trip...")
  }

  // override default postStop
  // notify context
  override def postStop {
    super.postStop
    TripContext.onTripTerminated(rider_id)
  }

  // define initial state and data
  startWith(WaitingForJeepney, Detail(null, rider_id, WaitingForJeepney.identifier))

  // define event handlers for each state
  when(WaitingForJeepney) {
    case Event(Board(vehicle_id), detail: Detail) => {
      logger.info(s"event fired => Board($vehicle_id)")
      goto(WaitingToPay) applying SetVehicleEvent(vehicle_id) replying true
    }
  }

  when(WaitingToPay) {
    case Event(NoMoney, detail: Detail) => {
      logger.info(s"event fired => NoMoney")
      goto(WaitingToStop) applying ContinueRidingEvent replying true
    }
    case Event(PassMoney, detail: Detail) => {
      logger.info(s"event fired => PassMoney")
      goto(WaitingForChange) applying ContinueRidingEvent replying true
    }
  }

  when(WaitingForChange) {
    case Event(ReceiveChange, detail: Detail) => {
      logger.info(s"event => ReceiveChange")
      goto(RidingJeepney) applying ContinueRidingEvent replying true
    }
    case Event(NoChange, detail: Detail) => {
      logger.info(s"event => NoChange")
      goto(ChangeResolution) applying ContinueRidingEvent replying true
    }
  }

  when(RidingJeepney) {
    case Event(ClickCoinOnRoof, detail: Detail) => {
      logger.info(s"event => ClickCoinOnRoof")
      goto(WaitingToStop) applying ContinueRidingEvent replying true
    }
  }

  when(ChangeResolution) {
    case Event(Resolve, detail: Detail) => {
      logger.info(s"event => Resolve")
      goto(WaitingToStop) applying ContinueRidingEvent replying true
    }
  }

  when(WaitingToStop) {
    case Event(GetOff, detail: Detail) => {
      logger.info(s"event => GetOff")
      goto(WaitingForJeepney) applying EndTripEvent replying true
    }
  }

  whenUnhandled {
    case Event(event, state) =>
      logger.error(s"Unable to handle event $event in state $state")
      stay replying false
  }

  // define state transition handler
  onTransition {
    case WaitingForJeepney -> WaitingToPay => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"WaitingForJeepney -> WaitingToPay :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
        }
      }
    }
    case WaitingToPay -> WaitingToStop => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"WaitingToPay -> WaitingToStop :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
        }
      }
    }
    case WaitingToPay -> WaitingForChange => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"WaitingToPay -> WaitingForChange :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
        }
      }
    }
    case WaitingForChange -> RidingJeepney => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"WaitingForChange -> RidingJeepney :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
        }
      }
    }
    case WaitingForChange -> ChangeResolution => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"WaitingForChange -> ChangeResolution :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
        }
      }
    }
    case RidingJeepney -> WaitingToStop => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"RidingJeepney -> WaitingToStop :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
        }
      }
    }
    case ChangeResolution -> WaitingToStop => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"ChangeResolution -> WaitingToStop :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
        }
      }
    }
    case WaitingToStop -> WaitingForJeepney => {
      nextStateData match {
        case Detail(vehicle_id, rider_id, state_name) => {
          logger.info(s"Resetting :: data => { vehicle_id => $vehicle_id, rider_id => $rider_id, state_name => $state_name }")
          self ! PoisonPill
        }
      }
    }
  }

  // define termination handler
  onTermination {
    case StopEvent(PersistentFSM.Shutdown, state, data) => {
      state match {
        case WaitingForJeepney => {
          logger.info("Trip has ended.")
        }
        case _ => {
          logger.info(s"Trip has ended abruptly while at state ${state.identifier}.")
        }
      }
    }
  }
}
