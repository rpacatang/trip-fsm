package com.trax.fsm.trip

sealed trait DomainEvent

case class SetVehicleEvent(vehicle_id: Integer) extends DomainEvent
case object ContinueRidingEvent extends DomainEvent
case object EndTripEvent extends DomainEvent