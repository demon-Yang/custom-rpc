package com.yxd.extension;

import com.yxd.annotation.SPI;
import com.yxd.util.LogbackUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @Description：类似dubbo的扩展程序
 * @Date 2020/12/22 16:04
 * @Author YXD
 * @Version 1.0
 */
public final class ExtensionLoader<T> {
    private static final String SERVICE_DIRECTORY = "META-INF/extension/";
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Class type;
    private final Map<String, Object> cachedInstances = new ConcurrentHashMap<>();
    private final Map<String, Class<?>> cachedClasses = new ConcurrentHashMap<>();

    private ExtensionLoader(Class type) {
        this.type = type;
    }

    /**
     * 每个接口类加载一个extensionLoader
     * @param type
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface.");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated by @SPI");
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

    /**
     * 获取扩展类
     * @param name
     * @return
     */
    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Extension name should not be null or empty.");
        }
        Object instance = cachedInstances.get(name);
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    instance = createExtension(name);
                    cachedInstances.putIfAbsent(name, instance);
                }
            }
        }
        return (T) instance;
    }

    /**
     * 生成扩展类
     * @param name
     * @return
     */
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("No such extension of name " + name);
        }
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (Exception e) {
                e.printStackTrace();
                LogbackUtil.error(e.toString());
            }
        }
        return instance;
    }

    /**
     * 获取该接口所有的扩展类
     * @return
     */
    private Map<String, Class<?>> getExtensionClasses() {
        if (cachedClasses.size() == 0) {
            synchronized (this) {
                if (cachedClasses.size() == 0) {
                    //加载该接口类的全部扩展
                    loadDirectory(cachedClasses);
                }
            }
        }
        return cachedClasses;
    }

    /**
     * 加载文件
     * @param extensionClasses
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        String fileName = ExtensionLoader.SERVICE_DIRECTORY + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogbackUtil.error(e.toString());
        }
    }

    /**
     * 解析文件
     * @param extensionClasses
     * @param classLoader
     * @param resourceUrl
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), UTF_8))) {
            String line;
            // read every line
            while ((line = reader.readLine()) != null) {
                // get index of comment
                final int ci = line.indexOf('#');
                if (ci >= 0) {
                    // string after # is comment so we ignore it
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        final int ei = line.indexOf('=');
                        String name = line.substring(0, ei).trim();
                        String clazzName = line.substring(ei + 1).trim();
                        // our SPI use key-value pair so both of them must not be empty
                        if (name.length() > 0 && clazzName.length() > 0) {
                            Class<?> clazz = classLoader.loadClass(clazzName);
                            extensionClasses.put(name, clazz);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogbackUtil.error(e.toString());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogbackUtil.error(e.toString());
        }
    }
}
