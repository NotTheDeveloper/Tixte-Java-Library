/**
 * Copyright 2022 Dominic (aka. BlockyDotJar)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.blocky.library.tixte.internal.utils.logging;

import com.google.errorprone.annotations.CheckReturnValue;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * This class serves as a {@link LoggerFactory} for Tixte4J's internals.
 * <br>It will either return a logger from a SLF4J implementation via {@link LoggerFactory} if present,
 * or an instance of a custom {@link SimpleLogger}. (from slf4j-simple)
 * <br>It also has the utility method {@link #getLazyString(LazyEvaluation)} which is used to lazily construct strings for logging.
 *
 * @author BlockyDotJar
 * @version v1.2.0
 * @since v1.0.0-alpha.3
 */
public class TixteLogger
{
    /**
     * Marks whether a SLF4J {@code StaticLoggerBinder} (pre 1.8.x) or
     * {@code SLF4JServiceProvider} implementation (1.8.x+) was found.
     * <br>If false, Tixte4J will use its fallback logger.
     * <br>This variable is initialized during static class initialization.
     */
    public static final boolean SLF4J_ENABLED;
    private static final Map<String, Logger> LOGS = new CaseInsensitiveMap<>();

    TixteLogger()
    {
    }

    static
    {
        boolean SLF4J;
        try
        {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");

            SLF4J = true;
        }
        catch (ClassNotFoundException eStatic)
        {
            // There is no StaticLoggerBinder (SLF4J pre-1.8.x)

            try
            {
                Class<?> serviceProviderInterface = Class.forName("org.slf4j.spi.SLF4JServiceProvider");

                // Check if there is a service implementation for the service, indicating a provider for SLF4J 1.8.x+ is installed
                SLF4J = ServiceLoader.load(serviceProviderInterface).iterator().hasNext();
            }
            catch (ClassNotFoundException eService)
            {
                // There was no ServiceProvider interface (SLF4J 1.8.x+)

                // Prints warning of missing implementation.
                LoggerFactory.getLogger(TixteLogger.class);

                SLF4J = false;
            }
        }
        SLF4J_ENABLED = SLF4J;
    }

    /**
     * Will get the {@link Logger} with the given log-name
     * or create and cache a fallback logger if there is no SLF4J implementation present.
     * <br>The fallback logger will be an instance of a slightly modified version of SLF4J's {@code SimpleLogger}.
     *
     * @param name The name of the logger.
     *
     * @return Logger with given log name.
     */
    @NotNull
    public static Logger getLog(@NotNull String name)
    {
        synchronized (LOGS)
        {
            if (SLF4J_ENABLED)
            {
                return LoggerFactory.getLogger(name);
            }
            return LOGS.computeIfAbsent(name, SimpleLogger::new);
        }
    }

    /**
     * Will get the {@link Logger} for the given class
     * or create and cache a fallback logger if there is no SLF4J implementation present.
     * <br>The fallback logger will be an instance of a slightly modified version of SLF4J's {@code SimpleLogger}.
     *
     * @param clazz The class used for the logger name.
     *
     * @return Logger for given class.
     */
    @NotNull
    public static Logger getLog(@NotNull Class<?> clazz)
    {
        synchronized (LOGS)
        {
            if (SLF4J_ENABLED)
            {
                return LoggerFactory.getLogger(clazz);
            }
            return LOGS.computeIfAbsent(clazz.getName(), (n) -> new SimpleLogger(clazz.getSimpleName()));
        }
    }

    /**
     * Utility function to enable logging of complex statements more efficiently. (lazy)
     *
     * @param lazyLambda The supplier used when evaluating the expression.
     *
     * @return An object that can be passed to SLF4J's logging methods as lazy parameter.
     */
    @Nullable
    @CheckReturnValue
    public static Object getLazyString(@NotNull LazyEvaluation lazyLambda)
    {
        return new Object()
        {
            @Override
            public String toString()
            {
                try
                {
                    return lazyLambda.getString();
                }
                catch (Exception ex)
                {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    return "Error while evaluating lazy string... " + sw;
                }
            }
        };
    }

    /**
     * Functional interface used for {@link #getLazyString(LazyEvaluation)} to lazily construct a string.
     *
     * @author BlockyDotJar
     * @version v1.1.0
     * @since v1.0.0-alpha.3
     */
    @FunctionalInterface
    public interface LazyEvaluation
    {

        /**
         * This method is used by {@link #getLazyString(LazyEvaluation)}
         * when SLF4J requests string construction.
         * <br>The string returned by this is used to construct the log message.
         *
         * @throws IOException If an I/O error occurs.
         *
         * @return The string for log message.
         */
        String getString() throws IOException;
    }
}
