package com.trax.fsm.trip

import akka.actor._

import akka.testkit._

import org.scalatest._

class TripSpec extends TestKit(ActorSystem("TripSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  
  override def afterAll {
    system.terminate()
  }

  "Trip FSM" must { 
    "handle when rider has no money" in
    {
      val rider_id: Integer = 1
      val vehicle_id: Integer = 1

      val trip = system.actorOf(Props(classOf[TripFSM], rider_id), s"$rider_id")

      trip ! Board(vehicle_id)
      expectMsg(true)
      trip ! NoMoney
      expectMsg(true)
      trip ! GetOff
      expectMsg(true)
    }
    "handle when rider has money and vehicle has change" in {
      val rider_id: Integer = 2
      val vehicle_id: Integer = 2
      
      val trip = system.actorOf(Props(classOf[TripFSM], rider_id), s"$rider_id")
      
      trip ! Board(vehicle_id)
      expectMsg(true)
      trip ! PassMoney
      expectMsg(true)
      trip ! ReceiveChange
      expectMsg(true)
      trip ! ClickCoinOnRoof
      expectMsg(true)
      trip ! GetOff
      expectMsg(true)
    }
    "handle when rider has money but vehicle has no change" in {
      val rider_id: Integer = 3
      val vehicle_id: Integer = 3
      
      val trip = system.actorOf(Props(classOf[TripFSM], rider_id), s"$rider_id")
      
      trip ! Board(vehicle_id)
      expectMsg(true)
      trip ! PassMoney
      expectMsg(true)
      trip ! NoChange
      expectMsg(true)
      trip ! Resolve
      expectMsg(true)
      trip ! GetOff
      expectMsg(true)
    }
    "persist and recover data" in {
      val rider_id: Integer = 4
      val vehicle_id: Integer = 4
      
      var trip = system.actorOf(Props(classOf[TripFSM], rider_id), s"$rider_id")
      
      trip ! Board(vehicle_id)
      expectMsg(true)
      trip ! PassMoney
      expectMsg(true)
      trip ! PoisonPill
      
      system.stop(trip)
      
      trip = system.actorOf(Props(classOf[TripFSM], rider_id), s"$rider_id")
      
      trip ! NoChange
      expectMsg(true)
      trip ! Resolve
      expectMsg(true)
      trip ! GetOff
      expectMsg(true)
    }
    "fail when event doesn't match the state" in {
      val rider_id: Integer = 5
      val vehicle_id: Integer = 5
      
      var trip = system.actorOf(Props(classOf[TripFSM], rider_id), s"$rider_id")
      
      trip ! Board(vehicle_id)
      expectMsg(true)
      trip ! GetOff
      expectMsg(false)
    }
  }
}