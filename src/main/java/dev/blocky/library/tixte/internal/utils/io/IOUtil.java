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

import java.io.Closeable;
import java.io.IOException;

/**
 * Utility class for I/O operations.
 *
 * @author MinnDevelopment and BlockyDotJar
 * @version v1.1.1
 * @since v1.0.0-beta.5
 */
public class IOUtil
{
    IOUtil()
    {
    }

    /**
     * Closes this stream and releases any system resources associated with it.
     * <br>If the stream is already closed then invoking this method has no effect.
     *
     * @param closeable The closeable to close.
     */
    public static void silentClose(@NotNull Closeable closeable)
    {
        try
        {
            closeable.close();
        }
        catch (IOException ignored)
        {
            // Ignored.
        }
    }
}
