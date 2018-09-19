//Exercício 1.1. (Ping-Pong) Há três mensagens Start (sem campos),
//Ping e Pong (com um campo inteiro). Um AtorA, ao qual possui um
//atributo actB do tipo ActorRef indicando um ator AtorB ao qual ele
//pode se comunicar, pode receber um Start de modo a começar a cadeia
//e mandar um Ping(0) para um AtorB. O AtorB responde com um Pong
//incrementando o valor do campo. Com o campo inteiro chegando em
//2000 o processo para. Implemente esse Ping-Pong usando o Akka.

//Tadeu Raphael da Silva Guimaraes França
//Daniel Carvalho Herdy Moura

import akka.actor._

import akka.routing.RoundRobinRouter

import scala.concurrent.duration._

import scala.io._


sealed trait Message



case object Start extends Message;


case class Ping(ping: Int) extends Message;


case class Pong(ping: Int) extends Message;



class AtorA(ping: Int, actB: ActorRef) extends Actor{

    override def receive: Receive = {

        case Start => {

            println("Comecou o jogo");

            actB ! Ping(0);

        }

        

        case Pong(ping) => {

            println((ping+1) + "o Ping");

            actB ! Ping(ping+1);

        }

    }

}



class AtorB(ping: Int) extends Actor{

    def receive: Receive = {

        case Ping(ping) => {

            if(ping == 2000){

                println("Fim de jogo");

                context.stop(self);

            }
            else{

                println((ping+1) + "o Ping");

                sender() ! Pong(ping+1);

            }

        }

    }

}



object Teste{

    def main(args: Array[String]): Unit = {

        val system = ActorSystem("MainSystem");

        val atorB = system.actorOf(Props(new AtorB(0)), "pong");

        val atorA = system.actorOf(Props(new AtorA(0, atorB)), "ping");

        

        atorA ! Start;

    }

}