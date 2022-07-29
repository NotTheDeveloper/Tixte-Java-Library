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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.TixteClient;
import dev.blocky.library.tixte.api.utils.discord.TimeFormat;
import dev.blocky.library.tixte.api.utils.discord.Timestamp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Some basic examples, how to create a Discord timestamp.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.3
 */
public class DiscordTimestampExample
{
    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Converts the provided {@link Instant} into a {@link Timestamp} with this style.
     */
    @NotNull
    @CheckReturnValue
    public static Timestamp createDiscordTimestamp() throws IOException
    {
        TixteClient client = BasicTixteClientExample.getTixteClient();

        // Fully parses the text producing a temporal object.
        // This parses the entire text producing a temporal object.
        // The result of this method is TemporalAccessor which has been resolved, applying basic validation checks to help ensure a valid date-time.
        TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(client.getSelfUser().getLastLogin());
        Instant instant = Instant.from(temporalAccessor);

        // Converts the provided Instant into a Timestamp with this style.
        return TimeFormat.RELATIVE.atInstant(instant);
    }
}
