package daos

import javax.inject.Inject
import play.api.db.DBApi
import anorm._


class BaseDAO @Inject()(dBApi: DBApi) {

  def findWhere[A](
    table: String,
    fields: List[String],
    params: Seq[(String, ParameterValue)],
    parser: RowParser[A]
  ): Seq[A] = {
    dBApi.database("default").withConnection(implicit con => {

      val query = "SELECT " + (
        if (fields.isEmpty) { "*" } else { fields.map(f => {"${f}"}).mkString(",") }
      ) + " FROM " + table + (
        if (params.isEmpty) {
          ""
        } else {
          params.map({ case (col, _) => col + "={" + col + "}" }).mkString(" AND ")
        }
      )

      val nps = params.map(p => {
        val np: NamedParameter = p
        np
      })

      SQL(query).on(nps: _*).as(parser*)
    })
  }

}
