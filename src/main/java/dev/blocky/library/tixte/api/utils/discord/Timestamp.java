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
package dev.blocky.library.tixte.api.utils.discord;

import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

/**
 * Utility class representing Discord Markdown timestamps.
 * <br>This class implements {@link #toString()} such that it can be directly included in message content.
 * <br>These timestamps are rendered by the individual receiving Discord client in a local timezone and language format.
 * <br>Each timestamp can be displayed with different {@link TimeFormat TimeFormats}.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.3
 */
public class Timestamp
{
    private final TimeFormat format;
    private final long timestamp;

    protected Timestamp(@NotNull TimeFormat format, long timestamp)
    {
        Checks.notNull(format, "TimeFormat");
        this.format = format;
        this.timestamp = timestamp;
    }

    /**
     * The {@link TimeFormat} used to display this timestamp.
     *
     * @return The {@link TimeFormat}.
     */
    @NotNull
    public TimeFormat getFormat()
    {
        return format;
    }

    /**
     * The unix epoch timestamp for this markdown timestamp.
     * <br>This is similar to {@link System#currentTimeMillis()} and provided in millisecond precision for easier compatibility.
     * Discord uses seconds precision instead.
     *
     * @return The millisecond unix epoch timestamp.
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * Shortcut for {@code Instant.ofEpochMilli(getTimestamp())}.
     *
     * @return The {@link Instant} of this timestamp.
     */
    @NotNull
    public Instant toInstant()
    {
        return Instant.ofEpochMilli(timestamp);
    }

    /**
     * Creates a new timestamp instance with the provided offset into the future relative to the current timestamp.
     *
     * @param millis The millisecond offset for the new timestamp.
     *
     * @see #plus(Duration)
     *
     * @return Copy of this timestamp with the relative offset.
     */
    @NotNull
    public Timestamp plus(long millis)
    {
        return new Timestamp(format, timestamp + millis);
    }

    /**
     * Creates a new timestamp instance with the provided offset into the future relative to the current timestamp.
     *
     * @param duration The offset for the new timestamp.
     *
     * @throws IllegalArgumentException If the provided duration is null.
     *
     * @see #plus(long)
     *
     * @return Copy of this timestamp with the relative offset.
     */
    @NotNull
    public Timestamp plus(@NotNull Duration duration)
    {
        Checks.notNull(duration, "Duration");
        return plus(duration.toMillis());
    }

    /**
     * Creates a new timestamp instance with the provided offset into the past relative to the current timestamp.
     *
     * @param millis The millisecond offset for the new timestamp.
     *
     * @see #minus(Duration)
     *
     * @return Copy of this timestamp with the relative offset.
     */
    @NotNull
    public Timestamp minus(long millis)
    {
        return new Timestamp(format, timestamp - millis);
    }

    /**
     * Creates a new timestamp instance with the provided offset into the past relative to the current timestamp.
     *
     * @param duration The offset for the new timestamp.
     *
     * @throws IllegalArgumentException If the provided duration is null.
     *
     * @see #minus(long)
     *
     * @return Copy of this timestamp with the relative offset.
     */
    @NotNull
    public Timestamp minus(@NotNull Duration duration)
    {
        Checks.notNull(duration, "Duration");
        return minus(duration.toMillis());
    }

    @NotNull
    @Override
    public String toString()
    {
        return "<t:" + timestamp / 1000 + ":" + format.getStyle() + ">";
    }
}
