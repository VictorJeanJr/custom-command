package dev.victorjeanjr.customcommand.content;

import com.google.common.collect.Lists;
import dev.victorjeanjr.customcommand.content.executor.CE;
import dev.victorjeanjr.customcommand.content.method.DataAnnotation;
import dev.victorjeanjr.customcommand.content.method.FormatterClass;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;

public class Commands {

    private Plugin plugin;

    private Collection<DataAnnotation> dataAnnotations;
    private Collection<FormatterClass<?>> formats;

    public Commands(Plugin plugin) {
        this.plugin = plugin;
        this.dataAnnotations = Lists.newCopyOnWriteArrayList();
        this.formats = Lists.newCopyOnWriteArrayList();

        this.registerFormat(String.class, String::valueOf);
        this.registerFormat(Integer.class, Integer::valueOf);
        this.registerFormat(int.class, Integer::valueOf);
        this.registerFormat(Double.class, Double::valueOf);
    }

    public <T> void registerFormat(Class<?> clazz, FormatterClass.Formatter<T> formatter) {
        this.formats.add(new FormatterClass<>(clazz, formatter));
    }

    public void registerCommand(JavaPlugin plugin, Class<?> clazz, String... commands) {
        DataAnnotation.register(clazz, this);
        Arrays.asList(commands).forEach(e -> plugin.getCommand(e).setExecutor(new CE(this)));
    }

    public DataAnnotation get(String command) {
        return this.dataAnnotations.stream().filter(e ->
                e.getAnnotationCommand().getCommand().equalsIgnoreCase(
                        command.contains(" ") ? command.split(" ")[0] : command))
                .findFirst().orElse(null);
    }

    public Collection<FormatterClass<?>> getFormats() {
        return formats;
    }

    public Collection<DataAnnotation> getDataAnnotations() {
        return dataAnnotations;
    }
}
