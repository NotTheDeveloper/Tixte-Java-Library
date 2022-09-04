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
package dev.blocky.library.tixte.api;

import dev.blocky.library.tixte.api.enums.CachePolicy;
import dev.blocky.library.tixte.internal.interceptor.CacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ErrorResponseInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ForceCacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.RateLimitInterceptor;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to create <b>new</b> {@link TixteClient} instances.
 *
 * <p>A single {@link TixteClientBuilder} can be reused multiple times.
 * <br>Each call to {@link #build()} creates a <b>new</b> {@link TixteClient} instance using the same information.
 *
 * @author BlockyDotJar
 * @version v1.2.0
 * @since v1.0.0-alpha.1
 */
public class TixteClientBuilder
{
    protected final Dispatcher dispatcher = new Dispatcher();
    protected static String apiKey, sessionToken, defaultDomain;
    protected static CachePolicy policy;
    protected static OkHttpClient client;
    protected static Request request;

    private final Logger logger = TixteLogger.getLog(TixteClientBuilder.class);

    /**
     * Creates a <b>new</b> {@link TixteClientBuilder} instance by initializing the builder with your API-key.
     * <br>There also will be set a policy which decides whether there should be created a cache or not.
     * <br>This will be called throughout Tixte4J when data gets constructed or modified and allows for a dynamically
     * adjusting cache.
     * <br>When {@link TixteClient#pruneCache() TixteClient#pruneCache()} is called, the
     * configured policy will be used to unload any data that the policy has decided not to cache.
     * <br>If null the cache-policy will be set to {@link CachePolicy#NONE NONE}.
     *
     * @param apiKey The API-key to use.
     * @param policy The cache-policy, which should be used.
     *
     * @return Instantiates a <b>new</b> Tixte-Client-Builder.
     */
    @NotNull
    public TixteClientBuilder create(@NotNull String apiKey, @Nullable CachePolicy policy)
    {
        Checks.notEmpty(apiKey, "apiKey");
        Checks.noWhitespace(apiKey, "apiKey");

        final Pattern pattern = Pattern.compile("^([a-z\\d]){8}-([a-z\\d]){4}-([a-z\\d]){4}-([a-z\\d]){4}-([a-z\\d]){12}$");
        final Matcher matcher = pattern.matcher(apiKey);

        if (matcher.find())
        {
            TixteClientBuilder.apiKey = apiKey;
        }
        else
        {
            throw new IllegalArgumentException("Regex doesn't match with your API-key. Please check if you specified the right key. (session-token != API-key)");
        }

        TixteClientBuilder.policy = policy;
        return this;
    }

    /**
     * Creates a <b>new</b> {@link TixteClientBuilder} instance by initializing the builder with your API-key.
     *
     * @param apiKey The API-key to use.
     *
     * @return Instantiates a <b>new</b> Tixte-Client-Builder.
     */
    @NotNull
    public TixteClientBuilder create(@NotNull String apiKey)
    {
        return create(apiKey, null);
    }

    /**
     * Creates a <b>new</b> {@link TixteClientBuilder} instance by initializing the builder with your session-token.
     *
     * @param sessionToken The session-token to use.
     *
     * @return Instantiates a <b>new</b> Tixte-Client-Builder.
     */
    @NotNull
    public TixteClientBuilder setSessionToken(@NotNull String sessionToken)
    {
        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

        final Pattern pattern = Pattern.compile("^tx.(mfa.)?([a-zA-Z\\d]){16}.([a-zA-Z\\d]){16}.([a-zA-Z\\d]){16}.([a-zA-Z\\d]){4}$");
        final Matcher matcher = pattern.matcher(sessionToken);

        if (matcher.find())
        {
            TixteClientBuilder.sessionToken = sessionToken;
        }
        else
        {
            throw new IllegalArgumentException("Regex doesn't match with your session-token. Please check if you specified the right token. (API-key != session-token)");
        }
        return this;
    }

    /**
     * Creates a <b>new</b> {@link TixteClientBuilder} instance by initializing the builder with your default domain.
     *
     * @param defaultDomain The default domain to use.
     *
     * @return Instantiates a <b>new</b> Tixte-Client-Builder.
     */
    @NotNull
    public TixteClientBuilder setDefaultDomain(@NotNull String defaultDomain)
    {
        Checks.notEmpty(defaultDomain, "defaultDomain");
        Checks.noWhitespace(defaultDomain, "defaultDomain");

        if (defaultDomain.startsWith("https://") || defaultDomain.startsWith("http://"))
        {
            throw new IllegalArgumentException("Don't use 'http(s)://' at the beginning of the domain!");
        }

        final Pattern pattern = Pattern.compile("^([a-zA-Z\\d_-])+.([a-zA-Z\\d_-])+.([a-zA-Z\\d])+$");
        final Matcher matcher = pattern.matcher(defaultDomain);

        if (matcher.find())
        {
            TixteClientBuilder.defaultDomain = defaultDomain;
        }
        else
        {
            throw new IllegalArgumentException("Regex doesn't match with your default-domain. Please check if you specified a valid domain.");
        }
        return this;
    }

    /**
     * Policy which decides whether there should be created a cache or not.
     * <br>This will be called throughout Tixte4J when data gets constructed or modified and allows for a dynamically
     * adjusting cache.
     * <br>When {@link TixteClient#pruneCache() TixteClient#pruneCache()} is called, the
     * configured policy will be used to unload any data that the policy has decided not to cache.
     * <br>If null the cache-policy will be set to {@link CachePolicy#NONE NONE}.
     *
     * @param policy The cache-policy, which should be used.
     *
     * @return Instantiates a <b>new</b> Tixte-Client-Builder.
     */
    @NotNull
    public TixteClientBuilder setCachePolicy(@Nullable CachePolicy policy)
    {
        TixteClientBuilder.policy = policy;
        return this;
    }

    /**
     * Builds a <b>new</b> {@link TixteClient} instance and uses the provided API-key and session-token to start the login process.
     * <br>In this method there will be set a rate-limit for max. 100 requests per host.
     * <br>Here also will be built a {@link OkHttpClient} instance, in which every interceptor will be set.
     * <br>You can also set the {@link CachePolicy} by calling {@link #setCachePolicy(CachePolicy)}, which will be used here.
     *
     * @return A {@link TixteClient} instance that has started the login process.
     */
    @NotNull
    @NonBlocking
    public TixteClient build()
    {
        dispatcher.setMaxRequestsPerHost(25);

        ConnectionPool connectionPool = new ConnectionPool(5, 10, TimeUnit.SECONDS);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .addInterceptor(new RateLimitInterceptor())
                .addInterceptor(new ErrorResponseInterceptor());

        if (policy == null)
        {
            policy = CachePolicy.NONE;
            logger.info("'policy' equals null, setting to 'NONE'.");
        }

        switch (policy)
        {
        case NONE:
            client = builder.build();
            break;
        case ONLY_FORCE_CACHE:
            client = builder
                    .addInterceptor(new ForceCacheInterceptor())
                    .build();
            break;
        case ONLY_NETWORK_CACHE:
            client = builder
                    .addNetworkInterceptor(new CacheInterceptor())
                    .build();
            break;
        case ALL:
            client = builder
                    .addInterceptor(new ForceCacheInterceptor())
                    .addNetworkInterceptor(new CacheInterceptor())
                    .build();
            break;
        }
        return new TixteClient();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(new TixteClientBuilder());
    }

    @NotNull
    @Override
    public String toString()
    {
        return "TixteClientBuilder{" +
                "API_KEY='" + apiKey + "', " +
                "SESSION_TOKEN='" + sessionToken + "', " +
                "DEFAULT_DOMAIN='" + defaultDomain + "', " +
                "policy=" + policy +
                '}';
    }
}
