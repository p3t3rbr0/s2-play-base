package controllers

import javax.inject._

import views._
import play.api._
import play.api.mvc._
import play.api.Configuration
import play.api.libs.json.{Json, Writes, _}

import models.users.{User, UserList}
import services.UserService


@Singleton
class AdminUserController @Inject()(
  cc: ControllerComponents,
  userService: UserService
) extends AbstractController(cc) {

  def getUsersList = Action { implicit request =>
    Ok(Json.toJson(userService.all)(Writes.list(UserList.jsonWrites)))
  }

  def getUserDetail(userId: Long) = Action { implicit request =>
    Ok(
      Json.toJson(
        userService.getWholeById(userId).map(Json.toJson(_)(User.jsonWrites))
      )
    )
  }

  def createUser = Action { implicit request =>
    println("!!! createUser !!!")
    println(request)
    Ok
  }

}

