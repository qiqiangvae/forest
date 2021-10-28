package online.qiqiang.forest.framework.log;

import com.fasterxml.jackson.databind.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
public class JacksonLogPrinterCustomizer {
    private final List<Module> modules = new ArrayList<>();

    public void addModel(Module module) {
        modules.add(module);
    }

    protected List<Module> getModules() {
        return modules;
    }
}
