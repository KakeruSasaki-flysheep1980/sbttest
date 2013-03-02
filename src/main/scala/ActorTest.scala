object ActorTest {

  import scala.actors.Actor._
  import java.util.Date
  import scala.util.control.Exception.allCatch

  private lazy val tester = actor {
    loop {
      react {
        case message: String => {
          println("[%s][%s]received message and waiting...".format(new Date().getTime, message))
          val result = allCatch.either {
            if (message.indexOf("3") < 0) throw new RuntimeException(message) else message
          }
          println("[%s][%s]finish to wait thread.".format(new Date().getTime, message))
          reply(result)
        }
        case 0 => exit()
      }
    }
  }

  def execute = {
    val futures = (1 to 10).map { index =>
      val message = "message%d".format(index)
      val future = tester !! (message, new PartialFunction[Any, Either[Throwable, String]] {
        def apply(v1: Any): Either[Throwable, String] = v1.asInstanceOf[Either[Throwable, String]]
        def isDefinedAt(x: Any): Boolean = true
      })
      (message, future)
    }
    val results = futures.map { case (message, future) =>
      future.apply()
    }
    results.foreach(println)

    // exit
    tester ! 0
  }
}
