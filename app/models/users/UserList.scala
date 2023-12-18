package models.users

import anorm._
import anorm.SqlParser._
import org.joda.time.DateTime
import play.api.libs.json.{JsValue, Json, Writes}


case class UserList(
  id: Int,
  username: String,
  email: String,
  avatar: Option[String],
  created: DateTime = DateTime.now,
  last_visited: Option[DateTime],
  is_activated: Boolean = false,
  is_admin: Boolean = false
) {

  // def getAvatar: String = this.avatar.map(ava => ava).getOrElse("app/img/default-avatar.png")
  
}

trait UserListJson {
  val jsonWrites = new Writes[UserList] {
    def writes(user: UserList) = {
      Json.obj(
        "id" -> user.id,
        "username" -> user.username,
        "email" -> user.email,
        "created" -> user.created.toString("yyyy-MM-dd"),
        "last_visited" -> user.last_visited.map(_.toString("yyyy-MM-dd HH:mm:ss")),
        "is_activated" -> user.is_activated,
        "is_admin" -> user.is_admin
      )
    }
  }
}

trait UserListSerialize {
  val writes = {
    get[Int]("id") ~
    get[String]("username") ~
    get[String]("email") ~
    get[Option[String]]("avatar") ~
    get[DateTime]("created") ~
    get[Option[DateTime]]("last_visited") ~
    get[Boolean]("is_activated") ~
    get[Boolean]("is_admin") map {
      case id ~ username ~ email ~ avatar ~ created ~ last_visited ~ is_activated ~ is_admin => UserList(
        id, username, email, avatar, created, last_visited, is_activated, is_admin
      )
    }
  }
}

object UserList extends UserListJson with UserListSerialize
