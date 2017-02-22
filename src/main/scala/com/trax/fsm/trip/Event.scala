package com.trax.fsm.trip

case class Board(vehicle_id: Integer)
case object NoMoney
case object PassMoney
case object NoChange
case object ReceiveChange
case object ClickCoinOnRoof
case object Resolve
case object GetOff