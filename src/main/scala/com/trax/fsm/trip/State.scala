package com.trax.fsm.trip

import akka.persistence.fsm.PersistentFSM.FSMState

sealed trait State extends FSMState

case object WaitingForJeepney extends State {
  override def identifier: String = "WaitingForJeepney"
}
case object WaitingToPay extends State {
  override def identifier: String = "WaitingToPay"
}
case object WaitingForChange extends State {
  override def identifier: String = "WaitingForChange"
}
case object ChangeResolution extends State {
  override def identifier: String = "ChangeResolution"
}
case object RidingJeepney extends State {
  override def identifier: String = "RidingJeepney"
}
case object WaitingToStop extends State {
  override def identifier: String = "WaitingToStop"
}

