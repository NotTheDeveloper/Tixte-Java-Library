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
package dev.blocky.library.tixte.internal.utils.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * An {@link BiConsumer} for I/O operations.
 *
 * @param <T> The type of the first argument to the operation.
 * @param <R> The type of the second argument to the operation.
 *
 * @author BlockyDotJar
 * @version v1.0.1
 * @since v1.0.0-beta.5
 */
@FunctionalInterface
public interface IOBiConsumer<T, R>
{
    /**
     * Performs this operation on the given arguments.
     *
     * @param a The first input argument to the operation.
     * @param b The second input argument to the operation.
     *
     * @throws IOException If an I/O error occurs.
     */
    void accept(@NotNull T a, @NotNull R b) throws IOException;
}
