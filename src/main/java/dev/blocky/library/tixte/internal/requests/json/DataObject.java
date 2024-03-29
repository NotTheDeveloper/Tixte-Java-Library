/**
 * Copyright 2022 Dominic R. (aka. BlockyDotJar), Florian Spieß (aka. MinnDevelopment),
 * Austin Keener (aka. DV8FromTheWorld), Austin Shapiro (aka. Scarsz), Dennis Neufeld (aka. napstr),
 * Andre_601 (aka. Andre601) and Mitmocc
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
import com.fasterxml.jackson.databind.type.MapType;
import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.exceptions.ParsingException;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.Helpers;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Represents a map of values used in communication with the Tixte API.
 * <br>Throws {@link NullPointerException}, if a parameter annotated with {@link NotNull} is provided with {@code null}.
 * <br>This class is not thread-safe.
 *
 * @param data A {@link List} of objects.
 *
 * @author MinnDevelopment, napstr, Andre601, Mitmocc and BlockyDotJar
 * @version v1.1.2
 * @since v1.0.0-beta.3
 */
public record DataObject(@NotNull Map<String, Object> data) implements SerializableData
{
    private static final Logger log = TixteLogger.getLog(DataObject.class);
    private static final ObjectMapper mapper;
    private static final SimpleModule module;
    private static final MapType mapType;

    static
    {
        mapper = new ObjectMapper();
        module = new SimpleModule();
        module.addAbstractTypeMapping(Map.class, HashMap.class);
        module.addAbstractTypeMapping(List.class, ArrayList.class);
        mapper.registerModule(module);
        mapType = mapper.getTypeFactory().constructRawMapType(HashMap.class);
    }

    /**
     * Creates a new empty {@link DataObject}, ready to be populated with values.
     *
     * @see #put(String, Object)
     *
     * @return An empty {@link DataObject} instance.
     */
    @NotNull
    public static DataObject empty()
    {
        return new DataObject(new HashMap<>());
    }

    /**
     * Parses a JSON payload into a {@link DataObject} instance
     *
     * @param data The correctly formatted JSON payload to parse.
     *
     * @throws ParsingException If the provided json is incorrectly formatted.
     *
     * @return A {@link DataObject} instance for the provided payload.
     */
    @NotNull
    public static DataObject fromJson(byte[] data)
    {
        try
        {
            final Map<String, Object> map = mapper.readValue(data, mapType);
            return new DataObject(map);
        }
        catch (IOException ex)
        {
            throw new ParsingException(ex);
        }
    }

    /**
     * Parses a JSON payload into a {@link DataObject} instance.
     *
     * @param json The correctly formatted JSON payload to parse.
     *
     * @throws ParsingException  If the provided json is incorrectly formatted.
     *
     * @return A {@link DataObject} instance for the provided payload.
     */
    @NotNull
    public static DataObject fromJson(@NotNull String json)
    {
        try
        {
            final Map<String, Object> map = mapper.readValue(json, mapType);
            return new DataObject(map);
        }
        catch (IOException ex)
        {
            throw new ParsingException(ex);
        }
    }

    /**
     * Parses a JSON payload into a {@link DataObject} instance.
     *
     * @param stream The correctly formatted JSON payload to parse.
     *
     * @throws ParsingException If the provided json is incorrectly formatted or an I/O error occurred.
     *
     * @return A {@link DataObject} instance for the provided payload.
     */
    @NotNull
    public static DataObject fromJson(@NotNull InputStream stream)
    {
        try
        {
            final Map<String, Object> map = mapper.readValue(stream, mapType);
            return new DataObject(map);
        }
        catch (IOException ex)
        {
            throw new ParsingException(ex);
        }
    }

    /**
     * Parses a JSON payload into a {@link DataObject} instance.
     *
     * @param stream The correctly formatted JSON payload to parse.
     *
     * @throws ParsingException If the provided json is incorrectly formatted or an I/O error occurred.
     *
     * @return A {@link DataObject} instance for the provided payload.
     */
    @NotNull
    public static DataObject fromJson(@NotNull Reader stream)
    {
        try
        {
            final Map<String, Object> map = mapper.readValue(stream, mapType);
            return new DataObject(map);
        }
        catch (IOException ex)
        {
            throw new ParsingException(ex);
        }
    }

    /**
     * Whether the specified key is present.
     *
     * @param key The key to check.
     *
     * @return <b>true</b> - If the specified key is present.
     *         <br><b>false</b> - If the specified key is not present.
     */
    public boolean hasKey(@NotNull String key)
    {
        return data.containsKey(key);
    }

    /**
     * Whether the specified key is missing or null.
     *
     * @param key The key to check.
     *
     * @return <b>true</b> - If the specified key is null or missing.
     *         <br><b>false</b> - If the specified key is not null or not missing.
     */
    public boolean isNull(@NotNull String key)
    {
        return data.get(key) == null;
    }

    /**
     * Whether the specified key is of the specified type.
     *
     * @param key The key to check.
     * @param type The type to check.
     *
     * @see DataType#isType(Object)
     *
     * @return <b>true</b> - If the type check is successful.
     *         <br><b>false</b> - If the type check fails.
     */
    public boolean isType(@NotNull String key, @NotNull DataType type)
    {
        return type.isType(data.get(key));
    }

    /**
     * Resolves a {@link DataObject} to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the type is incorrect or no value is present for the specified key.
     *
     * @return The resolved instance of {@link DataObject} for the key.
     */
    @NotNull
    public DataObject getDataObject(@NotNull String key)
    {
        return optObject(key).orElseThrow(() -> valueError(key, "DataObject"));
    }

    /**
     * Resolves a {@link DataObject} to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the type is incorrect.
     *
     * @return The resolved instance of {@link DataObject} for the key, wrapped in {@link Optional}.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public Optional<DataObject> optObject(@NotNull String key)
    {
        Map<String, Object> child = null;
        try
        {
            child = (Map<String, Object>) get(Map.class, key);
        }
        catch (ClassCastException ex)
        {
            log.error("Unable to extract child data", ex);
        }
        return child == null ? Optional.empty() : Optional.of(new DataObject(child));
    }

    /**
     * Resolves a {@link DataArray} to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException  If the type is incorrect or no value is present for the specified key.
     *
     * @return The resolved instance of {@link DataArray} for the key.
     */
    @NotNull
    public DataArray getDataArray(@NotNull String key)
    {
        return optArray(key).orElseThrow(() -> valueError(key, "DataArray"));
    }

    /**
     * Resolves a {@link DataArray} to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the type is incorrect.
     *
     * @return The resolved instance of {@link DataArray} for the key, wrapped in {@link Optional}.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public Optional<DataArray> optArray(@NotNull String key)
    {
        List<Object> child = null;
        try
        {
            child = (List<Object>) get(List.class, key);
        }
        catch (ClassCastException ex)
        {
            log.error("Unable to extract child data", ex);
        }
        return child == null ? Optional.empty() : Optional.of(new DataArray(child));
    }

    /**
     * Resolves any type to the provided key.
     *
     * @param key The key to check for a value.
     *
     * @return {@link Optional} with a possible value.
     */
    @NotNull
    public Optional<Object> opt(@NotNull String key)
    {
        return Optional.ofNullable(data.get(key));
    }

    /**
     * Resolves any type to the provided key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is missing or null.
     *
     * @see #opt(String)
     *
     * @return The value of any type.
     *
     */
    @NotNull
    @CheckReturnValue
    public Object get(@NotNull String key)
    {
        final Object value = data.get(key);

        if (value == null)
        {
            throw valueError(key, "any");
        }

        return value;
    }

    /**
     * Resolves a string to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is missing or null.
     *
     * @return The string value.
     */
    @NotNull
    public String getString(@NotNull String key)
    {
        final String value = getString(key, null);

        if (value == null)
        {
            throw valueError(key, "String");
        }

        return value;
    }

    /**
     * Resolves a string to a key.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @return The string value, or null if provided with null {@code defaultValue}.
     */
    @Contract("_, !null -> !null")
    public String getString(@NotNull String key, @Nullable String defaultValue)
    {
        final String value = get(String.class, key, UnaryOperator.identity(), String::valueOf);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves a boolean to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return <b>true</b> - If the value is present and set to true.
     *         <br><b>false</b> - If the value is missing or set to false.
     */
    public boolean getBoolean(@NotNull String key)
    {
        return getBoolean(key, false);
    }

    /**
     * Resolves a boolean to a key.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return <b>true</b> - If the value is present and set to true.
     *         <br><b>false</b> - If the value is set to false.
     *         <br><b>defaultValue</b> - If it is missing.
     */
    public boolean getBoolean(@NotNull String key, boolean defaultValue)
    {
        final Boolean value = get(Boolean.class, key, Boolean::parseBoolean, null);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves a long to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is missing, null, or of the wrong type.
     *
     * @return The long value for the key.
     */
    public long getLong(@NotNull String key)
    {
        final Long value = get(Long.class, key, Helpers::parseLong, Number::longValue);

        if (value == null)
        {
            throw valueError(key, "long");
        }

        return value;
    }

    /**
     * Resolves a long to a key.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The long value for the key.
     */
    public long getLong(@NotNull String key, long defaultValue)
    {
        final Long value = get(Long.class, key, Long::parseLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves an unsigned long to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is missing, null, or of the wrong type.
     *
     * @return The unsigned long value for the key.
     */
    public long getUnsignedLong(@NotNull String key)
    {
        final Long value = get(Long.class, key, Long::parseUnsignedLong, Number::longValue);

        if (value == null)
        {
            throw valueError(key, "unsigned long");
        }

        return value;
    }

    /**
     * Resolves an unsigned long to a key.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The unsigned long value for the key.
     */
    public long getUnsignedLong(@NotNull String key, long defaultValue)
    {
        final Long value = get(Long.class, key, Long::parseUnsignedLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves an int to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is missing, null, or of the wrong type.
     *
     * @return The int value for the key.
     */
    public int getInt(@NotNull String key)
    {
        final Integer value = get(Integer.class, key, Integer::parseInt, Number::intValue);

        if (value == null)
        {
            throw valueError(key, "int");
        }

        return value;
    }

    /**
     * Resolves an int to a key.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The int value for the key.
     */
    public int getInt(@NotNull String key, int defaultValue)
    {
        final Integer value = get(Integer.class, key, Integer::parseInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves an unsigned int to a key.
     *
     * @param  key The key to check for a value.
     *
     * @throws ParsingException If the value is missing, null, or of the wrong type.
     *
     * @return The unsigned int value for the key.
     */
    public int getUnsignedInt(@NotNull String key)
    {
        final Integer value = get(Integer.class, key, Integer::parseUnsignedInt, Number::intValue);

        if (value == null)
        {
            throw valueError(key, "unsigned int");
        }

        return value;
    }

    /**
     * Resolves an unsigned int to a key.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The unsigned int value for the key.
     */
    public int getUnsignedInt(@NotNull String key, int defaultValue)
    {
        final Integer value = get(Integer.class, key, Integer::parseUnsignedInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves a double to a key.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is missing, null, or of the wrong type.
     *
     * @return The double value for the key.
     */
    public double getDouble(@NotNull String key)
    {
        final Double value = get(Double.class, key, Double::parseDouble, Number::doubleValue);

        if (value == null)
        {
            throw valueError(key, "double");
        }

        return value;
    }

    /**
     * Resolves a double to a key.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @throws ParsingException If the value is of the wrong type.
     *
     * @return The double value for the key.
     */
    public double getDouble(@NotNull String key, double defaultValue)
    {
        final Double value = get(Double.class, key, Double::parseDouble, Number::doubleValue);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves an {@link OffsetDateTime} to a key.
     * <br><b>Note:</b> This method should be used on ISO8601 timestamps.
     *
     * @param key The key to check for a value.
     *
     * @throws ParsingException If the value is missing, null, or not a valid ISO8601 timestamp.
     *
     * @return Possibly-null {@link OffsetDateTime} object representing the timestamp.
     */
    @NotNull
    public OffsetDateTime getOffsetDateTime(@NotNull String key)
    {
        OffsetDateTime value = getOffsetDateTime(key, null);

        if(value == null)
        {
            throw valueError(key, "OffsetDateTime");
        }
        return value;
    }

    /**
     * Resolves an {@link OffsetDateTime} to a key.
     * <br><b>Note:</b> This method should only be used on ISO8601 timestamps.
     *
     * @param key The key to check for a value.
     * @param defaultValue Alternative value to use when no value or null value is associated with the key.
     *
     * @throws ParsingException If the value is not a valid ISO8601 timestamp.
     *
     * @return Possibly-null {@link OffsetDateTime} object representing the timestamp.
     */
    @Contract("_, !null -> !null")
    public OffsetDateTime getOffsetDateTime(@NotNull String key, @Nullable OffsetDateTime defaultValue)
    {
        OffsetDateTime value;

        try
        {
            value = get(OffsetDateTime.class, key, OffsetDateTime::parse, null);
        }
        catch (DateTimeParseException e)
        {
            String reason = "Cannot parse value for %s into an OffsetDateTime object. Try double checking that %s is a valid ISO8601 timestamp.";
            throw new ParsingException(String.format(reason, key, e.getParsedString()));
        }
        return value == null ? defaultValue : value;
    }



    /**
     * Removes the value associated with the specified key.
     * <br>If no value is associated with the key, this does nothing.
     *
     * @param key The key to unlink.
     *
     * @return A {@link DataObject} with the removed key.
     */
    @NotNull
    public DataObject remove(@NotNull String key)
    {
        data.remove(key);
        return this;
    }

    /**
     * Upserts a null value for the provided key.
     *
     * @param key The key to upsert.
     *
     * @return A {@link DataObject} with the updated value.
     */
    @NotNull
    public DataObject putNull(@NotNull String key)
    {
        data.put(key, null);
        return this;
    }

    /**
     * Upserts a new value for the provided key.
     *
     * @param key The key to upsert.
     * @param value The new value.
     *
     * @return A {@link DataObject} with the updated value.
     */
    @NotNull
    public DataObject put(@NotNull String key, @Nullable Object value)
    {
        if (value instanceof SerializableData)
        {
            data.put(key, ((SerializableData) value).toData().data);
        }
        else if (value instanceof SerializableArray)
        {
            data.put(key, ((SerializableArray) value).toDataArray().data());
        }
        else
        {
            data.put(key, value);
        }
        return this;
    }

    /**
     * {@link Collection} of all values in this {@link DataObject}.
     *
     * @return {@link Collection} for all values.
     */
    @NotNull
    public Collection<Object> values()
    {
        return data.values();
    }

    /**
     * {@link Set} of all keys in this {@link DataObject}.
     *
     * @return {@link Set} of keys.
     */
    @NotNull
    public Set<String> keys()
    {
        return data.keySet();
    }

    /**
     * Renames an existing field to the new name.
     * <br>This is a shorthand to {@link #remove(String) remove} under the old key and then {@link #put(String, Object) put} under the new key.
     *
     * <p>If there is nothing mapped to the old key, this does nothing.
     *
     * @param key The old key.
     * @param newKey The new key.
     *
     * @throws IllegalArgumentException If null is provided.
     *
     * @return A DataObject with the updated value.
     */
    @NotNull
    public DataObject rename(@NotNull String key, @NotNull String newKey)
    {
        Checks.notNull(key, "Key");
        Checks.notNull(newKey, "Key");

        if (!this.data.containsKey(key))
        {
            return this;
        }
        this.data.put(newKey, this.data.remove(key));
        return this;
    }

    /**
     * Serialize this object as JSON.
     *
     * @return Byte array containing the JSON representation of this object.
     */
    public byte[] toJson()
    {
        try
        {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mapper.writeValue(outputStream, data);
            return outputStream.toByteArray();
        }
        catch (IOException e)
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
        catch (JsonProcessingException e)
        {
            throw new ParsingException(e);
        }
    }

    /**
     * Converts this {@link DataObject} to a {@link Map}.
     *
     * @return The resulting {@link Map}.
     */
    @NotNull
    public Map<String, Object> toMap()
    {
        return data;
    }

    @NotNull
    private ParsingException valueError(@NotNull String key, @NotNull String expectedType)
    {
        return new ParsingException("Unable to resolve value with key " + key + " to type " + expectedType + ": " + data.get(key));
    }

    @Nullable
    @CheckReturnValue
    private <T> T get(@NotNull Class<T> type, @NotNull String key)
    {
        return get(type, key, null, null);
    }

    @Nullable
    @CheckReturnValue
    private <T> T get(@NotNull Class<T> type, @NotNull String key, @Nullable Function<String, T> stringParse, @Nullable Function<Number, T> numberParse)
    {
        final Object value = data.get(key);

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

        if (value instanceof Number && numberParse != null)
        {
            return numberParse.apply((Number) value);
        }
        else if (value instanceof String && stringParse != null)
        {
            return stringParse.apply((String) value);
        }

        throw new ParsingException(Helpers.format("Cannot parse value for %s into type %s: %s instance of %s",
                key, type.getSimpleName(), value, value.getClass().getSimpleName()));
    }

    @NotNull
    @Override
    public DataObject toData()
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
        catch (JsonProcessingException e)
        {
            throw new ParsingException(e);
        }
    }
}
