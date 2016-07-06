package chatprotocol

import java.io._
import java.net.{ServerSocket, Socket, SocketException}

object ChatServer {

  def main(args: Array[String]): Unit = {
    val serverport = 9998
    try {
      val listener = new ServerSocket(serverport)
      println("Server launching on port " + serverport + ".")
      while (true)
        new ChatServerThread(listener.accept()).start()
      listener.close()
    }
    catch {
      case e: IOException =>
        System.err.println("Could not listen on port: " + serverport + ".");
        System.exit(-1)
    }
  }
}

case class ChatServerThread(socket: Socket) extends Thread("ServerThread") {

  override def run(): Unit = {
    try {
      val out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))
      val in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))
      var userMessage = ""

      def endSession(): Unit = {
        out.close()
        in.close()
        socket.close()
      }

      if (ChatProtocolObject.initializeConnection(in.readObject().asInstanceOf[String])) {
        out.writeObject("READY")
      } else {
        out.writeObject("REFUSED")
        endSession
      }

      while (true) {
        var succeeded = false
        println("Wait for client message...")
        do {
          //userMessage = in.readObject().asInstanceOf[String]
          userMessage = ChatProtocolObject.processInput(in.readObject().asInstanceOf[String])
          if (userMessage != "") {
            println("Received message from client: " + userMessage)
            out.writeObject("Successful receipt of: " + userMessage)
            out.flush()
            succeeded = true
          }
        } while (! succeeded)
        Thread.sleep(100)
      }

      endSession
    }
    catch {
      case e: SocketException =>
        () // avoid stack trace when stopping a client with Ctrl-C
      case e: IOException =>
        e.printStackTrace()
    }
  }

}