package dev.victorjeanjr.customcommand.content.method;

import dev.victorjeanjr.customcommand.content.Commands;
import dev.victorjeanjr.customcommand.objects.AnnotationCommand;
import dev.victorjeanjr.customcommand.objects.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataAnnotation {

    private CompactMethod compactMethod;
    private AnnotationCommand annotationCommand;

    private List<Arg> args;

    private DataAnnotation(CompactMethod compactMethod, AnnotationCommand annotationCommand) {
        this.compactMethod = compactMethod;
        this.annotationCommand = annotationCommand;
        this.args = new CopyOnWriteArrayList<>();
    }

    public boolean invoke(Commands commands, CommandSender commandSender, String[] args) {
        try {
            Method method = this.getCompactMethod().constructMethod();
            Object instance = this.getCompactMethod().getClazz().newInstance();

            Collection<Object> o = FormatterClass.convert(commands, this.getCompactMethod().getParameterClass(),
                    commandSender, this.getAnnotationCommand(), args);

            int i = 0;
            Object[] callParams = new Object[o.size()];
            for (Object param : o) {
                callParams[i++] = param;
            }

            Object object = method.invoke(instance, callParams);
            return object instanceof Boolean ? (Boolean)object : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void invoke(Commands commands, Arg arg, CommandSender commandSender, String[] args) {
        try {
            Method method = arg.getCompactMethod().constructMethod();
            Object instance = arg.getCompactMethod().getClazz().newInstance();

            Collection<Object> o = FormatterClass.convert(commands, arg.getCompactMethod().getParameterClass(),
                    commandSender, this.getAnnotationCommand(), args);

            int i = 0;
            Object[] callParams = new Object[o.size()];
            for (Object param : o) {
                callParams[i++] = param;
            }

            method.invoke(instance, callParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAnnotationCommand(AnnotationCommand annotationCommand) {
        this.annotationCommand = annotationCommand;
    }

    private void setCompactMethod(CompactMethod compactMethod) {
        this.compactMethod = compactMethod;
    }

    public Arg get(String arg) {
        return this.args.stream().filter(e -> e.getOnlyArg().equalsIgnoreCase(arg)
                || e.getAnnotationCommand().getCommand().equalsIgnoreCase(arg)).findFirst().orElse(null);
    }

    public AnnotationCommand getAnnotationCommand() {
        return annotationCommand;
    }

    public CompactMethod getCompactMethod() {
        return compactMethod;
    }

    public List<Arg> getArgs() {
        return args;
    }

    public static void register(Class<?> clazz, Commands commands) {
        for(Method method : clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Command.class)) {
                Command command = method.getAnnotation(Command.class);
                DataAnnotation result = commands.get(command.command());
                if(!command.command().contains(" ")) {
                    if(Objects.isNull(result)) {
                        commands.getDataAnnotations()
                                .add(new DataAnnotation(CompactMethod.of(method),
                                        AnnotationCommand.of(command, false)));
                    } else {
                        if(Objects.isNull(result.getCompactMethod()))
                            result.setCompactMethod(CompactMethod.of(method));
                        if(result.getAnnotationCommand().isModify())
                            result.setAnnotationCommand(AnnotationCommand.of(command, false));
                    }
                } else {
                    if(Objects.isNull(result)) {
                        DataAnnotation created = new DataAnnotation(null,
                                AnnotationCommand.of(command, command.command().split(" ")[0], false));

                        created.getArgs().add(new Arg(CompactMethod.of(method),
                                AnnotationCommand.of(command, true)));

                        commands.getDataAnnotations().add(created);
                    } else {
                        Arg resultArg = result.get(command.command().split(" ")[1]);

                        if(Objects.isNull(resultArg))
                            result.getArgs().add(new Arg(CompactMethod.of(method),
                                    AnnotationCommand.of(command, true)));
                    }
                }

            }
        }
    }

    public static class Arg {
        private CompactMethod compactMethod;
        private AnnotationCommand annotationCommand;

        private Arg(CompactMethod compactMethod, AnnotationCommand annotationCommand) {
            this.compactMethod = compactMethod;
            this.annotationCommand = annotationCommand;
        }

        public CompactMethod getCompactMethod() {
            return compactMethod;
        }

        public AnnotationCommand getAnnotationCommand() {
            return annotationCommand;
        }

        public String getOnlyArg() {
            return this.getAnnotationCommand().getCommand().split(" ")[1];
        }
    }
}
