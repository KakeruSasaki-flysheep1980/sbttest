object ActorTest {

  import scala.actors.Actor._
  import java.util.Date

  private lazy val tester = actor {
    loop {
      react {
        case message: String => {
          println("[%s][%s]received message and waiting...".format(new Date().getTime, message))
          println("[%s][%s]finish to wait thread.".format(new Date().getTime, message))
          reply(message)
        }
      }
    }
  }

  def execute = {
    val futures = (1 to 10).map { index =>
      tester !! "message%d".format(index)
    }
    val results = futures.map { future =>
      future.apply().asInstanceOf[String]
    }aa
    results.foreach(println)
  }
}
