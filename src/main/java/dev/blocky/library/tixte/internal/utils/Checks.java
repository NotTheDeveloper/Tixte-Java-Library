/**
 * Copyright 2022 Dominic R. (aka. BlockyDotJar), Florian Spieß (aka. MinnDevelopment) and
 * Austin Keener (aka. DV8FromTheWorld)
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Utility class for handling errors.
 *
 * @author MinnDevelopment and BlockyDotJar
 * @version v1.2.1
 * @since v1.0.0-beta.1
 */
public class Checks
{
    Checks()
    {
    }

    /**
     * The boolean, which should be checked.
     * <br>If the boolean value is false, an {@link IllegalArgumentException} will be thrown.
     *
     * @param expression The expression, which should be checked.
     * @param message The message for the exception.
     */
    @Contract("false, _ -> fail")
    public static void check(boolean expression, @NotNull String message)
    {
        if (!expression)
        {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * The boolean, which should be checked.
     * <br>If the boolean value is false, an {@link IllegalArgumentException}, which contains a formatted string, will
     * be thrown.
     *
     * @param expression The expression, which should be checked.
     * @param format A format string.
     * @param args Arguments referenced by the format specifiers in the format string. If there are more arguments
     *             than format specifiers, the extra arguments are ignored. The number of arguments is variable and
     *             may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as
     *             defined by <i> The Java™ Virtual Machine Specification</i>. The behaviour on a {@code null} argument
     *             depends on the conversion.
     */
    @Contract("false, _, _ -> fail")
    public static void check(boolean expression, @NotNull String format, @NotNull Object... args)
    {
        if (!expression)
        {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    /**
     * If the given object is null, there will be thrown an {@link IllegalArgumentException}.
     *
     * @param argument The argument, which should be checked.
     * @param name The name of the object.
     */
    @Contract("null, _ -> fail")
    public static void notNull(@Nullable Object argument, @NotNull String name)
    {
        if (argument == null)
        {
            throw new IllegalArgumentException("\"" + name + "\" may not be null.");
        }
    }

    /**
     * If the given object is null, there will be thrown an {@link IllegalArgumentException} and if the given
     * {@link CharSequence} is empty, there will be thrown an {@link IllegalStateException}.
     *
     * @param argument The argument, which should be checked.
     * @param name The name of the object.
     */
    @Contract("null, _ -> fail")
    public static void notEmpty(@Nullable CharSequence argument, @NotNull String name)
    {
        notNull(argument, name);

        if (Helpers.isEmpty(argument))
        {
            throw new IllegalStateException("\"" + name + "\" may not be empty.");
        }
    }

    /**
     * If the given object is null, there will be thrown an {@link IllegalArgumentException} and if the given
     * {@link CharSequence} contains whitespaces, there will be thrown an {@link IllegalStateException}.
     *
     * @param argument The argument, which should be checked.
     * @param name The name of the object.
     */
    @Contract("null, _ -> fail")
    public static void noWhitespace(@Nullable CharSequence argument, @NotNull String name)
    {
        notNull(argument, name);

        if (Helpers.containsWhitespace(argument))
        {
            throw new IllegalStateException("\"" + name + "\" may not contain blanks. Provided: \"" + argument + "\"");
        }
    }

    /**
     * If the given integer is below 0, there will be thrown an {@link IllegalStateException}.
     *
     * @param number The number, which should be checked.
     * @param name The name of the object.
     */
    public static void notNegative(int number, @NotNull String name)
    {
        if (number < 0)
        {
            throw new IllegalStateException("\"" + name + "\" may not be negative.");
        }
    }

    /**
     * Checks if the following inputs are containing matches.
     *
     * @param input The input to match.
     * @param pattern A compiled representation of a regex.
     * @param name The name of the object.
     */
    public static void matches(@NotNull String input, @NotNull Pattern pattern, @NotNull String name)
    {
        notNull(input, name);
        check(pattern.matcher(input).matches(), "%s must match regex ^%s$. Provided: \"%s\"", name, pattern.pattern(), input);
    }
}
