package dev.victorjeanjr.customcommand.content.executor;

import dev.victorjeanjr.customcommand.objects.AnnotationCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Executor {

    private CommandSender commandSender;
    private AnnotationCommand annotationCommand;

    private Executor(CommandSender commandSender, AnnotationCommand annotationCommand) {
        this.commandSender = commandSender;
        this.annotationCommand = annotationCommand;
    }

    public String[] formatCommands(String mark) {
        Collection<String> commands = new ArrayList<>();
        commands.add(String.format("§a§l%s §a- COMANDOS: ", mark));
        return commands.toArray(new String[0]);
    }

    public boolean has(String permission) {
        return this.commandSender.hasPermission(permission);
    }

    public boolean isPlayer() {
        return this.commandSender instanceof Player;
    }

    public CommandSender getSender() {
        return commandSender;
    }

    public Player getPlayer() {
        return (Player)this.commandSender;
    }

    public AnnotationCommand getAnnotation() {
        return annotationCommand;
    }

    public static Executor of(CommandSender commandSender, AnnotationCommand annotationCommand) {
        return new Executor(commandSender, annotationCommand);
    }

}
