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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.entities.SelfUser;
import dev.blocky.library.tixte.api.TixteClient;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Some basic examples, how to format an ISO string to a {@link Date}.
 *
 * @author BlockyDotJar
 * @version v1.1.1
 * @since v1.0.0-beta.3
 */
public class DateFormatExample
{
    /**
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A valid datetime.
     */
    @NotNull
    @CheckReturnValue
    public static Date formatDateTime() throws ExecutionException, InterruptedException
    {
        TixteClient client = BasicTixteClientExample.getTixteClient();
        SelfUser self = client.getSelfUser();

        // Fully parses the text producing a temporal object.
        // This parses the entire text producing a temporal object.
        // The result of this method is TemporalAccessor which has been resolved, applying basic validation checks to help ensure a valid date-time.
        TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(self.getLastLogin());
        Instant instant = Instant.from(temporalAccessor);

        // Returns a valid datetime
        return Date.from(instant);
    }
}
