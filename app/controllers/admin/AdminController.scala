package controllers

import javax.inject._
import views._
import play.api._
import play.api.mvc._
import play.api.Configuration
import play.api.libs.json.{Json, Writes, _}
import play.api.i18n.{MessagesApi, I18nSupport}


@Singleton
class AdminController @Inject()(
  config: Configuration,
  cc: ControllerComponents,
) extends AbstractController(cc) with I18nSupport {

  def index = Action { implicit request => Ok(html.index()) }

}