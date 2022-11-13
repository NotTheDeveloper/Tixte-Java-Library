/**
 * Copyright 2022 Dominic R. (aka. BlockyDotJar) and Florian Spieß (aka. MinnDevelopment)
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
package dev.blocky.library.tixte.internal.requests.json;

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.exceptions.ParsingException;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * This utility class can be used to access nested values within {@link DataObject DataObjects} and {@link DataArray DataArrays}.
 *
 * <p><b>Path expression grammar</b>
 *
 * <br>The syntax for paths is given by this grammar:
 *
 * <pre>{@code
 * <name-syntax>  ::= /[^.\[\]]+/;
 * <index-syntax> ::= "[" <number> "]";
 * <name>         ::= <name-syntax> | <name-syntax> "?";
 * <index>        ::= <index-syntax> | <index-syntax> "?";
 * <element>      ::= <name> ( <index> )*;
 * <path-start>   ::= <element> | <index> ( <index> )*;
 * <path>         ::= <path-start> ( "." <element> )*;
 * }</pre>
 *
 * <p><b>Examples</b>
 * <br>Given a JSON object such as:
 * <pre>{@code
 * {
 *     "array": [{
 *         "foo": "bar",
 *     }]
 * }
 * }</pre>
 *
 * The content of {@code "foo"} can be accessed using the code:
 * <pre>{@code String foo = DataPath.getString(root, "array[0].foo")}</pre>
 *
 * <p>With the safe-access operator {@code "?"}, you can also allow missing values within your path:
 * <pre>{@code String foo = DataPath.getString(root, "array[1]?.foo", "default")}</pre>
 * This will result in {@code foo == "default"}, since the array element 1 is marked as optional, and missing in the actual object.
 *
 * @author MinnDevelopment and BlockyDotJar
 * @version v1.1.1
 * @since v1.0.0
 */
public record DataPath()
{
    private static final Pattern INDEX_EXPRESSION = Pattern.compile("^\\[\\d+].*");
    private static final Pattern NAME_EXPRESSION = Pattern.compile("^[^\\[.].*");

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param <T> The result type.
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fromObject Object relative resolver of the value, this is used for the final reference and resolves the value.
     *                   <br>The first parameter is the {@link DataObject} where you get the value from, and the second is the field name.
     *                   <br>An example would be {@code (obj, name) -> obj.getString(name)} or as a method reference {@code DataObject::getString}.
     * @param fromArray Array relative resolver of the value, this is used for the final reference and resolves the value.
     *                  <br>The first parameter is the {@link DataArray} where you get the value from, and the second is the field index.
     *                  <br>An example would be {@code (array, index) -> obj.getString(index)} or as a method reference {@code DataArray::getString}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The value at the provided path, using the provided resolver functions.
     *         <br>Possibly null, if the path ends with a "?" operator, or the resolver function returns null.
     */
    @Nullable
    @CheckReturnValue
    public static <T> T get(@NotNull DataObject root, @NotNull String path, @NotNull BiFunction<DataObject, String, ? extends T> fromObject,
                            @NotNull BiFunction<DataArray, Integer, ? extends T> fromArray)
    {
        Checks.notEmpty(path, "Path");
        Checks.matches(path, NAME_EXPRESSION, "Path");
        Checks.notNull(root, "DataObject");
        Checks.notNull(fromObject, "Object Resolver");
        Checks.notNull(fromArray, "Array Resolver");
        return getUnchecked(root, path, fromObject, fromArray);
    }

    @Nullable
    @CheckReturnValue
    private static <T> T getUnchecked(@NotNull DataObject root, @NotNull String path, @NotNull BiFunction<DataObject, String, ? extends T> fromObject,
                                      @NotNull BiFunction<DataArray, Integer, ? extends T> fromArray)
    {
        final String[] parts = path.split("\\.", 2);
        final String child = parts.length > 1 ? parts[1] : null;

        String current = parts[0];

        if (current.indexOf('[') > -1)
        {
            final int arrayIndex = current.indexOf('[');

            String key = current.substring(0, arrayIndex);
            path = path.substring(arrayIndex);

            if (key.endsWith("?"))
            {
                key = key.substring(0, key.length() - 1);
                if (root.isNull(key))
                {
                    return null;
                }
            }

            return getUnchecked(root.getDataArray(key), path, fromObject, fromArray);
        }

        boolean isOptional = current.endsWith("?");

        if (isOptional)
        {
            current = current.substring(0, current.length() - 1);
        }

        if (child == null)
        {
            if (isOptional && root.isNull(current))
            {
                return null;
            }
            return fromObject.apply(root, current);
        }

        if (isOptional && root.isNull(current))
        {
            return null;
        }

        return getUnchecked(root.getDataObject(current), child, fromObject, fromArray);
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param <T> The result type.
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fromObject Object relative resolver of the value, this is used for the final reference and resolves the value.
     *                   <br>The first parameter is the {@link DataObject} where you get the value from, and the second is the field name.
     *                   <br>An example would be {@code (obj, name) -> obj.getString(name)} or as a method reference {@code DataObject::getString}.
     * @param fromArray Array relative resolver of the value, this is used for the final reference and resolves the value.
     *                  <br>The first parameter is the {@link DataArray} where you get the value from, and the second is the field index.
     *                  <br>An example would be {@code (array, index) -> obj.getString(index)} or as a method reference {@code DataArray::getString}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The value at the provided path, using the provided resolver functions.
     *         <br>Possibly null, if the path ends with a "?" operator, or the resolver function returns null.
     */
    @Nullable
    @CheckReturnValue
    public static <T> T get(@NotNull DataArray root, @NotNull String path, @NotNull BiFunction<DataObject, String, ? extends T> fromObject,
                            @NotNull BiFunction<DataArray, Integer, ? extends T> fromArray)
    {
        Checks.notNull(root, "DataArray");
        Checks.notEmpty(path, "Path");
        Checks.matches(path, INDEX_EXPRESSION, "Path");
        Checks.notNull(fromObject, "Object Resolver");
        Checks.notNull(fromArray, "Array Resolver");
        return getUnchecked(root, path, fromObject, fromArray);
    }

    @Nullable
    @CheckReturnValue
    private static <T> T getUnchecked(@NotNull DataArray root, String path, @NotNull BiFunction<DataObject, String, ? extends T> fromObject,
                                      @NotNull BiFunction<DataArray, Integer, ? extends T> fromArray)
    {
        final byte[] chars = path.getBytes(StandardCharsets.UTF_8);

        int offset = 0;

        for (int i = 0; i < chars.length; i++)
        {
            final int end = indexOf(chars, offset + 1, ']');
            final int index = Integer.parseInt(path.substring(offset + 1, end));

            offset = Math.min(chars.length, end + 1);
            final boolean optional = offset != chars.length && chars[offset] == '?';

            final boolean isMissing = root.length() <= index || root.isNull(index);
            if (optional)
            {
                offset++;
                if (isMissing)
                {
                    return null;
                }
            }

            if (offset == chars.length)
            {
                return fromArray.apply(root, index);
            }

            if (chars[offset] == '[')
            {
                root = root.getDataArray(index);
            }
            else
            {
                return getUnchecked(root.getDataObject(index), path.substring(offset + 1), fromObject, fromArray);
            }
        }

        throw new ParsingException("Array path nesting seems to be way too deep, we went " + chars.length + " arrays deep. Path: " + path);
    }

    private static int indexOf(byte[] chars, int offset, char c)
    {
        final byte b = (byte) c;
        for (int i = offset; i < chars.length; i++)
        {
            if (chars[i] == b)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *            <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *            <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The boolean value at the given path, if declared as optional this returns false when the value is missing.
     */
    public static boolean getBoolean(@NotNull DataObject root, @NotNull String path)
    {
        final Boolean bool = get(root, path, DataObject::getBoolean, DataArray::getBoolean);
        return bool != null && bool;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             This must start with a name element, such as {@code "foo"}.
     * @param fallback The boolean, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The boolean value at the given path, if declared as optional this returns the provided fallback when the value is missing.
     */
    public static boolean getBoolean(@NotNull DataObject root, @NotNull String path, boolean fallback)
    {
        final Boolean bool = get(root, path, (obj, key) -> obj.getBoolean(key, fallback), (arr, index) -> arr.getBoolean(index, fallback));
        return bool != null ? bool : fallback;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The boolean value at the given path, if declared as optional this returns false when the value is missing.
     */
    public static boolean getBoolean(@NotNull DataArray root, @NotNull String path)
    {
        final Boolean bool = get(root, path, DataObject::getBoolean, DataArray::getBoolean);
        return bool != null && bool;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The boolean, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The boolean value at the given path, if declared as optional this returns the provided fallback when the value is missing.
     */
    public static boolean getBoolean(@NotNull DataArray root, @NotNull String path, boolean fallback)
    {
        final Boolean bool = get(root, path, (obj, key) -> obj.getBoolean(key, fallback), (arr, index) -> arr.getBoolean(index, fallback));
        return bool != null ? bool : fallback;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseInt(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The int value at the given path.
     */
    public static int getInt(@NotNull DataObject root, @NotNull String path)
    {
        final Integer integer = get(root, path, DataObject::getInt, DataArray::getInt);

        if (integer == null)
        {
            pathError(path, "int");
        }
        return integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseInt(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fallback The integer, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The int value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static int getInt(@NotNull DataObject root, @NotNull String path, int fallback)
    {
        final Integer integer = get(root, path, (obj, key) -> obj.getInt(key, fallback), (arr, index) -> arr.getInt(index, fallback));
        return integer == null ? fallback : integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseInt(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The int value at the given path.
     */
    public static int getInt(@NotNull DataArray root, @NotNull String path)
    {
        final Integer integer = get(root, path, DataObject::getInt, DataArray::getInt);

        if (integer == null)
        {
            pathError(path, "int");
        }
        return integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseInt(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The integer, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The int value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static int getInt(@NotNull DataArray root, @NotNull String path, int fallback)
    {
        final Integer integer = get(root, path, (obj, key) -> obj.getInt(key, fallback), (arr, index) -> arr.getInt(index, fallback));
        return integer == null ? fallback : integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseUnsignedInt(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned int value at the given path.
     */
    public static int getUnsignedInt(@NotNull DataObject root, @NotNull String path)
    {
        final Integer integer = get(root, path, DataObject::getUnsignedInt, DataArray::getUnsignedInt);

        if (integer == null)
        {
            pathError(path, "unsigned int");
        }
        return integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseUnsignedInt(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fallback The unsigned integer, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned int value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static int getUnsignedInt(@NotNull DataObject root, @NotNull String path, int fallback)
    {
        final Integer integer = get(root, path, (obj, key) -> obj.getUnsignedInt(key, fallback), (arr, index) -> arr.getUnsignedInt(index, fallback));
        return integer == null ? fallback : integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseUnsignedInt(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned int value at the given path
     */
    public static int getUnsignedInt(@NotNull DataArray root, @NotNull String path)
    {
        final Integer integer = get(root, path, DataObject::getUnsignedInt, DataArray::getUnsignedInt);

        if (integer == null)
        {
            pathError(path, "unsigned int");
        }
        return integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Integer#parseUnsignedInt(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The unsigned integer, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned int value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static int getUnsignedInt(@NotNull DataArray root, @NotNull String path, int fallback)
    {
        final Integer integer = get(root, path, (obj, key) -> obj.getUnsignedInt(key, fallback), (arr, index) -> arr.getUnsignedInt(index, fallback));
        return integer == null ? fallback : integer;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseLong(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The long value at the given path.
     */
    public static long getLong(@NotNull DataObject root, @NotNull String path)
    {
        final Long longValue = get(root, path, DataObject::getLong, DataArray::getLong);

        if (longValue == null)
        {
            pathError(path, "long");
        }
        return longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseLong(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fallback The long, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The long value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static long getLong(@NotNull DataObject root, @NotNull String path, long fallback)
    {
        final Long longValue = get(root, path, (obj, key) -> obj.getLong(key, fallback), (arr, index) -> arr.getLong(index, fallback));
        return longValue == null ? fallback : longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseLong(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The long value at the given path.
     */
    public static long getLong(@NotNull DataArray root, @NotNull String path)
    {
        final Long longValue = get(root, path, DataObject::getLong, DataArray::getLong);

        if (longValue == null)
        {
            pathError(path, "long");
        }
        return longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseLong(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The long, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The long value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static long getLong(@NotNull DataArray root, @NotNull String path, long fallback)
    {
        final Long longValue = get(root, path, (obj, key) -> obj.getLong(key, fallback), (arr, index) -> arr.getLong(index, fallback));
        return longValue == null ? fallback : longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseUnsignedLong(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned long value at the given path.
     */
    public static long getUnsignedLong(@NotNull DataObject root, @NotNull String path)
    {
        final Long longValue = get(root, path, DataObject::getUnsignedLong, DataArray::getUnsignedLong);

        if (longValue == null)
        {
            throw pathError(path, "unsigned long");
        }
        return longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseUnsignedLong(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fallback The unsigned long, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned long value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static long getUnsignedLong(@NotNull DataObject root, @NotNull String path, long fallback)
    {
        final Long longValue = get(root, path, (obj, key) -> obj.getUnsignedLong(key, fallback), (arr, index) -> arr.getUnsignedLong(index, fallback));
        return longValue == null ? fallback : longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseUnsignedLong(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned long value at the given path.
     */
    public static long getUnsignedLong(@NotNull DataArray root, @NotNull String path)
    {
        final Long longValue = get(root, path, DataObject::getUnsignedLong, DataArray::getUnsignedLong);

        if (longValue == null)
        {
            throw pathError(path, "unsigned long");
        }
        return longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Long#parseUnsignedLong(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The unsigned long, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The unsigned long value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static long getUnsignedLong(@NotNull DataArray root, @NotNull String path, long fallback)
    {
        final Long longValue = get(root, path, (obj, key) -> obj.getUnsignedLong(key, fallback), (arr, index) -> arr.getUnsignedLong(index, fallback));
        return longValue == null ? fallback : longValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Double#parseDouble(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The double value at the given path.
     */
    public static double getDouble(@NotNull DataObject root, @NotNull String path)
    {
        final Double doubleValue = get(root, path, DataObject::getDouble, DataArray::getDouble);

        if (doubleValue == null)
        {
            pathError(path, "double");
        }
        return doubleValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     * <br>If the resulting value is a string, this will parse the string using {@link Double#parseDouble(String)}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fallback The double, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The double value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static double getDouble(@NotNull DataObject root, @NotNull String path, double fallback)
    {
        final Double doubleValue = get(root, path, (obj, key) -> obj.getDouble(key, fallback), (arr, index) -> arr.getDouble(index, fallback));
        return doubleValue == null ? fallback : doubleValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Double#parseDouble(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The double value at the given path.
     */
    public static double getDouble(@NotNull DataArray root, @NotNull String path)
    {
        final Double doubleValue = get(root, path, DataObject::getDouble, DataArray::getDouble);

        if (doubleValue == null)
        {
            pathError(path, "double");
        }
        return doubleValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     * <br>If the resulting value is a string, this will parse the string using {@link Double#parseDouble(String)}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The double, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The double value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    public static double getDouble(@NotNull DataArray root, @NotNull String path, double fallback)
    {
        final Double doubleValue = get(root, path, (obj, key) -> obj.getDouble(key, fallback), (arr, index) -> arr.getDouble(index, fallback));
        return doubleValue == null ? fallback : doubleValue;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The String value at the given path.
     */
    @NotNull
    public static String getString(@NotNull DataObject root, @NotNull String path)
    {
        final String string = get(root, path, DataObject::getString, DataArray::getString);

        if (string == null)
        {
            pathError(path, "String");
        }
        return string;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fallback The string, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The String value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    @Contract("_, _, !null -> !null")
    public static String getString(@NotNull DataObject root, @NotNull String path, @Nullable String fallback)
    {
        final String string = get(root, path, (obj, key) -> obj.getString(key, fallback), (arr, index) -> arr.getString(index, fallback));
        return string == null ? fallback : string;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The String value at the given path.
     */
    @NotNull
    public static String getString(@NotNull DataArray root, @NotNull String path)
    {
        final String string = get(root, path, DataObject::getString, DataArray::getString);

        if (string == null)
        {
            pathError(path, "String");
        }
        return string;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The string, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The String value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    @Contract("_, _, !null -> !null")
    public static String getString(@NotNull DataArray root, @NotNull String path, @Nullable String fallback)
    {
        final String string = get(root, path, (obj, key) -> obj.getString(key, fallback), (arr, index) -> arr.getString(index, fallback));
        return string == null ? fallback : string;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link OffsetDateTime} value at the given path.
     */
    @NotNull
    public static OffsetDateTime getOffsetDateTime(@NotNull DataObject root, @NotNull String path)
    {
        final OffsetDateTime offsetDateTime = get(root, path, DataObject::getOffsetDateTime, DataArray::getOffsetDateTime);

        if (offsetDateTime == null)
        {
            pathError(path, "OffsetDateTime");
        }
        return offsetDateTime;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     * @param fallback The {@link OffsetDateTime}, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link OffsetDateTime} value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    @Contract("_, _, !null -> !null")
    public static OffsetDateTime getOffsetDateTime(@NotNull DataObject root, @NotNull String path, @Nullable OffsetDateTime fallback)
    {
        final OffsetDateTime offsetDateTime = get(root, path, (obj, key) -> obj.getOffsetDateTime(key, fallback), (arr, index) -> arr.getOffsetDateTime(index, fallback));
        return offsetDateTime == null ? fallback : offsetDateTime;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link OffsetDateTime} value at the given path.
     */
    @NotNull
    public static OffsetDateTime getOffsetDateTime(@NotNull DataArray root, @NotNull String path)
    {
        final OffsetDateTime offsetDateTime = get(root, path, DataObject::getOffsetDateTime, DataArray::getOffsetDateTime);

        if (offsetDateTime == null)
        {
            pathError(path, "OffsetDateTime");
        }
        return offsetDateTime;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     * @param fallback The {@link OffsetDateTime}, which should be used for the case of a fallback.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link OffsetDateTime} value at the given path, returning the fallback if the path resolves to an optional value that is missing.
     */
    @Contract("_, _, !null -> !null")
    public static OffsetDateTime getOffsetDateTime(@NotNull DataArray root, @NotNull String path, @Nullable OffsetDateTime fallback)
    {
        final OffsetDateTime offsetDateTime = get(root, path, (obj, key) -> obj.getOffsetDateTime(key, fallback), (arr, index) -> arr.getOffsetDateTime(index, fallback));
        return offsetDateTime == null ? fallback : offsetDateTime;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The DataObject at the given path.
     */
    @NotNull
    public static DataObject getDataObject(@NotNull DataObject root, @NotNull String path)
    {
        final DataObject obj = optObject(root, path);

        if (obj == null)
        {
            pathError(path, "Object");
        }
        return obj;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The DataObject at the given path, or null if the path resolves to an optional value that is missing.
     */
    @Nullable
    @CheckReturnValue
    public static DataObject optObject(@NotNull DataObject root, @NotNull String path)
    {
        if (!path.endsWith("?"))
        {
            path += "?";
        }
        return get(root, path, DataObject::getDataObject, DataArray::getDataObject);
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The DataObject at the given path.
     */
    @NotNull
    public static DataObject getDataObject(@NotNull DataArray root, @NotNull String path)
    {
        final DataObject obj = optObject(root, path);

        if (obj == null)
        {
            pathError(path, "Object");
        }
        return obj;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The DataObject at the given path, or null if the path resolves to an optional value that is missing.
     */
    @Nullable
    @CheckReturnValue
    public static DataObject optObject(@NotNull DataArray root, @NotNull String path)
    {
        if (!path.endsWith("?"))
        {
            path += "?";
        }
        return get(root, path, DataObject::getDataObject, DataArray::getDataObject);
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *              <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link DataArray} at the given path.
     */
    @NotNull
    public static DataArray getDataArray(@NotNull DataObject root, @NotNull String path)
    {
        final DataArray array = optArray(root, path);

        if (array == null)
        {
            pathError(path, "Array");
        }
        return array;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataObject}.
     *
     * @param root The root data object, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with a name element, such as {@code "foo"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link DataArray} at the given path, or null if the path resolves to an optional value that is missing.
     */
    @Nullable
    public static DataArray optArray(@NotNull DataObject root, @NotNull String path)
    {
        if (!path.endsWith("?"))
        {
            path += "?";
        }
        return get(root, path, DataObject::getDataArray, DataArray::getDataArray);
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link DataArray} at the given path.
     */
    @NotNull
    public static DataArray getDataArray(@NotNull DataArray root, @NotNull String path)
    {
        final DataArray array = optArray(root, path);

        if (array == null)
        {
            pathError(path, "Array");
        }
        return array;
    }

    /**
     * Parses the given {@code path} and finds the appropriate value within this {@link DataArray}.
     *
     * @param root The root data array, which is the top level accessor.
     *             <br>The very first element in the path corresponds to a field of that name within this root object.
     * @param path The path of the value, in accordance with the described grammar by {@link DataPath}.
     *             <br>This must start with an index element, such as {@code "[0]"}.
     *
     * @throws ParsingException If the path is invalid or resolving fails due to missing elements.
     * @throws IndexOutOfBoundsException If any of the elements in the path refer to an array index that is out of bounds.
     * @throws IllegalArgumentException If null is provided or the path is empty.
     *
     * @return The {@link DataArray} at the given path, or null if the path resolves to an optional value that is missing.
     */
    @Nullable
    @CheckReturnValue
    public static DataArray optArray(@NotNull DataArray root, @NotNull String path)
    {
        if (!path.endsWith("?"))
        {
            path += "?";
        }
        return get(root, path, DataObject::getDataArray, DataArray::getDataArray);
    }

    @NotNull
    private static ParsingException pathError(@NotNull String path, @NotNull String type)
    {
        throw new ParsingException("Could not resolve value of type " + type + " at path \"" + path + "\"");
    }
}


