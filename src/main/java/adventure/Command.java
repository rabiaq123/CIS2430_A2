package adventure;

/* TO DO add a static data structure or another enum class that lists all the valid commands.
Then add methods for validating commands */

/* You may add other methods to this class if you wish */

public class Command implements java.io.Serializable {
    private static final long serialVersionUID = 7767450600784420978L;
    private Parser myParser = new Parser();
    private String action;
    private String noun;
    private static String[] validCommands = {"go", "look", "take", "inventory", "quit", "help"};

    /**
     * REQUIRED constructor: create a command object with default values. Both instance variables are set to null
     * @throws InvalidCommandException
     */
    public Command() throws InvalidCommandException {
        this(null, null); // calling Command constructor to send two String params
    }

    /**
     * REQUIRED constructor: create a command object given only an action. this.noun is set to null
     * @param command first word of the user input - the verb
     * @throws InvalidCommandException
     */
    public Command(String command) throws InvalidCommandException {
        // TO DO validate the action word here and throw an exception if it isn't a single-word action
        this(command, null); // calling other Command constructor to send two String params
    }

    /**
     * REQUIRED constructor: create a command object given both an action and a noun
     * validate the command and ensure that the noun provided is valid for the given command
     * @param command first word of the command
     * @param what second word of the command
     */
    public Command(String command, String what) throws InvalidCommandException {
        boolean needsNoun = requiresNoun(command);
        boolean nounEntered = hasNoun(what);

        // checking for valid command
        if (isValidCommand(command)) {
            if (needsNoun && !nounEntered) { // a noun must follow "go" and "take" commands
                throw new InvalidCommandException();
            } else if (needsNoun && nounEntered){
                if (command.equals("go")) {
                    if (!validDirection(what)) { // valid direction must follow "go" command
                        throw new InvalidCommandException();
                    }
                }
            } else if (command.equals("inventory") || command.equals("quit") || command.equals("help")) {
                if (nounEntered) {
                    throw new InvalidCommandException();
                }
            }
            this.action = command;
            this.noun = what;
        } else { // throw exeption if command/action is invalid
            throw new InvalidCommandException();
        }
    }

    /**
     * @param command first word in user input
     * @return boolean representing whether command requires noun to accompany it
     */
    private boolean requiresNoun(String command) {
        boolean requiresNoun = false;
        if (command.equals("go") || command.equals("take")) {
            requiresNoun = true;
        }

        return requiresNoun;
    }

    /**
     * @param what everything after the first word in user input
     * @return boolean representing whether user input contains noun
     */
    private boolean hasNoun(String what) {
        boolean hasNoun = true;
        if (what == null || what.isEmpty()) {
            hasNoun = false;
        }

        return hasNoun;
    }

    /**
     * check whether user entered a valid command
     * @param userCommand first word in user input
     * @return boolean variable representing whether the command the user entered is valid
     */
    public boolean isValidCommand(String userCommand) {
        boolean validCommand = false;
        for (String command : validCommands) {
            if (userCommand.equals(command)) {
                validCommand = true;
            }
        }

        return validCommand;
    }

    /**
     * wrapper method for isValidDirection() method in Parser class
     * @param userDirection
     * @return boolean variable representing whether user entered a valid direction
     */
    private boolean validDirection(String userDirection) {
        return myParser.isValidDirection(userDirection);
    }

    /**
     * REQUIRED return command word of this command. If command was not understood, result is null.
     * @return The command word (the first word).
     */
    public String getActionWord() {
        return this.action;
    }

    /**
     * REQUIRED
     * @return The second word of this command. Returns null if there was no second word.
     */
    public String getNoun() {
        return this.noun;
    }

    /**
     * REQURIED
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord() {
        return (noun != null);
    }

    /**
     * REQUIRED toString method prints String instead of mem location on accident
     * @return String a String representing the valid commands and the user input
     */
    @Override
    public String toString() {
        String input = "user input: ";

        if (getActionWord() != null && getNoun() != null) { // user entered commmand and noun
            input = input.concat(getActionWord()).concat(" ").concat(getNoun());
        } else if (getActionWord() != null) { // user entered only command
            input = input.concat(getActionWord());
        } else { // no user input
            input = input.concat("error in user command - no action given");
        }

        return input;
    }
}
