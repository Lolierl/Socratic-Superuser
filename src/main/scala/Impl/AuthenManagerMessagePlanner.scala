package Impl

import APIs.PatientAPI.PatientQueryMessage
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, *}
import Common.Object.{ParameterList, SqlParameter}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*


case class AuthenManagerMessagePlanner(userName: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using planContext: PlanContext): IO[String] = {
    // Check if the user is already registered
    val checkTaskExists = readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.ManagerTasks WHERE user_name = ?)",
        List(SqlParameter("String", userName))
      )

    checkTaskExists.flatMap { exists =>
      if (exists) {
        IO.pure("already registered")
      } else {
          writeDB(s"INSERT INTO ${schemaName}.ManagerTasks (user_name) VALUES (?)",
          List(SqlParameter("String", userName)
          ))
      }
    }
  }

