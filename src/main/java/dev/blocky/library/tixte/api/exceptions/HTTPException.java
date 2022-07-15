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
package dev.blocky.library.tixte.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Exception used for handling HTTP errors.
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-alpha.1
 */
public class HTTPException extends RuntimeException
{

    /**
     * Constructs a {@link HTTPException} with the specified detail message.
     * <br>A detail message is a String that describes this particular exception.
     *
     * @param message The detail message.
     */
    public HTTPException(@NotNull String message)
    {
        super(message);
    }
}
