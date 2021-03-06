package chatprotocol

import java.io._
import java.net.{InetAddress, Socket}
import scala.io._

object ChatClient {
  def main(args: Array[String]) {

    try {
      val serverHost = "localhost"
      val serverPort = 9998
      val ia = InetAddress.getByName(serverHost)
      val socket = new Socket(ia, serverPort)
      val out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))
      val in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))
      val validationString = "CXN v1.0"
      var serverMessage = ""

      def endSession: Unit ={
        out.close()
        in.close()
        socket.close()
        System.exit(1)
      }

      // Begin connection validation.
      out.writeObject(validationString)
      out.flush()

      if(in.readObject().asInstanceOf[String] != "READY"){
        println("[CXN] The server refused the connection. Are you using the correct version?")
        endSession
      } else {
        println("[CXN] Ready! Begin sending messages to the server, and it will verify they were received properly.")
      }

      while (true) {
        print("> ")
        out.writeObject(StdIn.readLine())
        //val clientMessage = out.writeObject(StdIn.readLine()).asInstanceOf[String]
        var waitingOnServer = true

        /* // Client-side connection termination for quick exits. Currently having issue with casting Object as String for if conditions.
        if (clientMessage == "exit") {
          out.flush()
          endSession
        } else if (clientMessage == "") {
          print("<Message cannot be blank.>")
          waitingOnServer = false
        } else out.flush()
        */

        do {
          serverMessage = in.readObject().asInstanceOf[String]
          if (serverMessage == "TERMINATESIGNAL") endSession
          else if (serverMessage != "") {
            println("< [Server] " + serverMessage)
            waitingOnServer = false
          } else {
            waitingOnServer = false
          }
        } while (waitingOnServer)
        Thread.sleep(100)
      }
      endSession
    }
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }


}
