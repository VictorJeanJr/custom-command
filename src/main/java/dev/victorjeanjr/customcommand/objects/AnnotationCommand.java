package dev.victorjeanjr.customcommand.objects;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnnotationCommand {

    private String command, description, permission;
    private boolean arg, modify, onlyPlayer;

    private AnnotationCommand(Command command, boolean arg) {
        this.command = command.command();
        this.description = command.description();
        this.permission = command.permission();
        this.arg = arg;
        this.onlyPlayer = command.onlyPlayer();
        this.modify = false;
    }

    private AnnotationCommand(Command command, String commandArg, boolean arg) {
        this.command = commandArg;
        this.description = command.description();
        this.permission = command.permission();
        this.arg = arg;
        this.onlyPlayer = command.onlyPlayer();
        this.modify = true;
    }

    public boolean isModify() {
        return modify;
    }

    public String getOnlyArg() {
        if(!this.isArg()) return "";
        return this.getCommand().split(" ")[1];
    }

    public String getCommand() {
        return command;
    }

    public boolean hasDescription() {
        return !description.equalsIgnoreCase("");
    }

    public String getDescription() {
        return description;
    }

    public boolean hasPermission(CommandSender commandSender) {
        if(this.permission.isEmpty()) return true;
        return commandSender.hasPermission(this.permission);
    }

    public String getPermission() {
        return permission;
    }

    public boolean isArg() {
        return arg;
    }

    public String[] getArgs() {
        if(!this.isArg()) return new String[0];
        return this.getCommand().split(" ");
    }

    public boolean isOnlyPlayer(CommandSender commandSender) {
        return onlyPlayer ? commandSender instanceof Player : true;
    }


    public static AnnotationCommand of(Command command, boolean arg) {
        return new AnnotationCommand(command, arg);
    }

    public static AnnotationCommand of(Command command, String commandArg, boolean arg) {
        return new AnnotationCommand(command, commandArg, arg);
    }

}
