package chatprotocol

package object ChatProtocolObject {

  def initializeConnection(args: String): Boolean = {
    // Validate connection based on initial input.
    val validationRequirement = "CXN v1.0" // Checks protocol acronym and version number match what's expected.
    if (args != validationRequirement) false else true
  }

  def processInput(args: String): String = {
    var result = args

    if (args == "exit") result = "TERMINATESIGNAL"
    else if (args == "help") {
      result =
        """
          |ChatProtocol
          |
          |help:
          |  Display the help text.
          |
          |exit:
          |  Terminate the session.
        """.stripMargin
    }

    result
  }
}

package object ChatProtocolIdleTimer {
  var idleseconds = 30
  // If the idleseconds value reaches zero, terminate the idle connection.

}