package com.trax.fsm.trip

sealed trait Data

case class Detail(vehicle_id: Integer, rider_id: Integer, state_name: String) extends Data