/**
 * Copyright 2022 Dominic R. (aka. BlockyDotJar), Florian Spieß (aka. MinnDevelopment),
 * Austin Keener (aka. DV8FromTheWorld), Austin Shapiro (aka. Scarsz) and Dennis Neufeld (aka. napstr)
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.blocky.library.logging.FallbackLogger;
import dev.blocky.library.tixte.api.exceptions.ParsingException;
import dev.blocky.library.tixte.internal.utils.Helpers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a list of values used in communication with the Tixte API.
 * <br>Throws {@link IndexOutOfBoundsException} if provided with index out of bounds.
 * <br>This class is not thread-safe.
 *
 * @param data A {@link List} of objects.
 *
 * @author MinnDevelopment, napstr and BlockyDotJar
 * @version v1.1.1
 * @since v1.0.0-beta.3
 */
public record DataArray(@NotNull List<Object> data) implements Iterable<Object>, SerializableArray
{
    private static final Logger log = FallbackLogger.getLog(DataArray.class);
    private static final ObjectMapper mapper;
    private static final SimpleModule module;
    private static final CollectionType listType;

    static
    {
        mapper = new ObjectMapper();
        module = new SimpleModule();
        module.addAbstractTypeMapping(Map.class, HashMap.class);
        module.addAbstractTypeMapping(List.class, ArrayList.class);
        mapper.registerModule(module);
        listType = mapper.getTypeFactory().constructRawCollectionType(ArrayList.class);
    }

    /**
     * Creates a new empty {@link DataArray}, ready to be populated with values.
     *
     * @see #add(Object)
     *
     * @return An empty {@link DataArray} instance.
     */
    @NotNull
    public static DataArray empty()
    {
        return new DataArray(new ArrayList<>());
    }

    /**
     * Creates a new {@link DataArray} and populates it with the contents of the provided collection.
     *
     * @param col The {@link Collection}.
     *
     * @return A new {@link DataArray} populated with the contents of the collection.
     */
    @NotNull
    public static DataArray fromCollection(@NotNull Collection<?> col)
    {
        return empty().addAll(col);
    }

    /**
     * Parses a JSON array into a {@link DataArray} instance.
     *
     * @param json The correctly formatted JSON array.
     *
     * @throws ParsingException If the provided JSON is incorrectly formatted.
     *
     * @return A new {@link DataArray} instance for the provided array.
     */
    @NotNull
    public static DataArray fromJson(@NotNull String json)
    {
        try
        {
            return new DataArray(mapper.readValue(json, listType));
        }
        catch (@NotNull IOException e)
        {
            throw new ParsingException(e);
        }
    }

    /**
     * Parses a JSON array into a {@link DataArray} instance.
     *
     * @param json The correctly formatted JSON array.
     *
     * @throws ParsingException If the provided JSON is incorrectly formatted or an I/O error occurred.
     *
     * @return A new {@link DataArray} instance for the provided array.
     */
    @NotNull
    public static DataArray fromJson(@NotNull InputStream json)
    {
        try
        {
            return new DataArray(mapper.readValue(json, listType));
        }
        catch (@NotNull IOException e)
        {
            throw new ParsingException(e);
        }
    }

    /**
     * Parses a JSON array into a {@link DataArray} instance.
     *
     * @param json The correctly formatted JSON array.
     *
     * @throws ParsingException f the provided JSON is incorrectly formatted or an I/O error occurred.
     *
     * @return A new {@link DataArray} instance for the provided array.
     */
    @NotNull
    public static DataArray fromJson(@NotNull Reader json)
    {
        try
        {
            return new DataArray(mapper.readValue(json, listType));
        }
        catch (@NotNull IOException e)
        {
            throw new ParsingException(e);
        }
    }

    /**
     * Whether the value at the specified index is null.
     *
     * @param index The index to check.
     *
     * @return <b>true</b> - If the value at the index is null.
     *         <br><b>false</b> - If the value at the index is not null.
     */
    public boolean isNull(int index)
    {
        return data.get(index) == null;
    }

    /**
     * Whether the value at the specified index is of the specified type.
     *
     * @param index The index to check.
     * @param type The type to check.
     *
     * @see DataType#isType(Object)
     *
     * @return <b>true</b> - If the type check is successful.
     *         <br><b>false</b> - If the type check fails.
     */
    public boolean isType(int index, @NotNull DataType type)
    {
        return type.isType(data.get(index));
    }

    /**
     * The length of the array.
     *
     * @return The length of the array.
     */
    public int length()
    {
        return data.size();
    }

    /**
     * Whether this array is empty.
     *
     * @return <b>true</b> - If this array is empty.
     *         <br><b>false</b> - If this array is not empty.
     */
    public boolean isEmpty()
    {
        return data.isEmpty();
    }

    /**
     * Resolves the value at the specified index to a {@link DataObject}.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type or missing.
     *
     * @return The resolved {@link DataObject}.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public DataObject getDataObject(int index)
    {
        Map<String, Object> child = null;
        try
        {
            child = (Map<String, Object>) get(Map.class, index);
        }
        catch (@NotNull ClassCastException ex)
        {
            log.error("Unable to extract child data", ex);
        }

        if (child == null)
        {
            throw valueError(index, "DataObject");
        }
        return new DataObject(child);
    }

    /**
     * Resolves the value at the specified index to a {@link DataArray}.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type or null.
     *
     * @return The resolved {@link DataArray}.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public DataArray getDataArray(int index)
    {
        List<Object> child = null;

        try
        {
            child = (List<Object>) get(List.class, index);
        }
        catch (@NotNull ClassCastException ex)
        {
            log.error("Unable to extract child data", ex);
        }

        if (child == null)
        {
            throw valueError(index, "DataArray");
        }
        return new DataArray(child);
    }

    /**
     * Resolves the value at the specified index to a string.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type or null.
     *
     * @return The resolved string.
     */
    @NotNull
    public String getString(int index)
    {
        final String value = get(String.class, index, UnaryOperator.identity(), String::valueOf);

        if (value == null)
        {
            throw valueError(index, "String");
        }
        return value;
    }

    /**
     * Resolves the value at the specified index to a string.
     *
     * @param index The index to resolve.
     * @param defaultValue Alternative value to use when the value associated with the index is null.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved string.
     */
    @Contract("_, !null -> !null")
    public String getString(int index, @Nullable String defaultValue)
    {
        final String value = get(String.class, index, UnaryOperator.identity(), String::valueOf);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves the value at the specified index to a boolean.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return <b>true</b> - If the value is present and set to true.
     *         <br><b>false</b> - If the value is set to false.
     */
    public boolean getBoolean(int index)
    {
        return getBoolean(index, false);
    }

    /**
     * Resolves the value at the specified index to a boolean.
     *
     * @param index The index to resolve.
     * @param defaultValue Alternative value to use when the value associated with the index is null.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return <b>true</b> - If the value is present and set to true.
     *         <br><b>false</b> - If the value is set to false.
     *         <br><b>defaultValue</b> - If it is missing.
     */
    public boolean getBoolean(int index, boolean defaultValue)
    {
        final Boolean value = get(Boolean.class, index, Boolean::parseBoolean, null);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves the value at the specified index to an int.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved int value.
     */
    public int getInt(int index)
    {
        final Integer value = get(Integer.class, index, Integer::parseInt, Number::intValue);

        if (value == null)
        {
            throw valueError(index, "int");
        }
        return value;
    }

    /**
     * Resolves the value at the specified index to an int.
     *
     * @param index The index to resolve.
     * @param defaultValue Alternative value to use when the value associated with the index is null.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved int value.
     */
    public int getInt(int index, int defaultValue)
    {
        final Integer value = get(Integer.class, index, Integer::parseInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves the value at the specified index to an unsigned int.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved unsigned int value.
     */
    public int getUnsignedInt(int index)
    {
        final Integer value = get(Integer.class, index, Integer::parseUnsignedInt, Number::intValue);

        if (value == null)
        {
            throw valueError(index, "unsigned int");
        }
        return value;
    }

    /**
     * Resolves the value at the specified index to an unsigned int.
     *
     * @param index  The index to resolve.
     * @param defaultValue Alternative value to use when the value associated with the index is null.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved unsigned int value.
     */
    public int getUnsignedInt(int index, int defaultValue)
    {
        final Integer value = get(Integer.class, index, Integer::parseUnsignedInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves the value at the specified index to a long.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved long value.
     */
    public long getLong(int index)
    {
        final Long value = get(Long.class, index, Long::parseLong, Number::longValue);

        if (value == null)
        {
            throw valueError(index, "long");
        }
        return value;
    }

    /**
     * Resolves the value at the specified index to a long.
     *
     * @param index The index to resolve.
     * @param defaultValue Alternative value to use when the value associated with the index is null.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved long value.
     */
    public long getLong(int index, long defaultValue)
    {
        final Long value = get(Long.class, index, Long::parseLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves the value at the specified index to an unsigned long.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved unsigned long value.
     */
    public long getUnsignedLong(int index)
    {
        final Long value = get(Long.class, index, Long::parseUnsignedLong, Number::longValue);

        if (value == null)
        {
            throw valueError(index, "unsigned long");
        }
        return value;
    }

    /**
     * Resolves the value at the specified index to an unsigned long.
     *
     * @param index The index to resolve.
     * @param defaultValue Alternative value to use when the value associated with the index is null.
     *
     * @throws ParsingException  If the value is of the wrong type.
     *
     * @return The resolved unsigned long value.
     */
    public long getUnsignedLong(int index, long defaultValue)
    {
        final Long value = get(Long.class, index, Long::parseUnsignedLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves the value at the specified index to a double.
     *
     * @param index The index to resolve.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved double value.
     */
    public double getDouble(int index)
    {
        final Double value = get(Double.class, index, Double::parseDouble, Number::doubleValue);

        if (value == null)
        {
            throw valueError(index, "double");
        }
        return value;
    }

    /**
     * Resolves the value at the specified index to a double.
     *
     * @param index The index to resolve.
     * @param defaultValue Alternative value to use when the value associated with the index is null.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The resolved double value.
     */
    public double getDouble(int index, double defaultValue)
    {
        final Double value = get(Double.class, index, Double::parseDouble, Number::doubleValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Appends the provided value to the end of the array.
     *
     * @param value The value to append.
     *
     * @return A {@link DataArray} with the value inserted at the end.
     */
    @NotNull
    @CanIgnoreReturnValue
    public DataArray add(@Nullable Object value)
    {
        if (value instanceof SerializableData)
        {
            data.add(((SerializableData) value).toData().data());
        }
        else if (value instanceof SerializableArray)
        {
            data.add(((SerializableArray) value).toDataArray().data);
        }
        else
        {
            data.add(value);
        }
        return this;
    }

    /**
     * Appends the provided values to the end of the array.
     *
     * @param values The values to append.
     *
     * @return A {@link DataArray} with the values inserted at the end.
     */
    @NotNull
    public DataArray addAll(@NotNull Collection<?> values)
    {
        values.forEach(this::add);
        return this;
    }

    /**
     * Appends the provided values to the end of the array.
     *
     * @param array The values to append.
     *
     * @return A {@link DataArray} with the values inserted at the end.
     */
    @NotNull
    public DataArray addAll(@NotNull DataArray array)
    {
        return addAll(array.data);
    }

    /**
     * Inserts the specified value at the provided index.
     *
     * @param index The target index.
     * @param value The value to insert.
     *
     * @return A {@link DataArray} with the value inserted at the specified index.
     */
    @NotNull
    public DataArray insert(int index, @Nullable Object value)
    {
        if (value instanceof SerializableData)
        {
            data.add(index, ((SerializableData) value).toData().data());
        }
        else if (value instanceof SerializableArray)
        {
            data.add(index, ((SerializableArray) value).toDataArray().data);
        }
        else
        {
            data.add(index, value);
        }
        return this;
    }

    /**
     * Removes the value at the specified index.
     *
     * @param index The target index to remove.
     *
     * @return A {@link DataArray} with the value removed.
     */
    @NotNull
    public DataArray remove(int index)
    {
        data.remove(index);
        return this;
    }

    /**
     * Removes the specified value.
     *
     * @param value The value to remove.
     *
     * @return A {@link DataArray} with the value removed.
     */
    @NotNull
    public DataArray remove(@Nullable Object value)
    {
        data.remove(value);
        return this;
    }

    /**
     * Serializes this object as JSON.
     *
     * @return Byte array containing the JSON representation of this object.
     */
    public byte[] toJson()
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mapper.writeValue(outputStream, data);
            return outputStream.toByteArray();
        }
        catch (@NotNull IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Method that can be used to convert the current object as a string.
     *
     * @return The current object as a pretty string.
     */
    @NotNull
    public String toPrettyString()
    {
        final DefaultPrettyPrinter.Indenter indent = new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
        final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.withObjectIndenter(indent).withArrayIndenter(indent);

        try
        {
            return mapper.writer(printer).writeValueAsString(data);
        }
        catch (@NotNull JsonProcessingException e)
        {
            throw new ParsingException(e);
        }
    }

    /**
     * Converts this DataArray to a {@link List}.
     *
     * @return The resulting {@link List}.
     */
    @NotNull
    public List<Object> toList()
    {
        return data;
    }

    @NotNull
    private ParsingException valueError(int index, @NotNull String expectedType)
    {
        return new ParsingException("Unable to resolve value at " + index + " to type " + expectedType + ": " + data.get(index));
    }

    @Nullable
    private <T> T get(@NotNull Class<T> type, int index)
    {
        return get(type, index, null, null);
    }

    @Nullable
    private <T> T get(@NotNull Class<T> type, int index, @Nullable Function<String, T> stringMapper, @Nullable Function<Number, T> numberMapper)
    {
        final Object value = data.get(index);

        if (value == null)
        {
            return null;
        }

        if (type.isInstance(value))
        {
            return type.cast(value);
        }

        if (type == String.class)
        {
            return type.cast(value.toString());
        }

        if (stringMapper != null && value instanceof String)
        {
            return stringMapper.apply((String) value);
        }
        else if (numberMapper != null && value instanceof Number)
        {
            return numberMapper.apply((Number) value);
        }

        throw new ParsingException(Helpers.format("Cannot parse value for index %d into type %s: %s instance of %s",
                index, type.getSimpleName(), value, value.getClass().getSimpleName()));
    }

    @NotNull
    public <T> Stream<T> stream(@NotNull BiFunction<? super DataArray, Integer, ? extends T> mapper)
    {
        return IntStream.range(0, length())
                .mapToObj(index -> mapper.apply(this, index));
    }

    @NotNull
    @Override
    public Iterator<Object> iterator()
    {
        return data.iterator();
    }

    @NotNull
    @Override
    public DataArray toDataArray()
    {
        return this;
    }

    @NotNull
    @Override
    public String toString()
    {
        try
        {
            return mapper.writeValueAsString(data);
        }
        catch (@NotNull JsonProcessingException e)
        {
            throw new ParsingException(e);
        }
    }
}
