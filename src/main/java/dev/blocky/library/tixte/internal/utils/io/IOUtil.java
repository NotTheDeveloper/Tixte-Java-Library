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
package dev.blocky.library.tixte.internal.utils.io;

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

/**
 * Utility class for I/O operations.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.5
 */
public class IOUtil
{
    private static final Logger logger = TixteLogger.getLog(IOUtil.class);

    IOUtil() { }

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

    /**
     * Provided as a simple way to fully read an {@link InputStream} into a {@code byte[]}.
     * <br>This method will block until the {@link InputStream} has been fully read, so if you provide an
     * {@link InputStream} that is non-finite, you're going to have a bad time.
     *
     * @param stream The Stream to be read.
     *
     * @throws IOException If the first byte cannot be read for any reason other than the end of the file,
     *                     if the input stream has been closed, or if some other I/O error occurs.
     *
     * @return A {@code byte[]} containing all the data provided by the {@link InputStream}.
     */
    public static byte[] readFully(@NotNull InputStream stream) throws IOException
    {
        Checks.notNull(stream, "InputStream");

        byte[] buffer = new byte[1024];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream())
        {
            int readAmount;
            while ((readAmount = stream.read(buffer)) != -1)
            {
                bos.write(buffer, 0, readAmount);
            }
            return bos.toByteArray();
        }
    }

    /**
     * Retrieves an {@link InputStream InputStream} for the provided {@link Response Response}.
     * <br>When the header for {@code Content-Encoding} is set with {@code gzip} this will wrap the body
     * in a {@link GZIPInputStream GZIPInputStream} which decodes the data.
     * <br>This is used to make usage of encoded responses more user-friendly in various parts of Tixte4J.
     *
     * @param response The not-null {@link Response} object.
     *
     * @return {@link InputStream} representing the body of this response.
     */
    @NotNull
    @CheckReturnValue
    @SuppressWarnings("ConstantConditions")
    public static InputStream getBody(@NotNull Response response) throws IOException
    {
        String encoding = response.header("Content-Encoding", "");
        InputStream data = new BufferedInputStream(response.body().byteStream());
        data.mark(256);
        try
        {
            if (encoding.equalsIgnoreCase("gzip"))
            {
                return new GZIPInputStream(data);
            }
            else if (encoding.equalsIgnoreCase("deflate"))
            {
                return new InflaterInputStream(data, new Inflater(true));
            }
        }
        catch (ZipException | EOFException ex)
        {
            data.reset();
            logger.error("Failed to read gzip content for response. Headers: {}\nContent: '{}'",
                    response.headers(), TixteLogger.getLazyString(() -> new String(readFully(data))), ex);
            return null;
        }
        return data;
    }
}
