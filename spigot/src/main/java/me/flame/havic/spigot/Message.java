package me.flame.havic.spigot;

import lombok.Getter;

@Getter
public enum Message {
    NOT_PLAYER(Lib.style("&cOnly players are allowed to execute this command.")),
    NOT_CONSOLE(Lib.style("&cOnly console is allowed to execute this command.")),
    NO_PERM(Lib.style("&cYou don't permission to execute this command.")),
    COOLDOWN_ACTIVE(Lib.style("&cYou need to wait %time% to use this command.")); // time may be automated to hours, minutes, seconds or days.

    private String message;

    Message(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = Lib.style(message);
    }
}
