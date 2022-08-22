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
package dev.blocky.library.tixte.internal.requests;

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.internal.utils.io.IOBiConsumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Utility class for creating async {@link Request requests}.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.5
 */
@Internal
public class FunctionalCallback implements Callback
{
    private final BiConsumer<Call, IOException> failure;
    private final IOBiConsumer<Call, Response> success;

    /**
     * Constructs a <b>new</b> {@link  FunctionalCallback}.
     *
     * @param failure The callback to be called when the request fails.
     * @param success The callback to be called when the request is successful.
     */
    public FunctionalCallback(@NotNull BiConsumer<Call, IOException> failure, @NotNull IOBiConsumer<Call, Response> success)
    {
        this.failure = failure;
        this.success = success;
    }

    /**
     * If set, the callback will be called when the request is successful.
     *
     * @param callback The callback to be called when the request is successful.
     *
     * @return The current instance of this {@link Builder}.
     */
    @NotNull
    @CheckReturnValue
    public static Builder onSuccess(@NotNull IOBiConsumer<Call, Response> callback)
    {
        return new Builder().onSuccess(callback);
    }

    /**
     * If set, the callback will be called when the request fails.
     *
     * @param callback The callback to be called when the request fails.
     *
     * @return The current instance of this {@link Builder}.
     */
    @NotNull
    @CheckReturnValue
    public static Builder onFailure(@NotNull BiConsumer<Call, IOException> callback)
    {
        return new Builder().onFailure(callback);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e)
    {
        if (failure != null)
            failure.accept(call, e);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
    {
        if (success != null)
        {
            success.accept(call, response);
        }
    }

    /**
     * Builder for {@link FunctionalCallback}.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-beta.5
     */
    public static class Builder
    {
        private BiConsumer<Call, IOException> failure;
        private IOBiConsumer<Call, Response> success;

        /**
         * If set, the callback will be called when the request is successful.
         *
         * @param callback The callback to be called when the request is successful.
         *
         * @return The current instance of this {@link Builder}.
         */
        @NotNull
        @CheckReturnValue
        public Builder onSuccess(@NotNull IOBiConsumer<Call, Response> callback)
        {
            this.success = callback;
            return this;
        }

        /**
         * If set, the callback will be called when the request fails.
         *
         * @param callback The callback to be called when the request fails.
         *
         * @return The current instance of this {@link Builder}.
         */
        @NotNull
        @CheckReturnValue
        public Builder onFailure(@NotNull BiConsumer<Call, IOException> callback)
        {
            this.failure = callback;
            return this;
        }

        /**
         * Builds a new {@link FunctionalCallback}.
         *
         * @return A <b>new</b> {@link FunctionalCallback}.
         */
        @NotNull
        @CheckReturnValue
        public FunctionalCallback build()
        {
            return new FunctionalCallback(failure, success);
        }
    }
}
