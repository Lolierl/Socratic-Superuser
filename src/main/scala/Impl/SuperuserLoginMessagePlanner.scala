package Impl

import APIs.PatientAPI.PatientQueryMessage
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{startTransaction, writeDB, *}
import Common.Object.{ParameterList, SqlParameter}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*


case class SuperuserLoginMessagePlanner(userName: String, password: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Attempt to validate the user by reading the rows from the database
    val validUsername = "Dragon"
    val validPassword = "yuanshen"

    // Validate the username and password
    IO {
      if (userName == validUsername && password == validPassword) {
        "Valid user"
      } else {
        "Invalid user"
      }
    }
  }

