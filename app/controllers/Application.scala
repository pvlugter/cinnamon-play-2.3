package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Akka
import play.api.Play.current

object Application extends Controller {

  def index = Action {
    Akka.system // load actor system to test cinnamon
    Ok(views.html.index("Your new application is ready."))
  }

}
