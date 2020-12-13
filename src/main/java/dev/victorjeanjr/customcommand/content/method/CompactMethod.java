package dev.victorjeanjr.customcommand.content.method;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CompactMethod {

    private Class<?> clazz;
    private String nameMethod;
    private Parameter[] parameters;

    private Class<?>[] parameterClass;

    private CompactMethod(Class<?> clazz, String nameMethod, Parameter[] parameters, Class<?>[] parameterClass) {
        this.clazz = clazz;
        this.nameMethod = nameMethod;
        this.parameters = parameters;
        this.parameterClass = parameterClass;
    }

    public Method constructMethod() throws NoSuchMethodException {
        return this.getClazz().getMethod(this.getNameMethod(), this.getParameterClass());
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getNameMethod() {
        return nameMethod;
    }

    public Class<?>[] getParameterClass() {
        return parameterClass;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public static CompactMethod of(Method method) {
        return new CompactMethod(method.getDeclaringClass(), method.getName(), method.getParameters(), method.getParameterTypes());
    }
}
