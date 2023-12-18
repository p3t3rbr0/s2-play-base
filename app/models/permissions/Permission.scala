package models.permissions

import anorm._
import anorm.SqlParser._


case class Permission(
  id: Int,
  title: String,
  comment: String
)

trait PermissionSerialize {
  val writes = {
    get[Int]("permissions.id") ~
    get[String]("permissions.title") ~
    get[String]("permissions.comment") map {
      case id ~ title ~ comment => Permission(id, title, comment)
    }
  }
}

object Permission extends PermissionSerialize 
