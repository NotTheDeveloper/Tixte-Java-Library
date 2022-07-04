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
package dev.blocky.library.tixte.api;

import dev.blocky.library.tixte.annotations.Undocumented;
import dev.blocky.library.tixte.api.enums.UserCachePolicy;
import dev.blocky.library.tixte.internal.interceptor.CacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ErrorResponseInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ForceCacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.RateLimitInterceptor;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * Used to create new {@link TixteClient} instances.
 *
 * @author BlockyDotJar
 * @version v1.0.0-alpha.3
 * @since v1.0.0-alpha.1
 */
public class TixteClientBuilder
{
    private final transient Logger logger = TixteLogger.getLog(TixteClient.class);
    private final transient Dispatcher dispatcher = new Dispatcher();
    static volatile String sessionToken, defaultDomain;
    static volatile OkHttpClient client;
    static volatile Request request;
    static volatile String apiKey;

    @Undocumented
    TixteClientBuilder(@NotNull String apiKey, @Nullable String sessionToken, @Nullable String defaultDomain)
    {
        TixteClientBuilder.apiKey = apiKey;
        TixteClientBuilder.sessionToken = sessionToken;
        TixteClientBuilder.defaultDomain = defaultDomain;

        Checks.notEmpty(apiKey, "apiKey");
        Checks.noWhitespace(apiKey, "apiKey");

        if (sessionToken.isEmpty() || sessionToken == null)
        {
            logger.warn("\"sessionToken\" is undefined.");
        }

        Checks.noWhitespace(sessionToken, "sessionToken");

        if (defaultDomain.isEmpty() || defaultDomain == null)
        {
            logger.warn("\"defaultDomain\" is undefined.");
        }

        Checks.noWhitespace(defaultDomain, "defaultDomain");
    }

    @Undocumented
    TixteClientBuilder(@NotNull String apiKey, @Nullable String sessionToken)
    {
        TixteClientBuilder.apiKey = apiKey;
        TixteClientBuilder.sessionToken = sessionToken;

        Checks.notEmpty(apiKey, "apiKey");
        Checks.noWhitespace(apiKey, "apiKey");

        if (sessionToken.isEmpty() || sessionToken == null)
        {
            logger.warn("\"sessionToken\" is undefined.");
        }

        Checks.noWhitespace(sessionToken, "sessionToken");
    }

    @Undocumented
    TixteClientBuilder(@NotNull String apiKey)
    {
        TixteClientBuilder.apiKey = apiKey;

        Checks.notEmpty(apiKey, "apiKey");
        Checks.noWhitespace(apiKey, "apiKey");
    }

    @NotNull
    @Undocumented
    public TixteClient build(@Nullable UserCachePolicy policy)
    {
        dispatcher.setMaxRequestsPerHost(100);

        Checks.notNull(policy, "policy");

        switch (policy)
        {
            case DEFAULT:
                client = new OkHttpClient.Builder()
                        .addInterceptor(new RateLimitInterceptor())
                        .addInterceptor(new ErrorResponseInterceptor())
                        .dispatcher(dispatcher)
                        .build();
                break;
            case ONLY_FORCE:
                client = new OkHttpClient.Builder()
                        .addInterceptor(new RateLimitInterceptor())
                        .addInterceptor(new ErrorResponseInterceptor())
                        .addInterceptor(new ForceCacheInterceptor())
                        .dispatcher(dispatcher)
                        .build();
                break;
            case ONLY_NETWORK:
                client = new OkHttpClient.Builder()
                        .addInterceptor(new RateLimitInterceptor())
                        .addInterceptor(new ErrorResponseInterceptor())
                        .addNetworkInterceptor(new CacheInterceptor())
                        .dispatcher(dispatcher)
                        .build();
                break;
            case ALL:
                client = new OkHttpClient.Builder()
                        .addInterceptor(new RateLimitInterceptor())
                        .addInterceptor(new ErrorResponseInterceptor())
                        .addInterceptor(new ForceCacheInterceptor())
                        .addNetworkInterceptor(new CacheInterceptor())
                        .dispatcher(dispatcher)
                        .build();
                break;
        }
        return new TixteClient();
    }
    @NotNull
    @Undocumented
    public static TixteClientBuilder createClient(@NotNull String apiKey, @Nullable String sessionKey, @Nullable String defaultDomain)
    {
        return new TixteClientBuilder(apiKey, sessionKey, defaultDomain);
    }

    @NotNull
    @Undocumented
    public static TixteClientBuilder createClient(@NotNull String apiKey, @Nullable String sessionKey)
    {
        return new TixteClientBuilder(apiKey, sessionKey);
    }

    @NotNull
    @Undocumented
    public static TixteClientBuilder createClient(@NotNull String apiKey)
    {
        return new TixteClientBuilder(apiKey);
    }

    @NotNull
    @Undocumented
    public TixteClientBuilder setSessionToken(@NotNull String sessionToken)
    {
        TixteClientBuilder.sessionToken = sessionToken;
        return this;
    }

    @NotNull
    @Undocumented
    public TixteClientBuilder setDefaultDomain(@NotNull String defaultDomain)
    {
        TixteClientBuilder.defaultDomain = defaultDomain;
        return this;
    }

    @Override
    public boolean equals(@Nullable Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        TixteClientBuilder that = (TixteClientBuilder) o;

        return dispatcher.equals(that.dispatcher);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(dispatcher);
    }

    @Override
    public String toString()
    {
        return "TixteClientBuilder{" +
                "dispatcher=" + dispatcher +
                '}';
    }
}
