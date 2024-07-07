package Impl

import APIs.PatientAPI.PatientQueryMessage
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, *}
import Common.Object.{ParameterList, SqlParameter}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*
import io.circe.Json
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.parser._

case class ReadTasksMessagePlanner(override val planContext: PlanContext) extends Planner[String]:
  override def plan(using planContext: PlanContext): IO[String] = {
    val managerTasksIO: IO[List[Json]] = readDBRows(
      s"SELECT user_name FROM ${schemaName}.ManagerTasks", List()
    )
    managerTasksIO.map { managerTasks =>
      Json.arr(managerTasks: _*).noSpaces
    }
  }

