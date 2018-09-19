/*Exercício 1.2. (Jogo de advinhação - Mestre/Escravo) Quer-se fazer
um jogo, de forma concorrente, de advinhação. Trinta e dois jogado-
res devem sortear números de 1 a 200 e a "máquina"possui o número
correto. Indique o(s) vencedore(s).*/

import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill }
import language.postfixOps
import scala.concurrent.duration._

case object Ping
case object Pong

class Pinger extends Actor {

  def receive = {
    case Start => {
      println("Começou")
      var countDown = 0
    }
    case Pong ⇒
      println(s"${self.path} received pong, count down $countDown")

      if (countDown < 10) {
        countDown += 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }
  }
}

class Ponger(pinger: ActorRef) extends Actor {
  def receive = {
    case Ping ⇒
      println(s"${self.path} received ping")
      pinger ! Pong
  }
}

    

object Main{
    
    def main(args: Array[String]): Unit = {
        val system = ActorSystem("pingpong")

    val pinger = system.actorOf(Props[Pinger], "pinger")

    val ponger = system.actorOf(Props(classOf[Ponger], pinger), "ponger")

    
        /*println(act);
        var cont: Int = 0;
        while(cont <= 11){
            if(cont == 0){
                act ! "Start";
            }
            else{
                if(cont % 2 == 0){
                    act ! "Ping";
                }
                else{
                    act ! "Pong";
                }
            }
            cont += 1;*/
        import system.dispatcher
    system.scheduler.scheduleOnce(500 millis) {
      ponger ! Ping
    }
    }
}

    

    

    

    