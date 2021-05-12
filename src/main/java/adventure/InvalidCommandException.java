package adventure;

public class InvalidCommandException extends Exception {
    /**
     * REQUIRED constructor: if command user enters is not one of "go", "look", "take", "inventory", "quit", or "help"
     */
    public InvalidCommandException() {
        super("Error: Invalid input. Enter 'help' for tips.");
    }

    public InvalidCommandException(String message) {
        super(message);
    }

    /**
     * REQUIRED toString method prints String instead of mem location on accident
     * @return String a String representing the invalid command error message
     */
    @Override
    public String toString() {
        return "\nInvalidCommandException Error: Invalid input. Enter 'help' for tips.";
    }
}
