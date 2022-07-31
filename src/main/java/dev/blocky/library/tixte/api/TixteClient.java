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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.enums.CachePolicy;
import dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static dev.blocky.library.tixte.api.RawResponseData.getConfigRaw;
import static dev.blocky.library.tixte.api.RawResponseData.setBaseRedirectRaw;
import static dev.blocky.library.tixte.api.TixteClientBuilder.*;

/**
 * The core of Tixte4J.
 * <br>Acts as a getting system of Tixte4J.
 * <br>All parts of the API can be accessed starting from this class.
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-alpha.1
 */
public class TixteClient
{

    /**
     * Instantiates a <b>new</b> Tixte-Client.
     */
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
    @Nullable
    @CheckReturnValue
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
    @Nullable
    @CheckReturnValue
    public Optional<String> getDefaultDomain()
    {
        return Optional.ofNullable(defaultDomain);
    }

    /**
     * Represents your Tixte user-account.
     *
     * @return Instantiates a <b>new</b> Self-User.
     */
    @NotNull
    public SelfUser getSelfUser()
    {
        return new SelfUser();
    }

    /**
     * Represents a Tixte user-account.
     *
     * @param userName  A specific user-name
     *
     * @return Instantiates a <b>new</b> user.
     */
    @Nullable
    @CheckReturnValue
    public User getUserByName(@NotNull String userName)
    {
        return new User(userName);
    }

    /**
     * Represents a Tixte user-account.
     *
     * @param userId A specific user-id.
     *
     * @return Instantiates a <b>new</b> user.
     */
    @Nullable
    @CheckReturnValue
    public User getUserById(@NotNull String userId)
    {
        return new User(userId);
    }

    /**
     * Represents the 'My Files' tab of the Tixte dashboard and everything else what Tixte offers you with files.
     *
     * @return Instantiates a <b>new</b> File-System.
     */
    @NotNull
    public MyFiles getFileSystem()
    {
        return new MyFiles();
    }

    /**
     * Represents the 'Domains' tab of the Tixte dashboard and everything else what Tixte offers you with domains.
     *
     * @return Instantiates a <b>new</b> Domain-System.
     */
    @NotNull
    public Domains getDomainSystem()
    {
        return new Domains();
    }

    /**
     * Builder system used to build {@link Embed embeds}.
     *
     * @return A <b>new</b> {@link EmbedEditor} instance, which can be used to create {@link Embed embeds}.
     */
    @NotNull
    public EmbedEditor getEmbedEditor()
    {
        return new EmbedEditor();
    }

    /**
     * Builder system used to build {@link Embed embeds}.
     *
     * @param editor The existing editor.
     *
     * @return An {@link EmbedEditor} using fields from an existing editor.
     */
    @Nullable
    @CheckReturnValue
    public EmbedEditor getEmbedEditor(@Nullable EmbedEditor editor)
    {
        return new EmbedEditor(editor);
    }

    /**
     * Builder system used to build {@link Embed embeds}.
     *
     * @param embed The embed.
     *
     * @return An {@link EmbedEditor} using fields in an existing embed.
     */
    @Nullable
    @CheckReturnValue
    public EmbedEditor getEmbedEditor(@Nullable Embed embed)
    {
        return new EmbedEditor(embed);
    }

    /**
     * Represents the 'Page Design' tab of the Tixte dashboard.
     *
     * @return Instantiates a <b>new</b> Page-Design.
     */
    @NotNull
    public PageDesign getPageDesign()
    {
        return new PageDesign();
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
     * An HTTP request.
     * <br>Instances of this class are immutable if their {@link Request#body()} is {@code null} or itself immutable.
     * <br>Note that you must send a request to an endpoint before using this method.
     *
     * @return An HTTP request.
     */
    @Nullable
    @CheckReturnValue
    public Optional<Request> getRequest()
    {
        return Optional.ofNullable(request);
    }

    /**
     * Gets the HTTP headers of the request.
     * <br>Note that you must send a request to an endpoint before using this method.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The HTTP headers of the request.
     */
    @Nullable
    @CheckReturnValue
    public Optional<String> getHeader() throws IOException
    {
        try (Response response = client.newCall(request).execute())
        {
            return Optional.ofNullable(response.headers().toString());
        }
    }

    /**
     * Closes the cache and deletes all of its stored values.
     * <br>This will delete all files in the cache directory including files that weren't created by the cache.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     */
    public void pruneCache() throws IOException
    {
        try (Cache cache = client.cache())
        {
            cache.delete();
        }
    }

    /**
     * Sets the redirect-URL.
     * <br>A redirect is a server- or client-side automatic forwarding from one URL to another URL.
     * <br>This requires a Tixte turbo/turbo-charged subscription or else there will be thrown a {@link TixteWantsYourMoneyException}.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @param redirectURL The redirect-URL.
     *
     * @return The current instance of the {@link TixteClient}.
     */
    @NotNull
    @CheckReturnValue
    public TixteClient setBaseRedirect(@NotNull String redirectURL) throws IOException
    {
        setBaseRedirectRaw(redirectURL);
        return this;
    }

    /**
     * This will return <b>false</b> if you have not set a redirect-url or this will return a string if you have set one.
     * <br>You can set the redirect-url by using {@link TixteClient#setBaseRedirect(String)}.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return <b>false</b> - If you are not redirected to the redirect-url.
     *         <br><b>Otherwise</b> - The redirect-url as a string.
     */
    @NotNull
    @CheckReturnValue
    public Object baseRedirect() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");

        return data.get("base_redirect");
    }

    /**
     * Cancel all calls currently enqueued or executing.
     * <br>Includes calls executed both {@link Call#execute()} and {@link Call#enqueue(Callback)}.
     */
    public void cancelRequests()
    {
        client.dispatcher().cancelAll();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(new TixteClient());
    }

    @NotNull
    @Override
    public String toString()
    {
        try
        {
            return "TixteClient{" +
                    "API_KEY='" + getAPIKey() + "', " +
                    "SESSION_TOKEN='" + getSessionToken() + "', " +
                    "DEFAULT_DOMAIN='" + getDefaultDomain() + "', " +
                    "policy=" + policy +  ", " +
                    "request=" + request + ", " +
                    "base_redirect='" + baseRedirect() + "'" +
                    '}';
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}