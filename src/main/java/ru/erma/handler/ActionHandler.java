package ru.erma.handler;

/**
 * This interface represents an action handler in the system.
 * An action handler provides a method to handle a user's action based on their choice.
 */
public interface ActionHandler {
    /**
     * Handles a user's action based on their choice.
     *
     * @param choice the user's choice
     */
    void handleAction(int choice);
}