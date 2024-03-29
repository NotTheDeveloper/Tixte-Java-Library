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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.NotNull;

/**
 * Allows custom serialization for JSON payloads of an object.
 *
 * @author MinnDevelopment and BlockyDotJar
 * @version v1.0.3
 * @since v1.0.0-beta.3
 */
@JsonSerialize
@FunctionalInterface
public interface SerializableData
{
    /**
     * Serialized {@link DataObject} for this object.
     *
     * @return A {@link DataObject}.
     */
    @NotNull
    DataObject toData();
}
