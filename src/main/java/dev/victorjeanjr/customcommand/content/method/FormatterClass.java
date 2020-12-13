package dev.victorjeanjr.customcommand.content.method;

import dev.victorjeanjr.customcommand.content.Commands;
import dev.victorjeanjr.customcommand.content.executor.Executor;
import dev.victorjeanjr.customcommand.objects.AnnotationCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;

public class FormatterClass<T> {

    private Class<?> clazz;
    private Formatter<T> formatter;

    public FormatterClass(Class<?> clazz, Formatter<T> formatter) {
        this.clazz = clazz;
        this.formatter = formatter;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Formatter<T> getFormatter() {
        return formatter;
    }

    public static Collection<Object> convert(Commands commands, Class<?>[] clazz, CommandSender commandSender,
                                             AnnotationCommand annotationCommand, String[] args) {
        Collection<Object> objects = new ArrayList<>();
        int i = 0;
        for(Class<?> c : clazz) {
            if(c.isAssignableFrom(Executor.class)) {
                objects.add(Executor.of(commandSender, annotationCommand));
            } else {
                for(FormatterClass formatterClass : commands.getFormats()) {
                    if(c.isAssignableFrom(formatterClass.getClazz())) {
                        Object valueArg;
                        try {
                            valueArg = formatterClass.getFormatter().parse(args[i]);
                        } catch (Exception exception) {
                            valueArg = null;
                        }
                        objects.add(valueArg);
                    }
                }
            }
            i++;
        }
        return objects;
    }

    public interface Formatter<T> {
        T parse(String format);
    }

}
