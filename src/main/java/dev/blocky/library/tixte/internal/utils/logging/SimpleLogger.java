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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.Util;
import org.slf4j.spi.LocationAwareLogger;

import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static dev.blocky.library.tixte.internal.utils.logging.SimpleLogger.SimpleLoggerConfiguration.*;

/**
 * A custom {@link SimpleLogger}. (from <a href="https://www.slf4j.org/api/org/slf4j/simple/SimpleLogger.html">slf4j-simple</a>).
 *
 * @author BlockyDotJar
 * @version v1.1.1
 * @since v1.0.0-alpha.3
 */
public class SimpleLogger extends LegacyAbstractLogger
{

    private static final long serialVersionUID = -632788891211436180L;

    private static final long START_TIME = System.currentTimeMillis();

    private static final int LOG_LEVEL_TRACE = LocationAwareLogger.TRACE_INT;
    private static final int LOG_LEVEL_DEBUG = LocationAwareLogger.DEBUG_INT;
    private static final int LOG_LEVEL_INFO = LocationAwareLogger.INFO_INT;
    private static final int LOG_LEVEL_WARN = LocationAwareLogger.WARN_INT;
    private static final int LOG_LEVEL_ERROR = LocationAwareLogger.ERROR_INT;

    private static final char SP = ' ';
    private static final String TID_PREFIX = "tid=";

    private static final int LOG_LEVEL_OFF = LOG_LEVEL_ERROR + 10;

    private static boolean INITIALIZED = false;

    public static int currentLogLevel = LOG_LEVEL_INFO;

    public transient String shortLogName;

    public static final String SYSTEM_PREFIX = "org.slf4j.simpleLogger.";

    public static final String LOG_KEY_PREFIX = SYSTEM_PREFIX + "log.";

    public static final String CACHE_OUTPUT_STREAM_STRING_KEY = SimpleLogger.SYSTEM_PREFIX + "cacheOutputStream";

    public static final String WARN_LEVEL_STRING_KEY = SimpleLogger.SYSTEM_PREFIX + "warnLevelString";

    public static final String LEVEL_IN_BRACKETS_KEY = SimpleLogger.SYSTEM_PREFIX + "levelInBrackets";

    public static final String LOG_FILE_KEY = SimpleLogger.SYSTEM_PREFIX + "logFile";

    public static final String SHOW_SHORT_LOG_NAME_KEY = SimpleLogger.SYSTEM_PREFIX + "showShortLogName";

    public static final String SHOW_LOG_NAME_KEY = SimpleLogger.SYSTEM_PREFIX + "showLogName";

    public static final String SHOW_THREAD_NAME_KEY = SimpleLogger.SYSTEM_PREFIX + "showThreadName";

    public static final String SHOW_THREAD_ID_KEY = SimpleLogger.SYSTEM_PREFIX + "showThreadId";

    public static final String DATE_TIME_FORMAT_KEY = SimpleLogger.SYSTEM_PREFIX + "dateTimeFormat";

    public static final String SHOW_DATE_TIME_KEY = SimpleLogger.SYSTEM_PREFIX + "showDateTime";

    public static final String DEFAULT_LOG_LEVEL_KEY = SimpleLogger.SYSTEM_PREFIX + "defaultLogLevel";

    SimpleLogger(@NotNull String name)
    {
        if (!INITIALIZED)
        {
            init();
        }

        this.name = name;

        String levelString = recursivelyComputeLevelString();

        if (levelString != null)
        {
            currentLogLevel = stringToLevel(levelString);
        }
        else
        {
            currentLogLevel = DEFAULT_LOG_LEVEL;
        }
    }

    @Nullable
    @CheckReturnValue
    private String recursivelyComputeLevelString()
    {
        String tempName = name;
        String levelString = null;

        int indexOfLastDot = tempName.length();

        while ((levelString == null) && (indexOfLastDot > -1))
        {
            tempName = tempName.substring(0, indexOfLastDot);
            levelString = getStringProperty(LOG_KEY_PREFIX + tempName, null);
            indexOfLastDot = tempName.lastIndexOf(".");
        }
        return levelString;
    }

    private synchronized void write(@NotNull StringBuilder buf, @Nullable Throwable t)
    {
        PrintStream targetStream = OUTPUT_CHOICE.getTargetPrintStream();

        targetStream.println(buf);
        writeThrowable(t, targetStream);
        targetStream.flush();
    }

    private void writeThrowable(@Nullable Throwable t, @NotNull PrintStream targetStream)
    {
        if (t != null)
        {
            t.printStackTrace(targetStream);
        }
    }

    @NotNull
    private synchronized String getFormattedDate()
    {
        Date now = new Date();
        String dateText;

        dateText = DATE_FORMATTER.format(now);

        return dateText;
    }

    @NotNull
    private String computeShortName()
    {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * Is the given log level currently enabled?
     *
     * @param logLevel Is this level enabled?
     *
     * @return <b>true</b> - If this level is enabled.
     *         <br><b>false</b> - If this level is not enabled.
     */
    private boolean isLevelEnabled(int logLevel)
    {
        return (logLevel >= currentLogLevel);
    }

    /**
     * Are {@code trace} messages currently enabled?
     *
     * @return <b>true</b> - If trace messages are enabled.
     *         <br><b>false</b> - If trace messages are not enabled.
     */
    @Override
    public boolean isTraceEnabled()
    {
        return isLevelEnabled(LOG_LEVEL_TRACE);
    }

    /**
     * Are {@code debug} messages currently enabled?
     *
     * @return <b>true</b> - If debug messages are enabled.
     *         <br><b>false</b> - If debug messages are not enabled.
     */
    @Override
    public boolean isDebugEnabled()
    {
        return isLevelEnabled(LOG_LEVEL_DEBUG);
    }

    /**
     * Are {@code info} messages currently enabled?
     *
     * @return <b>true</b> - If info messages are enabled.
     *         <br><b>false</b> - If info messages are not enabled.
     */
    @Override
    public boolean isInfoEnabled()
    {
        return isLevelEnabled(LOG_LEVEL_INFO);
    }

    /**
     * Are {@code warn} messages currently enabled?
     *
     * @return <b>true</b> - If warn messages are enabled.
     *         <br><b>false</b> - If warn messages are not enabled.
     */
    @Override
    public boolean isWarnEnabled()
    {
        return isLevelEnabled(LOG_LEVEL_WARN);
    }

    /**
     * Are {@code error} messages currently enabled?
     *
     * @return <b>true</b> - If error messages are enabled.
     *         <br><b>false</b> - If error messages are not enabled.
     */
    @Override
    public boolean isErrorEnabled()
    {
        return isLevelEnabled(LOG_LEVEL_ERROR);
    }

    @Override
    protected void handleNormalizedLoggingCall(@NotNull Level level, @NotNull Marker marker, @NotNull String messagePattern,
                                               @NotNull Object[] arguments, @Nullable Throwable t)
    {
        List<Marker> markers = null;

        if (marker != null)
        {
            markers = new ArrayList<>();
            markers.add(marker);
        }

        innerHandleNormalizedLoggingCall(level, markers, messagePattern, arguments, t);
    }

    private void innerHandleNormalizedLoggingCall(@NotNull Level level, @NotNull List<Marker> markers, @NotNull String messagePattern,
                                                  @NotNull Object[] arguments, @Nullable Throwable t)
    {
        StringBuilder buf = new StringBuilder(32);

        if (SHOW_DATE_TIME)
        {
            if (DATE_FORMATTER != null)
            {
                buf.append(getFormattedDate());
                buf.append(SP);
            }
            else
            {
                buf.append(System.currentTimeMillis() - START_TIME);
                buf.append(SP);
            }
        }

        if (SHOW_THREAD_NAME)
        {
            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
        }

        if (SHOW_THREAD_ID)
        {
            buf.append(TID_PREFIX);
            buf.append(Thread.currentThread().getId());
            buf.append(SP);
        }

        if (LEVEL_IN_BRACKETS)
        {
            buf.append('[');
        }

        String levelStr = level.name();
        buf.append(levelStr);

        if (LEVEL_IN_BRACKETS)
        {
            buf.append(']');
        }

        buf.append(SP);

        if (SHOW_SHORT_LOG_NAME)
        {
            if (shortLogName == null)
            {
                shortLogName = computeShortName();
            }

            buf.append(shortLogName).append(" - ");
        }
        else if (SHOW_LOG_NAME)
        {
            buf.append(name).append(" - ");
        }

        if (markers != null)
        {
            buf.append(SP);
            for (Marker marker : markers)
            {
                buf.append(marker.getName()).append(SP);
            }
        }

        String formattedMessage = MessageFormatter.basicArrayFormat(messagePattern, arguments);

        buf.append(formattedMessage);

        write(buf, t);
    }

    @Override
    protected String getFullyQualifiedCallerName()
    {
        return null;
    }

    /**
     * This class holds configuration values for {@link SimpleLogger}.
     * <br>The values are computed at runtime.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-beta.2
     */
    static class SimpleLoggerConfiguration
    {
        private static final String CONFIGURATION_FILE = "simpleLogger.properties";

        private static final int DEFAULT_LOG_LEVEL_DEFAULT = LOG_LEVEL_INFO;
        static int DEFAULT_LOG_LEVEL = DEFAULT_LOG_LEVEL_DEFAULT;

        private static final boolean SHOW_DATE_TIME_DEFAULT = false;
        static boolean SHOW_DATE_TIME = SHOW_DATE_TIME_DEFAULT;

        private static final String DATE_TIME_FORMAT_STR_DEFAULT = null;
        private static String DATE_TIME_FORMAT_STR = DATE_TIME_FORMAT_STR_DEFAULT;

        static DateFormat DATE_FORMATTER;

        private static final boolean SHOW_THREAD_NAME_DEFAULT = true;
        static boolean SHOW_THREAD_NAME = SHOW_THREAD_NAME_DEFAULT;

        private static final boolean SHOW_THREAD_ID_DEFAULT = false;
        static boolean SHOW_THREAD_ID = SHOW_THREAD_ID_DEFAULT;

        private static final boolean SHOW_LOG_NAME_DEFAULT = true;
        static boolean SHOW_LOG_NAME = SHOW_LOG_NAME_DEFAULT;

        private static final boolean SHOW_SHORT_LOG_NAME_DEFAULT = false;
        static boolean SHOW_SHORT_LOG_NAME = SHOW_SHORT_LOG_NAME_DEFAULT;

        private static final boolean LEVEL_IN_BRACKETS_DEFAULT = false;
        static boolean LEVEL_IN_BRACKETS = LEVEL_IN_BRACKETS_DEFAULT;

        private static final String LOG_FILE_DEFAULT = "System.err";
        private static String LOG_FILE = LOG_FILE_DEFAULT;
        static OutputChoice OUTPUT_CHOICE;

        private static final boolean CACHE_OUTPUT_STREAM_DEFAULT = false;
        private static boolean CACHE_OUTPUT_STREAM = CACHE_OUTPUT_STREAM_DEFAULT;

        private static final String WARN_LEVEL_STRING_DEFAULT = "WARN";
        static String WARN_LEVEL_STRING = WARN_LEVEL_STRING_DEFAULT;

        private static final Properties SIMPLE_LOGGER_PROPS = new Properties();

        static void init()
        {
            INITIALIZED = true;
            loadProperties();

            String defaultLogLevelString = getStringProperty(DEFAULT_LOG_LEVEL_KEY, null);

            if (defaultLogLevelString != null)
            {
                DEFAULT_LOG_LEVEL = stringToLevel(defaultLogLevelString);
            }

            SHOW_LOG_NAME = getBooleanProperty(SHOW_LOG_NAME_KEY, SHOW_LOG_NAME_DEFAULT);
            SHOW_SHORT_LOG_NAME = getBooleanProperty(SHOW_SHORT_LOG_NAME_KEY, SHOW_SHORT_LOG_NAME_DEFAULT);
            SHOW_DATE_TIME = getBooleanProperty(SHOW_DATE_TIME_KEY, SHOW_DATE_TIME_DEFAULT);
            SHOW_THREAD_NAME = getBooleanProperty(SHOW_THREAD_NAME_KEY, SHOW_THREAD_NAME_DEFAULT);
            SHOW_THREAD_ID = getBooleanProperty(SHOW_THREAD_ID_KEY, SHOW_THREAD_ID_DEFAULT);

            DATE_TIME_FORMAT_STR = getStringProperty(DATE_TIME_FORMAT_KEY, DATE_TIME_FORMAT_STR_DEFAULT);
            LEVEL_IN_BRACKETS = getBooleanProperty(LEVEL_IN_BRACKETS_KEY, LEVEL_IN_BRACKETS_DEFAULT);
            WARN_LEVEL_STRING = getStringProperty(WARN_LEVEL_STRING_KEY, WARN_LEVEL_STRING_DEFAULT);

            LOG_FILE = getStringProperty(LOG_FILE_KEY, LOG_FILE);

            CACHE_OUTPUT_STREAM = getBooleanProperty(CACHE_OUTPUT_STREAM_STRING_KEY, CACHE_OUTPUT_STREAM_DEFAULT);

            OUTPUT_CHOICE = computeOutputChoice(LOG_FILE, CACHE_OUTPUT_STREAM);

            if (DATE_TIME_FORMAT_STR != null)
            {
                try
                {
                    DATE_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
                }
                catch (IllegalArgumentException e)
                {
                    Util.report("Bad date format in " + CONFIGURATION_FILE + "; will output relative time", e);
                }
            }
        }

        private static void loadProperties()
        {
            InputStream in = AccessController.doPrivileged((PrivilegedAction<InputStream>) () ->
            {
                ClassLoader threadCL = Thread.currentThread().getContextClassLoader();

                if (threadCL != null)
                {
                    return threadCL.getResourceAsStream(CONFIGURATION_FILE);
                }
                else
                {
                    return ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE);
                }
            });

            if (null != in)
            {
                try
                {
                    SIMPLE_LOGGER_PROPS.load(in);
                }
                catch (IOException e)
                {
                    // Ignored.
                }
                finally
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException e)
                    {
                        // Ignored.
                    }
                }
            }
        }

        @Nullable
        @CheckReturnValue
        static String getStringProperty(@NotNull String name, @Nullable String defaultValue)
        {
            String prop = getStringProperty(name);
            return (prop == null) ? defaultValue : prop;
        }

        static boolean getBooleanProperty(@NotNull String name, boolean defaultValue)
        {
            String prop = getStringProperty(name);
            return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
        }

        @Nullable
        @CheckReturnValue
        static String getStringProperty(@NotNull String name)
        {
            String prop = null;

            try
            {
                prop = System.getProperty(name);
            }
            catch (SecurityException e)
            {
                // Ignore.
            }
            return (prop == null) ? SIMPLE_LOGGER_PROPS.getProperty(name) : prop;
        }

        static int stringToLevel(@NotNull String levelStr)
        {
            if ("trace".equalsIgnoreCase(levelStr))
            {
                return LOG_LEVEL_TRACE;
            }
            else if ("debug".equalsIgnoreCase(levelStr))
            {
                return LOG_LEVEL_DEBUG;
            }
            else if ("info".equalsIgnoreCase(levelStr))
            {
                return LOG_LEVEL_INFO;
            }
            else if ("warn".equalsIgnoreCase(levelStr))
            {
                return LOG_LEVEL_WARN;
            }
            else if ("error".equalsIgnoreCase(levelStr))
            {
                return LOG_LEVEL_ERROR;
            }
            else if ("off".equalsIgnoreCase(levelStr))
            {
                return LOG_LEVEL_OFF;
            }

            // Assume INFO by default.
            return LOG_LEVEL_INFO;
        }

        @NotNull
        static OutputChoice computeOutputChoice(@NotNull String logFile, boolean cacheOutputStream)
        {
            if ("System.err".equalsIgnoreCase(logFile))
            {
                if (cacheOutputStream)
                {
                    return new OutputChoice(OutputChoiceType.CACHED_SYS_ERR);
                }
                else
                {
                    return new OutputChoice(OutputChoiceType.SYS_ERR);
                }
            }
            else if ("System.out".equalsIgnoreCase(logFile))
            {
                if (cacheOutputStream)
                {
                    return new OutputChoice(OutputChoiceType.CACHED_SYS_OUT);
                }
                else
                {
                    return new OutputChoice(OutputChoiceType.SYS_OUT);
                }
            }
            else
            {
                try
                {
                    FileOutputStream fos = new FileOutputStream(logFile);
                    PrintStream printStream = new PrintStream(fos);
                    return new OutputChoice(printStream);
                }
                catch (FileNotFoundException e)
                {
                    Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
                    return new OutputChoice(OutputChoiceType.SYS_ERR);
                }
            }
        }
    }

    /**
     * This class encapsulates the user's choice of output target.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-beta.2
     */
    private static class OutputChoice
    {
        private final OutputChoiceType OUTPUT_CHOICE_TYPE;
        private final PrintStream TARGET_PRINT_STREAM;

        private OutputChoice(@NotNull OutputChoiceType outputChoiceType)
        {
            if (outputChoiceType == OutputChoiceType.FILE)
            {
                throw new IllegalArgumentException();
            }

            this.OUTPUT_CHOICE_TYPE = outputChoiceType;

            if (outputChoiceType == OutputChoiceType.CACHED_SYS_OUT)
            {
                this.TARGET_PRINT_STREAM = System.out;
            }
            else if (outputChoiceType == OutputChoiceType.CACHED_SYS_ERR)
            {
                this.TARGET_PRINT_STREAM = System.err;
            }
            else
            {
                this.TARGET_PRINT_STREAM = null;
            }
        }

        private OutputChoice(@NotNull PrintStream printStream)
        {
            this.OUTPUT_CHOICE_TYPE = OutputChoiceType.FILE;
            this.TARGET_PRINT_STREAM = printStream;
        }

        @NotNull
        private PrintStream getTargetPrintStream()
        {
            switch (OUTPUT_CHOICE_TYPE)
            {
            case SYS_OUT:
                return System.out;
            case SYS_ERR:
                return System.err;
            case CACHED_SYS_ERR:
            case CACHED_SYS_OUT:
            case FILE:
                return TARGET_PRINT_STREAM;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private enum OutputChoiceType
    {
        SYS_OUT, CACHED_SYS_OUT, SYS_ERR, CACHED_SYS_ERR, FILE
    }
}
