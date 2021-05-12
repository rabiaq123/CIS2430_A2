package adventure;

public class Parser {
    private static String[] validDirections = {"N", "S", "E", "W", "up", "down"};
    private static String[] validCommands = {"go", "look", "take", "inventory", "quit", "help"};

    /**
     * REQUIRED contains all valid commands user may enter
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
     * REQUIRED parse the user input into a command and throw exception if invalid command
     * @param userInput input to be used to create Command object
     * @throws InvalidCommandException if user inputs an invalid command
     * @return Command object representing user input
     */
    public Command parseUserCommand(String userInput) throws InvalidCommandException {
        String[] input = userInput.split("\\s+", 2); // store everything after first word into second index
        String verb = input[0];
        String noun;
        if (input.length == 1) {
            return new Command(verb);
        } else {
            noun = input[1];
            return new Command(verb, noun);
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
     * @return String a String representing the valid commands and directions
     */
    @Override
    public String toString() {
        String valid;

        valid = allCommands();
        valid = valid.concat("\nvalid directions: ");
        for (String direction : validDirections) {
            valid = valid.concat("/" + direction + "/ ");
        }

        return valid;
    }
}
