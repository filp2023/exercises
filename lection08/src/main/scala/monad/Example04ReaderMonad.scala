package monad

import Monad.Syntax._

object Example04ReaderMonad extends App {
  final case class Reader[Ctx, Out](run: Ctx => Out)

  object Reader {
    def ask[Ctx, CtxInfo](fn: Ctx => CtxInfo): Reader[Ctx, CtxInfo] =
      Reader(fn)

    implicit def monad[Ctx]: Monad[Reader[Ctx, *]] = new Monad[Reader[Ctx, *]] {
      def flatMap[A, B](fa: Reader[Ctx, A])(f: A => Reader[Ctx, B]): Reader[Ctx, B] =
        Reader(ctx => f(fa.run(ctx)).run(ctx))

      def pure[A](a: A): Reader[Ctx, A] =
        Reader(_ => a)
    }
  }

  // In
  final case class Config(server: ServerConfig, db: DbConfig, client: ClientConfig, appName: String)
  final case class ServerConfig(host: String, port: Int)
  final case class DbConfig(url: String, user: String, password: String)
  final case class ClientConfig(poolSize: Int, debug: Boolean)

  // Out
  final case class AppState(server: Server, db: Database, client: Client)
  final case class Server(socket: String, appName: String)
  final case class Database(connectedUrl: String, appName: String)
  final case class Client(poolSize: Int, debug: Boolean, appName: String)

  object ClassicConfigure {
    def configureServer(cfg: Config): Server = {
      val serverConfig = cfg.server

      Server(s"${serverConfig.host}:${serverConfig.port}", cfg.appName)
    }

    def configureDb(cfg: Config): Database = {
      val dbConfig = cfg.db

      Database(s"${dbConfig.url}?user=${dbConfig.user}&password=${dbConfig.password}", cfg.appName)
    }

    def configureClient(cfg: Config): Client = {
      val clientConfig = cfg.client

      Client(clientConfig.poolSize, clientConfig.debug, cfg.appName)
    }

    def configureApp(config: Config): AppState = {
      val server   = configureServer(config)
      val database = configureDb(config)
      val client   = configureClient(config)

      AppState(server, database, client)
    }
  }

  object ReaderConfigure {
    val askAppName: Reader[Config, String] =
      Reader.ask[Config, String](_.appName)

    val configureServer: Reader[Config, Server] =
      for {
        host    <- Reader.ask[Config, String](_.server.host)
        port    <- Reader.ask[Config, Int](_.server.port)
        appName <- askAppName
      } yield Server(s"$host:$port", appName)

    val configureDb: Reader[Config, Database] =
      for {
        url      <- Reader.ask[Config, String](_.db.url)
        user     <- Reader.ask[Config, String](_.db.user)
        password <- Reader.ask[Config, String](_.db.password)
        appName  <- askAppName
      } yield Database(s"$url?user=$user&password=$password", appName)

    val configureClient: Reader[Config, Client] =
      for {
        poolSize <- Reader.ask[Config, Int](_.client.poolSize)
        debug    <- Reader.ask[Config, Boolean](_.client.debug)
        appName  <- askAppName
      } yield Client(poolSize, debug, appName)

    val configureApp: Reader[Config, AppState] =
      for {
        server   <- configureServer
        database <- configureDb
        client   <- configureClient
      } yield AppState(server, database, client)
  }

  val cfg: Config =
    Config(
      ServerConfig("0.0.0.0", 1337),
      DbConfig("jdbc://localhost:5432", "postrgres", "123456"),
      ClientConfig(poolSize = 10, debug = true),
      appName = "filp"
    )

  println(ClassicConfigure.configureApp(cfg))
  println(ReaderConfigure.configureApp.run(cfg))
}
