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
     * @throws InvalidCommandException includes invalid verb and invalid input formatting
     */
    public Command() throws InvalidCommandException {
        this(null, null); // calling Command constructor to send two String params
    }

    /**
     * REQUIRED constructor: create a command object given only an action. this.noun is set to null
     * @param command first word of the user input - the verb
     * @throws InvalidCommandException includes invalid verb and invalid input formatting
     */
    public Command(String command) throws InvalidCommandException {
        // TO DO validate the action word here and throw an exception if it isn't a single-word action
        this(command, null); // calling other Command constructor to send two String params
    }

    /**
     * REQUIRED constructor: create a command object given both an action and a noun
     * validate the command and ensure that the noun provided is valid for the given command
     * @param command first word of user command
     * @param what second part of user command
     * @throws InvalidCommandException includes invalid verb and invalid input formatting
     */
    public Command(String command, String what) throws InvalidCommandException {
        if (command == null || command.isEmpty()) { // calling default constructor should throw exception
            throw new InvalidCommandException();
        }
        // checking for valid command
        checkValidCommand(command, what);
    }

    /**
     * checking whether valid command was given
     * @param command first word of user command
     * @param what second part of user command
     * @throws InvalidCommandException if invalid command was given
     */
    private void checkValidCommand(String command, String what) throws InvalidCommandException {
        if (isValidCommand(command)) {
            checkNeedsNoun(command, what); // throw exception if noun is needed and no noun is given
            this.action = command;
            this.noun = what;
        } else { // throw exception if command/action is invalid
            throw new InvalidCommandException();
        }
    }

    /**
     * checking whether a noun is needed for the corresponding user command
     * @param command first word of user command
     * @param what second part of user command
     * @throws InvalidCommandException if noun is included with command which does not use a noun
     */
    private void checkNeedsNoun(String command, String what) throws InvalidCommandException {
        boolean needsNoun = requiresNoun(command);
        boolean nounEntered = hasNoun(what);
        if (needsNoun) {
            checkNoun(command, what, nounEntered);
        } else if (command.equals("inventory") || command.equals("quit") || command.equals("help")) {
            if (nounEntered) {
                throw new InvalidCommandException();
            }
        }
    }

    /**
     * check whether (valid) noun was entered when it was needed
     * @param command first word of user command
     * @param thing second part of user command representing noun
     * @param nounEntered boolean variable representing whether a noun was entered
     * @throws InvalidCommandException includes invalid verb and invalid input formatting
     */
    private void checkNoun(String command, String thing, boolean nounEntered) throws InvalidCommandException {
        if (nounEntered) { // noun must follow "go" and "take" commands
            if (command.equals("go")) {
                if (!validDirection(thing)) { // valid direction must follow "go" command
                    throw new InvalidCommandException();
                }
            }
        } else {
            throw new InvalidCommandException();
        }
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
     * REQUIRED
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord() {
        return (noun != null);
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set myParser instance variable in Command class
     */
    public void setMyParser() {
        myParser = new Parser();
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set action instance variable in Command class
     * @param word1 first word in user input
     */
    public void setAction(String word1) {
        this.action = word1;
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set noun instance variable in Command class
     * @param word2 second part of user input (everything after first word)
     */
    public void setNoun(String word2) {
        this.noun = word2;
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
    private boolean isValidCommand(String userCommand) {
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
     * @param userDirection direction parsed from user input
     * @return boolean variable representing whether user entered a valid direction
     */
    private boolean validDirection(String userDirection) {
        return myParser.isValidDirection(userDirection);
    }

    /**
     * REQUIRED toString method prints String instead of mem location on accident
     * @return String a String representing the valid commands and the user input
     */
    @Override
    public String toString() {
        String input;

        input = "\nuser input: ";
        if (getActionWord() != null && getNoun() != null) { // user entered command and noun
            input = input + getActionWord() + " " + getNoun();
        } else if (getActionWord() != null) { // user entered only command
            input = input + getActionWord();
        } else { // no user input
            input = input + "error in user command - no action given";
        }

        return input;
    }
}
