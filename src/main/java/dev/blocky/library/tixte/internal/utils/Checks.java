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

import dev.blocky.library.tixte.annotations.Undocumented;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Utility class for handling errors.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.1
 */
public class Checks
{
    @Undocumented
    @Contract("false, _ -> fail")
    public static void check(final boolean expression, final String message)
    {
        if (!expression)
            throw new IllegalArgumentException(message);
    }

    @Undocumented
    @Contract("false, _, _ -> fail")
    public static void check(final boolean expression, final String message, final Object arg)
    {
        if (!expression)
            throw new IllegalArgumentException(String.format(message, arg));
    }

    @Undocumented
    @Contract("null, _ -> fail")
    public static void notNull(@Nullable Object argument, @NotNull String name)
    {
        if (argument == null)
        {
            throw new IllegalArgumentException("\"" + name + "\" may not be null.");
        }
    }

    @Undocumented
    @Contract("null, _ -> fail")
    public static void notEmpty(@Nullable CharSequence argument, @NotNull String name)
    {
        notNull(argument, name);

        if (isEmpty(argument))
        {
            throw new IllegalStateException("\"" + name + "\" cannot be undefined.");
        }
    }

    @Undocumented
    @Contract("null, _ -> fail")
    public static void noWhitespace(@Nullable CharSequence argument, @NotNull String name)
    {
        notNull(argument, name);

        if (containsWhitespace(argument))
        {
            throw new IllegalArgumentException("\"" + name + "\" may not contain blanks. Provided: \"" + argument + "\"");
        }
    }

    @Undocumented
    public static void notNegative(int number, @NotNull String name)
    {
        if (number < 0)
        {
            throw new IllegalStateException("\"" + name + "\" cannot be under 0.");
        }
    }

    @Undocumented
    static boolean isEmpty(@NotNull CharSequence seq)
    {
        return seq == null || seq.length() == 0;
    }

    @Undocumented
    static boolean containsWhitespace(@NotNull CharSequence charSequence)
    {
        if (isEmpty(charSequence))
        {
            return false;
        }

        for (int i = 0; i < charSequence.length(); i++)
        {
            if (Character.isWhitespace(charSequence.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Undocumented
    public static String format(String format, Object... args)
    {
        return String.format(Locale.ROOT, format, args);
    }

    @Undocumented
    public static int codePointLength(@NotNull String string)
    {
        return string.codePointCount(0, string.length());
    }
}
