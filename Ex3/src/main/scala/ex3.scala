/*Exercício 1.3. (Dutos e Filtros) DataSource, que possui um atributo
prox de tipo ActorRef indicando o próximo filtro, é um ator que re-
cebe uma mensagem Start (case class com um campo String). Em seu
recebimento, prox deve enviar uma mensagem Message (case class de
mesma característica). Os filtros LowerCase, UpperCase, FilterVowels
(atores de mesma característica que DataSource) e Duplicate devem
receber a mensagem Message e modificar seu conteudo de acordo, ou
seja, deixar minúsculo, maiúsculo, eliminar vogais e duplicar respecti-
vamente e enviar a mensagem modificada a um próximo ator prox. Se
não houver próximo ator (null for encontrado) o processo deve parar
imediatamente em qualquer ator. Monte os filtros no teste*/

import akka.actor._

import akka.routing.RoundRobinRouter

import scala.concurrent.duration._

import scala.io._

sealed trait Message



case class Start(st: String);

case class Msg(st: String);


class DataSource(prox: ActorRef) extends Actor{

     override def receive: Receive = {

        case Start(msg) => {

            println("Comecou");

            println(msg);

            prox ! Msg(msg);

        }

     }

}



class LowerCase(prox: ActorRef) extends Actor{

    override def receive: Receive = {

        case Msg(msg) => {

            println(msg.toLowerCase);

            prox ! Msg(msg.toLowerCase);

        }

     }

}



class UpperCase(prox: ActorRef) extends Actor{

    override def receive: Receive = {

        case Msg(msg) => {

            println(msg.toUpperCase);

            prox ! Msg(msg.toUpperCase);

        }

     }

}



class FilterVowels(prox: ActorRef) extends Actor{

    override def receive: Receive = {

        case Msg(msg) => {

            println(msg.replaceAll("[AEIOU]", ""));

            prox ! Msg(msg.replaceAll("[AEIOU]", ""));

        }

     }

}



class Duplicate() extends Actor{

    override def receive: Receive = {

        case Msg(msg) => {

                println(msg + msg);

                println("Acabou");

                context.stop(self);

            }

        }

     }

}



object Teste{

    def main(args: Array[String]): Unit = {

        val system = ActorSystem("MainSystem");

        val dp = system.actorOf(Props(new Duplicate()), "duplicate");

        val fv = system.actorOf(Props(new FilterVowels(dp)), "filtervowels");

        val up = system.actorOf(Props(new UpperCase(fv)), "uppercase");

        val lc = system.actorOf(Props(new LowerCase(up)), "lowercase");

        val dt = system.actorOf(Props(new DataSource(lc)), "datasource");

        

        dt ! Start("Ola");

    }

}