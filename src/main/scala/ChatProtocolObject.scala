package chatprotocol

package object ChatProtocolObject {

  def initializeConnection(args: String): Boolean = {
    // Validate connection based on initial input.
    val validationRequirement = "CXN v1.0" // Checks protocol acronym and version number match what's expected.
    if (args != validationRequirement) false else true
  }

  def processInput(args: String): String = {
    var result = args

    if (args == "lol") {
      result = "more like...LOL!"
    }

    result
  }
}
