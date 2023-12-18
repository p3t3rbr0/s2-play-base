package daos

import javax.inject.Inject
import play.api.db.DBApi
import org.joda.time.DateTime
import anorm._
import anorm.SqlParser._
import anorm.JodaParameterMetaData._
import models.users.{User, UserList}


class UserDAO @Inject()(dBApi: DBApi) extends BaseDAO(dBApi: DBApi) {

  def all: List[UserList] = {
    dBApi.database("default").withConnection(implicit con => {
      SQL(
        """
        SELECT
            id, username, email, avatar, created, last_visited, is_activated, is_admin
        FROM
            users
        """
      ).as(UserList.writes.*)
    })
  }

  def getWholeById(userId: Long): Option[User] = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("SELECT * FROM users WHERE id={userId}")
        .on('userId -> userId)
        .as(User.writes.singleOpt)
    })
  }

  def getCustomById(fields: List[String], userId: Long): Option[User] = {
    dBApi.database("default").withConnection(implicit con => {

      val query = "SELECT " + (
        if (fields.isEmpty) { "*" } else { fields.map(f => {"${f}"}).mkString(",") }
      ) + " FROM users WHERE id={userId}"

      SQL(query).on('userId -> userId).as(User.writes.singleOpt)
    })
  }

  def removeById(userId: Long): Int = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("DELETE FROM users WHERE id={userId}")
        .on('userId -> userId)
        .executeUpdate()
    })
  }

  def removeByIds(userIds: List[Long]): Int = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("DELETE FROM users WHERE id IN ({userIds})")
        .on('userIds -> userIds)
        .executeUpdate()
    })
  }

  def getByUid(uid: String) = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("SELECT * FROM users WHERE uid={uid}")
        .on('uid -> uid)
        .as(User.writes.singleOpt)
    })
  }

  def getByEmail(email: String) = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("SELECT * FROM users WHERE email={email}")
        .on('email -> email)
        .as(User.writes.singleOpt)
    })
  }

  def getByUsername(username: String) = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("SELECT * FROM users WHERE username={username}")
        .on('username -> username)
        .as(User.writes.singleOpt)
    })
  }

  def checkByEmail(email: String): Option[Int] = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("SELECT 1 FROM users WHERE email={email}")
        .on('email -> email)
        .as(scalar[Int].singleOpt)
    })
  }

  def checkByUsername(userName: String): Option[Int] = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("SELECT 1 FROM users WHERE username={userName}")
        .on('userName -> userName)
        .as(scalar[Int].singleOpt)
    })
  }

  def toggleActivated(isActivated: Int, userIds: List[Long]): Int = {
    dBApi.database("default").withConnection(implicit onn => {
      SQL("UPDATE users SET is_activated = {isActivated} WHERE id IN ({userIds})")
        .on('isActivated -> isActivated, 'userIds -> userIds)
        .executeUpdate()
    })
  }

  def countAll: Int = {
    dBApi.database("default").withConnection(implicit con => {
      SQL("SELECT count(*) FROM users").on().as(scalar[Int].single)
    })
  }

  def create(user: User): Option[Long] = {
    dBApi.database("default").withConnection(implicit con => {
      SQL(
        """
        INSERT INTO users(
            username,
            email,
            uid,
            password,
            first_name,
            last_name,
            avatar,
            created,
            last_visited,
            is_activated,
            is_admin
        ) VALUES (
            {username},
            {email},
            {uid},
            {password},
            {first_name},
            {last_name},
            {avatar},
            {created},
            {last_visited},
            {is_activated},
            {is_admin}
        )
        """
      ).on(
        'username -> user.username,
        'email -> user.email,
        'uid -> user.uid,
        'password -> user.password,
        'first_name -> user.first_name,
        'last_name -> user.last_name,
        'avatar -> user.avatar,
        'created -> user.created,
        'last_visited -> user.last_visited,
        'is_activated -> user.is_activated,
        'is_admin -> user.is_admin
      ).executeInsert()
    })
  }

  def updateWholeUser(u: User): Int = {
    dBApi.database("default").withConnection(implicit con => {
      SQL(
        """
        UPDATE
          users
        SET
          username = {username},
          email = {email},
          uid = {uid},
          password = {password},
          first_name = {first_name},
          last_name = {last_name},
          avatar = {avatar},
          is_activated = {is_activated},
          is_admin = {is_admin}
        WHERE
          id = {id}
        """
      ).on(
        'id -> u.id,
        'username -> u.username,
        'email -> u.email,
        'uid -> u.uid,
        'password -> u.password,
        'first_name -> u.first_name,
        'last_name -> u.last_name,
        'avatar -> u.avatar,
        'is_activated -> u.is_activated,
        'is_admin -> u.is_admin
      ).executeUpdate()
    })
  }

  // def getByIdWithPermissions(userId: Long): Option[(User, List[Permission])] = {
  //   dBApi.database("default").withConnection(implicit con => {
  //     SQL(
  //       """
  //       SELECT u.*, p.* FROM users AS u
  //       LEFT JOIN user_permissions AS up ON up.user_id = u.id
  //       LEFT JOIN permissions AS p ON p.id = up.permission_id
  //       WHERE u.id={userId}
  //       """
  //     ).on(
  //       'userId -> userId
  //     ).as(
  //       User.withPermissions *
  //     ).groupBy(_._1).headOption.map {
  //       case (user, permissions) => user.copy(permissions = Some(permissions))
  //     }
  //   })
  // }

  def searchByUsernameEmail(searchTerm: String, skip: Int): List[UserList] = {
    dBApi.database("default").withConnection(implicit con => {
      SQL(
        """
        SELECT
            id, username, email, avatar, created, is_activated, is_admin
        FROM
            users
        WHERE
            username LIKE {searchTerm} OR email LIKE {searchTerm}
        LIMIT
            {limit}
        OFFSET
            {skip}
        """
      ).on(
        'searchTerm -> s"%${searchTerm}%",
        'skip -> skip,
        'limit -> User.PER_PAGE_CNT
      ).as(UserList.writes.*)
    })
  }

}
