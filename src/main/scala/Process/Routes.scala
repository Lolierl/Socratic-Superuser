package Process

import Common.API.PlanContext
import Impl.*
import cats.effect.*
import io.circe.generic.auto.*
import io.circe.parser.decode
import io.circe.syntax.*
import org.http4s.*
import org.http4s.client.Client
import org.http4s.dsl.io.*


object Routes:
  private def executePlan(messageType:String, str: String): IO[String]=
    messageType match {

      case "SuperuserLoginMessage" =>
        IO(decode[SuperuserLoginMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for SuperuserLoginMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "AuthenManagerMessage" =>
        IO(decode[AuthenManagerMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for AuthenManagerMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadTasksMessage" =>
        IO(decode[ReadTasksMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadTasks")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "FinishManagerMessage" =>
        IO(decode[FinishManagerMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for FinishManagerMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case _ =>
        IO.raiseError(new Exception(s"Unknown type: $messageType"))
    }

  val service: HttpRoutes[IO] = HttpRoutes.of[IO]:
    case req @ POST -> Root / "api" / name =>
        println("request received")
        req.as[String].flatMap{executePlan(name, _)}.flatMap(Ok(_))
        .handleErrorWith{e =>
          println(e)
          BadRequest(e.getMessage)
        }
