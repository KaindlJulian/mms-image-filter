package common;

import java.util.List;

public interface NoParamFilter extends Filter {
    @Override
    default List<FilterParameter> getParameters() {
        return List.of();
    }
}
