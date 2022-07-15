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
package dev.blocky.library.tixte.internal.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class has major inspiration from
 * <a href="https://commons.apache.org/proper/commons-lang/" target="_blank">Lang 3</a>.
 *
 * <br>Specifically StringUtils.java and ExceptionUtils.java
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.2
 */
public final class Helpers
{

    /**
     * Checks if the given {@link CharSequence} is empty.
     *
     * @param argument The argument, which should be checked.
     *
     * @return  <b>true</b> - If the given {@link CharSequence} is empty.
     *          <br><b>false</b> - If the given {@link CharSequence} is not empty.
     */
    public static boolean isEmpty(@NotNull CharSequence argument)
    {
        return argument == null || argument.length() == 0;
    }

    /**
     * Checks if the given {@link CharSequence} contains whitespaces.
     *
     * @param argument The argument, which should be checked.
     *
     * @return <b>true</b> - If the given {@link CharSequence} contains whitespaces.
     *         <br><b>false</b> - If the given {@link CharSequence} contains whitespaces.
     */
    public static boolean containsWhitespace(@NotNull CharSequence argument)
    {
        if (isEmpty(argument))
        {
            return false;
        }

        for (int i = 0; i < argument.length(); i++)
        {
            if (Character.isWhitespace(argument.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a formatted string using {@link Locale#ROOT}, the specified format string, and the specified arguments.
     *
     * @param format A format string.
     * @param args   Arguments referenced by the format specifiers in the format string. If there are more arguments
     *               than format specifiers, the extra arguments are ignored. The number of arguments is variable and
     *               may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as
     *               defined by <i> The Java™ Virtual Machine Specification</i>. The behaviour on a {@code null} argument
     *               depends on the conversion.
     *
     * @return A formatted string.
     */
    @NotNull
    public static String format(@NotNull String format, @NotNull Object... args)
    {
        return String.format(Locale.ROOT, format, args);
    }

    /**
     * Gets the number of unicode code points in the specified text range.
     *
     * @param string The string of which the code points should be counted.
     *
     * @return The number of unicode code points in the specified text range.
     */
    public static int codePointLength(@NotNull String string)
    {
        return string.codePointCount(0, string.length());
    }
}
