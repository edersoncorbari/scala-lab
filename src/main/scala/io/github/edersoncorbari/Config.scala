package io.github.edersoncorbari

import com.typesafe.config.ConfigFactory

object ConfigProperty {

  case class App(name: String,
                 master: String)

}

trait Config {

  private[this] lazy val loadConf = ConfigFactory.load().getConfig("app")

  lazy val appConf = ConfigProperty.App(loadConf.getString("name"),
                                        loadConf.getString("master"))
}
