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
 * Exception used if the provided json is incorrectly formatted/an I/O error occurred/the type is incorrect/no value is
 * present for the specified key/the value is missing or null.
 *
 * @author BlockyDotJar
 * @version v1.0.1
 * @since v1.0.0-beta.3
 */
public class ParsingException extends IllegalStateException
{

    /**
     * Constructs a {@link ParsingException} with the specified detail message.
     * <br>A detail message is a String that describes this particular exception.
     *
     * @param message The detail message.
     */
    public ParsingException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Constructs a {@link ParsingException} with the specified cause and a detail message
     * of {@code (cause==null ? null : cause.toString())}.
     * <br>A detail message is a String that describes this particular exception.
     *
     * @param cause The cause.
     */
    public ParsingException(@NotNull Throwable cause)
    {
        super(cause);
    }
}
