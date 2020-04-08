package za.co.entelect.challenge.engine.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;

public class GameEngineClassLoader {

    private final ClassLoader urlClassLoader;

    public GameEngineClassLoader(String gameEngineJar) throws MalformedURLException {
        urlClassLoader = new URLClassLoader(new URL[]{new URL("file:" + gameEngineJar)});
    }

    public <T> T loadEngineObject(Class<T> cl) {
        final ServiceLoader<T> loader = ServiceLoader.load(cl, urlClassLoader);
        final Iterator<T> iterator = loader.iterator();
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("Couldn't find implementation for " + cl.toString() + " on the classpath.");
        }
        return iterator.next();
    }
}