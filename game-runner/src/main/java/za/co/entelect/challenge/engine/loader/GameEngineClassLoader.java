package za.co.entelect.challenge.engine.loader;

import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Set;
import java.util.ServiceLoader;

public class GameEngineClassLoader {
    public <T> T loadEngineObject(Class<T> cl) throws Exception {
        final ServiceLoader<T> loader = ServiceLoader.load(cl);
        final Iterator<T> iterator = loader.iterator();
        if (!iterator.hasNext())
            throw new Exception("Couldn't find implementation for " + cl.toString() + " on the classpath.");
        return iterator.next();
    }
}
