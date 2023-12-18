package models.users

import anorm._
import anorm.SqlParser._
import org.joda.time.DateTime
import models.permissions.Permission
import play.api.libs.json.{JsValue, Json, Writes}
// import play.api.data.Form
// import play.api.mvc.MultipartFormData
// import com.google.common.net.MediaType
// import play.api.libs.Files.TemporaryFile

// import utils.Helper.md5


case class User(
  id: Int,
  username: String,
  email: String,
  uid: Option[String],
  password: String,
  first_name: Option[String],
  last_name: Option[String],
  avatar: Option[String],
  created: DateTime = DateTime.now,
  last_visited: Option[DateTime],
  is_activated: Boolean = false,
  is_admin: Boolean = false,
  permissions: Option[List[Permission]] = None
) {

  // def setUID(s: String) = this.copy(uid = Some(md5(s)))

  // def setAvatar(newAvatar: Option[String]) = this.copy(avatar = newAvatar)

  // def getAvatar: String = this.avatar.map(ava => ava).getOrElse("app/img/default-avatar.png")

  // def setPassword(newPassword: String) = this.copy(password = newPassword)

  // def parseAvatar(avatar: MultipartFormData.FilePart[TemporaryFile], basePath: String): Option[String] = {
  //   import java.io.File

  //   avatar.contentType.filter(cType => MediaType.parse(cType).is(MediaType.ANY_IMAGE_TYPE)).map(cType => {
  //     val extension = avatar.filename.trim.split('.').last
  //     val filename = md5(this.username + avatar.filename + DateTime.now.toString)
  //     val uploadPath = new File("public/" + basePath, this.username)

  //     if (!uploadPath.exists()) {
  //       uploadPath.mkdir()
  //     }

  //     avatar.ref.moveTo(new File(s"${uploadPath}/${filename}.${extension}"))

  //     s"${basePath}/${this.username}/${filename}.${extension}"
  //   })
  // }

}

trait UserJson {
  val jsonWrites = new Writes[User] {
    def writes(user: User) = {
      Json.obj(
        "id" -> user.id,
        "uid" -> user.uid,
        "username" -> user.username,
        "email" -> user.email,
        "first_name" -> user.first_name,
        "last_name" -> user.last_name,
        "created" -> user.created.toString("yyyy-MM-dd"),
        "last_visited" -> user.last_visited.map(_.toString("yyyy-MM-dd HH:mm:ss")),
        "is_activated" -> user.is_activated,
        "is_admin" -> user.is_admin
      )
    }
  }
}

trait UserSerialize {
  val writes = {
    get[Int]("id") ~
    get[String]("username") ~
    get[String]("email") ~
    get[Option[String]]("uid") ~
    get[String]("password") ~
    get[Option[String]]("first_name") ~
    get[Option[String]]("last_name") ~
    get[Option[String]]("avatar") ~
    get[DateTime]("created") ~
    get[Option[DateTime]]("last_visited") ~
    get[Boolean]("is_activated") ~
    get[Boolean]("is_admin") map {
      case id ~ username ~ email ~ uid ~ password ~ first_name ~ last_name ~ avatar ~ created ~ last_visited ~ is_activated ~ is_admin => User(
        id, username, email, uid, password, first_name, last_name, avatar, created, last_visited, is_activated, is_admin
      )
    }
  }

  // val withPermissions = {
  //   writes ~ Permission.writes map {
  //     case user ~ permissions => user.copy(permissions => Some(permissions))
  //   }
  // }
}

object User extends UserJson with UserSerialize {
  val PER_PAGE_CNT = 25
}
