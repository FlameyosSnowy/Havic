package me.flame.havic.spigot;

import lombok.Getter;

@Getter
public enum Message {
    NOT_PLAYER("&cOnly players are allowed to execute this command."),
    NOT_CONSOLE("&cOnly console is allowed to execute this command."),
    NO_PERM("&cYou don't permission to execute this command."),
    CORRECT_USAGE("&cCorrect usage: "),
    COOLDOWN_ACTIVE("&cYou need to wait %time% to use this command."); // time may be automated to hours, minutes, seconds or days.

    private String message;

    Message(String message) {
        this.message = Lib.style(message);
    }

    public void setMessage(String message) {
        this.message = Lib.style(message);
    }
}
