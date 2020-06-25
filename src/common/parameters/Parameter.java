package common.parameters;


public abstract class Parameter {
    private final String name;

    public Parameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
