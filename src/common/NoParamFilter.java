package common;

import common.parameters.Parameter;

import java.util.List;

public interface NoParamFilter extends Filter {
    @Override
    default List<Parameter> getParameters() {
        return List.of();
    }
}
