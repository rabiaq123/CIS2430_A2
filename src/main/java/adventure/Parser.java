package adventure;

public class Parser implements java.io.Serializable {
    private static final long serialVersionUID = 8156818232392220736L;
    private final String[] validDirections = {"N", "S", "E", "W", "up", "down"};
    private static String[] validCommands = {"go", "look", "take", "inventory", "quit", "help"};
    private Adventure myAdv = new Adventure();

    /**
     * REQUIRED contains all valid commands a user may input
     * @return a String representing a valid command
     */
    public String allCommands() {
        String commandInfo;

        commandInfo = "\n[VALID COMMANDS]---------------------------------------------------------\n";
        for (String command : validCommands) {
            commandInfo = commandInfo.concat("/" + command + "/ ");
        }

        return commandInfo;
    }

    /**
     * parse the user input into a command and throw exception if invalid command
     * @param userInput input to be used to create Command object
     * @throws InvalidCommandException if user inputs an invalid command
     * @return Command object representing user input
     */
    public Command parseUserInput(String userInput) throws InvalidCommandException {
        String[] input = userInput.split("\\s+", 2); // store everything after first word into second index
        String verb = input[0];
        String noun = null;

        if (input.length == 1) {
            Command oneCommand = new Command(verb);
            return oneCommand;
        } else if (input.length > 1) {
            noun = input[1];
            Command twoCommand = new Command(verb, noun);
            return twoCommand;
        } else { // throw exception if 0 words in user input
            throw new InvalidCommandException();
        }
    }

    /**
     * compares user input to valid directions
     * @param direction direction entered by user
     * @return boolean variable representing whether user entered a valid direction
     */
    public boolean isValidDirection(String direction) {
        boolean isValid = false;

        for (String validDir : validDirections) {
            if (direction.equals(validDir)) {
                isValid = true;
            }
        }

        return isValid;
    }

    /**
     * toString method prints String instead of mem location on accident
     * @return String a String representing the valid commands
     */
    @Override
    public String toString() {
        String valid = "valid commands: ";
        for (String command : validCommands) {
            valid = valid.concat("/" + command + "/ ");
        }
        valid.concat("\nvalid directions: ");
        for (String direction : validDirections) {
            valid = valid.concat("/" + direction + "/ ");
        }

        return valid;
    }
}
