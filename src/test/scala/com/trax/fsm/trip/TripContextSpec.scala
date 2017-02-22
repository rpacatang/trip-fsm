package com.trax.fsm.trip

import org.scalatest._

class TripContextSpec extends FlatSpec {
  "Trip Context" should "handle multiple trips and shutdown when all trips ends" in {
    val trip1 = TripContext.startTrip(1)
    val trip2 = TripContext.startTrip(2)
    val trip3 = TripContext.startTrip(3)

    TripContext.awaitTermination

    trip1 ! Board(1)
    trip2 ! Board(2)
    trip3 ! Board(3)

    trip1 ! NoMoney
    trip2 ! PassMoney
    trip3 ! PassMoney

    trip2 ! ReceiveChange
    trip3 ! NoChange

    trip2 ! ClickCoinOnRoof
    trip3 ! Resolve

    trip1 ! GetOff
    trip2 ! GetOff
    trip3 ! GetOff

    Thread.sleep(3000)

    assert(TripContext.isRunning == false)
  }
}