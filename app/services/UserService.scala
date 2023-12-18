package services

import javax.inject._
import models.users.{User, UserList}
import daos.UserDAO


@Singleton
class UserService @Inject() (userDAO: UserDAO) {
  def all: List[UserList] = userDAO.all

  def getWholeById(userId: Long) = userDAO.getWholeById(userId)
}
