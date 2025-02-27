package Impl

import APIs.ManagerAPI.ManagerRequestMessage
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, *}
import Common.Object.{ParameterList, SqlParameter}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*


case class FinishManagerMessagePlanner(userName: String, allowed:Boolean, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using planContext: PlanContext): IO[String] = {
    // Check if the user is already registered
    val checkTaskExists = readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.ManagerTasks WHERE user_name = ?)",
      List(SqlParameter("String", userName))
    )

    checkTaskExists.flatMap { exists =>
      if (!exists) {
        IO.pure("No such tasks")
      } else {
        writeDB(
            s"DELETE FROM ${schemaName}.ManagerTasks WHERE user_name = ?",
            List(SqlParameter("String", userName))
          ).flatMap { _ =>
            ManagerRequestMessage(userName, allowed).send
          }
        }
      }
    }


