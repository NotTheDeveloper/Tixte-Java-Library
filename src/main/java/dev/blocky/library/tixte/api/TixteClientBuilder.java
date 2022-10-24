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

import dev.blocky.library.logging.FallbackLogger;
import dev.blocky.library.tixte.api.enums.CachePolicy;
import dev.blocky.library.tixte.internal.interceptor.CacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ErrorResponseInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ForceCacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.RateLimitInterceptor;
import dev.blocky.library.tixte.internal.utils.Checks;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Used to create <b>new</b> {@link TixteClient} instances.
 *
 * <p>A single {@link TixteClientBuilder} can be reused multiple times.
 * <br>Each call to {@link #build()} creates a <b>new</b> {@link TixteClient} instance using the same information.
 *
 * @author BlockyDotJar
 * @version v1.3.0
 * @since v1.0.0-alpha.1
 */
public record TixteClientBuilder()
{
    private static final Pattern SESSION_TOKEN_PATTERN = Pattern.compile("^tx.(mfa.)?([a-zA-Z\\d]){16}.([a-zA-Z\\d]){16}.([a-zA-Z\\d]){16}.([a-zA-Z\\d]){4}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern API_KEY_PATTERN = Pattern.compile("^([a-z\\d]){8}-([a-z\\d]){4}-([a-z\\d]){4}-([a-z\\d]){4}-([a-z\\d]){12}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern DEFAULT_DOMAIN_PATTERN = Pattern.compile("^(?!.*https?://)([a-zA-Z\\d_-])+.([a-zA-Z-])+.([a-zA-Z])+$", Pattern.CASE_INSENSITIVE);
    private static final Logger logger = FallbackLogger.getLog(TixteClientBuilder.class);
    private static final TixteClient tixteClient = new TixteClient();

    static final Dispatcher dispatcher = new Dispatcher();

    static String apiKey, sessionToken, defaultDomain;
    static boolean prettyResponsePrinting = false;
    static CachePolicy policy;
    static OkHttpClient client;
    static Request request;

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
     * @return Instantiates a <b>new</b> {@link TixteClientBuilder}.
     */
    @NotNull
    public TixteClientBuilder create(@NotNull String apiKey, @Nullable CachePolicy policy)
    {
        Checks.notEmpty(apiKey, "apiKey");
        Checks.noWhitespace(apiKey, "apiKey");

        Checks.check(API_KEY_PATTERN.matcher(apiKey).matches(), "Regex doesn't match with your API-key. Please check if you specified the right key. (session-token != API-key)");

        TixteClientBuilder.apiKey = apiKey;
        TixteClientBuilder.policy = policy;
        return this;
    }

    /**
     * Creates a <b>new</b> {@link TixteClientBuilder} instance by initializing the builder with your API-key.
     *
     * @param apiKey The API-key to use.
     *
     * @return Instantiates a <b>new</b> {@link TixteClientBuilder}.
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
     * @return Instantiates a <b>new</b> {@link TixteClientBuilder}r.
     */
    @NotNull
    public TixteClientBuilder setSessionToken(@NotNull String sessionToken)
    {
        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

        Checks.check(SESSION_TOKEN_PATTERN.matcher(sessionToken).matches(), "Regex doesn't match with your session-token. Please check if you specified the right token. (API-key != session-token)");

        TixteClientBuilder.sessionToken = sessionToken;
        return this;
    }

    /**
     * Creates a <b>new</b> {@link TixteClientBuilder} instance by initializing the builder with your default domain.
     *
     * @param defaultDomain The default domain to use.
     *
     * @return Instantiates a <b>new</b> {@link TixteClientBuilder}.
     */
    @NotNull
    public TixteClientBuilder setDefaultDomain(@NotNull String defaultDomain)
    {
        Checks.notEmpty(defaultDomain, "defaultDomain");
        Checks.noWhitespace(defaultDomain, "defaultDomain");

        Checks.check(DEFAULT_DOMAIN_PATTERN.matcher(defaultDomain).matches(), "Regex doesn't match with your default-domain. Please check if you specified a valid domain.");

        TixteClientBuilder.defaultDomain = defaultDomain;
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
     * @return Instantiates a <b>new</b> {@link TixteClientBuilder}.
     */
    @NotNull
    public TixteClientBuilder setCachePolicy(@Nullable CachePolicy policy)
    {
        TixteClientBuilder.policy = policy;
        return this;
    }

    /**
     * If there should be printed out a pretty string or not.
     *
     * <p><b>Example:</b>
     * <pre>
     * <code>
     * {
     *      "data" : {
     *       "limit" : 15000000000,
     *       "premium_tier" : 0,
     *        "used" : 156108016
     *      },
     *       "success" : true
     *    }
     * </code>
     * </pre>
     *
     * This is used for every type of response. (Normal responses and error responses)
     * <br>Note, that this could slow the process.
     *
     * @param prettyResponsePrinting Whether there shall be printed a pretty string of the response or not.
     *
     * @return Instantiates a <b>new</b>{@link TixteClientBuilder}.
     */
    @NotNull
    public TixteClientBuilder setPrettyResponsePrinting(boolean prettyResponsePrinting)
    {
        TixteClientBuilder.prettyResponsePrinting = prettyResponsePrinting;
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

        final ConnectionPool connectionPool = new ConnectionPool(5, 5, TimeUnit.SECONDS);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(true)
                .addInterceptor(new RateLimitInterceptor())
                .addInterceptor(new ErrorResponseInterceptor());

        if (policy == null)
        {
            policy = CachePolicy.NONE;
            logger.info("'policy' equals null, setting to 'NONE'.");
        }

        switch (policy)
        {
        case NONE -> client = builder.build();
        case ONLY_FORCE_CACHE -> client = builder
                .addInterceptor(new ForceCacheInterceptor())
                .build();
        case ONLY_NETWORK_CACHE -> client = builder
                .addNetworkInterceptor(new CacheInterceptor())
                .build();
        case ALL -> client =
                builder
                        .addInterceptor(new ForceCacheInterceptor())
                        .addNetworkInterceptor(new CacheInterceptor())
                        .build();
        }
        return tixteClient;
    }
}
