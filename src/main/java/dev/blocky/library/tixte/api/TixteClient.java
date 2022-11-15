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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.enums.CachePolicy;
import dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException;
import dev.blocky.library.tixte.internal.interceptor.CacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ErrorResponseInterceptor;
import dev.blocky.library.tixte.internal.interceptor.ForceCacheInterceptor;
import dev.blocky.library.tixte.internal.interceptor.RateLimitInterceptor;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static dev.blocky.library.tixte.api.TixteClientBuilder.*;

/**
 * The core of Tixte4J.
 * <br>Acts as a getting system of Tixte4J.
 * <br>All parts of the API can be accessed starting from this class.
 *
 * @author BlockyDotJar
 * @version v1.5.0
 * @since v1.0.0-alpha.1
 */
public class TixteClient implements RawResponseData
{
    private final SelfUser self = new SelfUser();

    TixteClient()
    {
    }

    /**
     * Gets the API-key, you specified with {@link TixteClientBuilder#create(String)}.
     *
     * @return The API-key, you specified with {@link TixteClientBuilder#create(String)}.
     */
    @NotNull
    public String getAPIKey()
    {
        return apiKey;
    }

    /**
     * Gets the session-token, you specified with {@link TixteClientBuilder#setSessionToken(String)}.
     * <br>Note that you must specify a session-token before using this method.
     *
     * @return The session-token, you specified with {@link TixteClientBuilder#setSessionToken(String)}.
     */
    @NotNull
    public Optional<String> getSessionToken()
    {
        return Optional.ofNullable(sessionToken);
    }

    /**
     * Gets the default domain, you specified with {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>Note that you must specify a default domain before using this method.
     *
     * @return The default domain, you specified with {@link TixteClientBuilder#setDefaultDomain(String)}.
     */
    @NotNull
    public Optional<String> getDefaultDomain()
    {
        return Optional.ofNullable(defaultDomain);
    }

    /**
     * An HTTP-request.
     * <br>Instances of this class are immutable if their {@link Request#body()} is {@code null} or itself immutable.
     * <br>Note that you must send a request to an endpoint before using this method.
     *
     * @return An HTTP-request.
     */
    @NotNull
    public Optional<Request> getRequest()
    {
        return Optional.ofNullable(request);
    }

    /**
     * Gets the current {@link CachePolicy}.
     * <br>The {@link CachePolicy} can be set by using {@link TixteClientBuilder#setCachePolicy(CachePolicy)}.
     * <br>The default {@link CachePolicy} is {@link CachePolicy#NONE}.
     *
     * @return The current {@link CachePolicy}.
     */
    @NotNull
    public CachePolicy getCachePolicy()
    {
        return policy;
    }

    /**
     * Gets the current {@link OkHttpClient}.
     * <br>The client gets created when <code>this</code> {@link TixteClient} gets created.
     *
     * <p>This client uses:
     * <ul>
     *     <li>A {@link Dispatcher}, which sets a rate-limit of 25 requests per host</li>
     *     <li>A {@link ConnectionPool}, which sets the count of <code>maxIdleConnections</code> to 5 and allows them to be kept alive for 5 seconds</li>
     *     <li>A Retry system, if the connections fails</li>
     *     <li>A {@link RateLimitInterceptor}, which handles rate-limits</li>
     *     <li>A {@link ErrorResponseInterceptor}, which handles different error responses</li>
     *     <li>(Optional) A {@link ForceCacheInterceptor}, which handles cache without internet connectivity</li>
     *     <li>(Optional) A {@link CacheInterceptor}, which handles cache with internet connectivity</li>
     * </ul>
     *
     * @return The current {@link OkHttpClient}.
     */
    @NotNull
    public OkHttpClient getHttpClient()
    {
        return client;
    }

    /**
     * Gets the current {@link Dispatcher}.
     * <br>This dispatcher gets active, when <code>this</code> {@link TixteClient} gets created.
     *
     * <p>This dispatcher a rate-limit of 25 requests per host.
     *
     * @return The current {@link Dispatcher}.
     */
    @NotNull
    public Dispatcher getDispatcher()
    {
        return dispatcher;
    }

    /**
     * Sets the redirect-url or disables the redirect.
     * <br>A redirect is a server- or client-side automatic forwarding from one url to another url.
     * <br>This {@link Object} can either be {@code false} or a specified redirect url as a string.
     * <br>This also requires a Tixte turbo/turbo-charged subscription or else there will be thrown a {@link TixteWantsYourMoneyException}.
     *
     * @param redirect Either {@code false} or a specified redirect Url as a string.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of the {@link TixteClient}.
     */
    @NotNull
    public TixteClient setBaseRedirect(@NotNull Object redirect) throws InterruptedException, IOException
    {
        if (!self.hasTixteSubscription())
        {
            throw new TixteWantsYourMoneyException("Payment required: This feature requires a turbo subscription");
        }

        RawResponseData.setBaseRedirectRaw(redirect);
        return this;
    }

    /**
     * This will return <b>false</b> if you have not set a redirect-url or this will return a string if you have set one.
     * <br>You can set the redirect-url by using {@link TixteClient#setBaseRedirect(Object)}.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return <b>false</b> - If you are not redirected to the redirect-url.
     *         <br><b>Otherwise</b> - The redirect-url as a string.
     */
    @NotNull
    @CheckReturnValue
    public Object baseRedirect() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getConfigRaw().resultNow());
        final DataObject data = json.getDataObject("data");

        return data.get("base_redirect");
    }

    /**
     * Closes the cache and deletes all of its stored values.
     * <br>This will delete all files in the cache directory including files that weren't created by the cache.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void pruneCache() throws IOException
    {
        try (final Cache cache = client.cache())
        {
            if (cache != null)
            {
                cache.delete();
                logger.info("Deleted cache successfully.");
            }
            else
            {
                logger.warn("No cache to delete.");
            }
        }
    }

    /**
     * Cancel all calls currently enqueued or executing.
     * <br>Includes calls executed both {@link Call#execute()} and {@link Call#enqueue(Callback)}.
     */
    public void cancelRequests()
    {
        if (request != null)
        {
            client.dispatcher().cancelAll();
            logger.info("Canceled all requests.");
        }
        else
        {
            logger.warn("No requests to cancel.");
        }
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

        final TixteClient that = (TixteClient) o;

        return Objects.equals(self, that.self);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(self);
    }

    @NotNull
    @Override
    public String toString()
    {
        return "TixteClient{" +
                "self=" + self +
                '}';
    }
}
