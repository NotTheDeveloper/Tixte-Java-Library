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
package dev.blocky.library.tixte.internal.requests.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Enum constants representing possible types for a {@link DataObject} value.
 *
 * @author MinnDevelopment and BlcckyDotJar
 * @version v1.0.3
 * @since v1.0.0-beta.3
 */
public enum DataType
{
    INT, FLOAT, STRING, OBJECT, ARRAY, BOOLEAN, NULL, UNKNOWN;

    /**
     * Assumes the type of the provided value through instance checks.
     *
     * @param value The value to test.
     *
     * @return The {@link DataType} constant or {@link #UNKNOWN}.
     */
    @NotNull
    public static DataType getType(@Nullable Object value)
    {
        for (DataType type : values())
        {
            if (type.isType(value))
            {
                return type;
            }
        }
        return UNKNOWN;
    }

    /**
     * Tests whether the type for the provided value is the one represented by this enum constant.
     *
     * @param value The value to check.
     *
     * @return <b>true</b> - If the value is of this type.
     *         <br><b>false</b> - If the value is not of this type.
     */
    public boolean isType(@Nullable Object value)
    {
        return switch (this)
                {
                    case INT ->
                            value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte;
                    case FLOAT -> value instanceof Double || value instanceof Float;
                    case STRING -> value instanceof String;
                    case BOOLEAN -> value instanceof Boolean;
                    case ARRAY -> value instanceof List;
                    case OBJECT -> value instanceof Map;
                    case NULL -> value == null;
                    default -> false;
                };
    }
}
