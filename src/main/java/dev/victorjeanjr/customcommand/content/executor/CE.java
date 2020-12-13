package dev.victorjeanjr.customcommand.content.executor;


import dev.victorjeanjr.customcommand.content.Commands;
import dev.victorjeanjr.customcommand.content.method.DataAnnotation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class CE implements CommandExecutor {

    private final String NOT_FOUND_COMMAND = "§c§l ✗ §cEste comando não foi encontrado.";
    private final String ERROR_COMMAND = "§e§l ✗ §eO comando digitado está faltando argumentos.";

    private final String CONSOLE_MESSAGE = "§aApenas jogadores pode usar este comando.";

    private static final String NOT_PERMISSION = "§c§l ✗ §cVocê não apresenta permissões suficientes para executar este comando.";

    private Commands commands;

    public CE(Commands commands) {
        this.commands = commands;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        DataAnnotation dataAnnotation = this.commands.get(command.getName());
        if(Objects.nonNull(dataAnnotation)) {
            if(dataAnnotation.getAnnotationCommand().hasPermission(commandSender)) {
                if(args.length == 0) {
                    if(dataAnnotation.getAnnotationCommand().isOnlyPlayer(commandSender)) {
                        boolean helper = dataAnnotation.invoke(this.commands, commandSender, args);
                        if (helper) commandSender.sendMessage(this.helper(dataAnnotation.getArgs()));
                    } else {
                        commandSender.sendMessage(this.CONSOLE_MESSAGE);
                    }
                } else {
                    DataAnnotation.Arg arg = dataAnnotation.get(args[0]);
                    if(Objects.nonNull(arg)) {
                        if(arg.getAnnotationCommand().isOnlyPlayer(commandSender)) {
                            if(args.length >= arg.getCompactMethod().getParameterClass().length)
                                dataAnnotation.invoke(this.commands, arg, commandSender, args);
                            else commandSender.sendMessage(this.ERROR_COMMAND);
                        } else {
                            commandSender.sendMessage(this.CONSOLE_MESSAGE);
                        }
                    } else {
                        commandSender.sendMessage(this.NOT_FOUND_COMMAND);
                    }
                }
            } else {
                commandSender.sendMessage(CE.getNotPermission());
            }
        }
        return false;
    }

    private String[] helper(Collection<DataAnnotation.Arg> args) {
        Collection<String> collection = new ArrayList<>();
        collection.add("§7--------------| Comandos |-----------------");
        collection.add(" ");
        if(args.size() > 6) {
            args.forEach(e -> {
                collection.add(String.format("§7  §o/%s %s", e.getAnnotationCommand().getCommand(),
                        e.getAnnotationCommand().hasDescription() ?
                                "§r§8⇢ §n" + e.getAnnotationCommand().getDescription() : ""));
                collection.add(" ");
            });
        } else {
            args.forEach(e -> {
                collection.add("§7  §o/" + e.getAnnotationCommand().getCommand());
                if(e.getAnnotationCommand().hasDescription())
                    collection.add("§8   §l↪ §8§n" + e.getAnnotationCommand().getDescription());
                collection.add(" ");
            });
        }
        collection.add("§7------------------------------------------");
        return collection.toArray(new String[0]);
    }

    public static String getNotPermission() {
        return NOT_PERMISSION;
    }
}
