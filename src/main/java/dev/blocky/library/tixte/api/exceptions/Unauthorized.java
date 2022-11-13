/**
 * Copyright 2022 Dominic R. (aka. BlockyDotJar)
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
package dev.blocky.library.tixte.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown when a request is made to a Tixte API-endpoint, but you are not authorized to do so.
 *
 * @author BlockyDotJar
 * @version v1.1.2
 * @since v1.0.0-alpha.1
 */
public class Unauthorized extends HTTPException
{

    /**
     * Constructs an {@link HTTPException HTTPException} for the response code {@code 401} with the specified detail message.
     * <br>A detail message is a String that describes this particular exception.
     *
     * @param message The detail message.
     */
    public Unauthorized(@NotNull String message)
    {
        super(message);
    }
}
