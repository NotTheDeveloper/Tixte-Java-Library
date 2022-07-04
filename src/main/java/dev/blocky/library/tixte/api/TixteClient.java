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
import dev.blocky.library.tixte.api.entities.Embed;
import dev.blocky.library.tixte.api.entities.SelfUser;
import dev.blocky.library.tixte.api.entities.User;
import dev.blocky.library.tixte.api.exceptions.HTTPException;
import dev.blocky.library.tixte.api.systems.DomainSystem;
import dev.blocky.library.tixte.api.systems.FileSystem;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static dev.blocky.library.tixte.api.TixteClientBuilder.*;


/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
@Undocumented
public class TixteClient
{

    @Undocumented
    TixteClient()
    {
    }

    @Undocumented
    public boolean awaitTermination(long timeout, @Nullable TimeUnit unit) throws InterruptedException
    {
        return client.dispatcher().executorService().awaitTermination(timeout, unit == null ? TimeUnit.SECONDS : unit);
    }

    @Undocumented
    public boolean awaitTermination(long timeout) throws InterruptedException
    {
        return client.dispatcher().executorService().awaitTermination(timeout, TimeUnit.SECONDS);
    }

    @Undocumented
    public void shutdown()
    {
        client.dispatcher().executorService().shutdown();
    }

    @Undocumented
    public void shutdownNow()
    {
        client.dispatcher().executorService().shutdownNow();
    }

    @Nullable
    @Undocumented
    public String getHeader() throws IOException
    {
        try (Response response = client.newCall(request).execute())
        {
            return Optional.ofNullable(response.headers().toString()).orElseThrow(() -> new HTTPException("No headers found. Probably because you use this method before a request is made."));
        }
    }

    @NotNull
    @Undocumented
    public static RawResponseData getRawResponseData()
    {
        return new RawResponseData();
    }

    @NotNull
    @Undocumented
    public String getAPIKey()
    {
        return apiKey;
    }

    @Nullable
    @Undocumented
    public String getSessionToken()
    {
        return Optional.ofNullable(sessionToken).orElseThrow(() -> new IllegalStateException("\"sessionToken\" is undefined."));
    }

    @Nullable
    @Undocumented
    public String getDefaultDomain()
    {
        return Optional.ofNullable(defaultDomain).orElseThrow(() -> new IllegalStateException("\"defaultDomain\" is undefined."));
    }

    @NotNull
    @Undocumented
    public SelfUser getSelfUser()
    {
        return new SelfUser();
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public User getUserByName(@NotNull String userName)
    {
        return new User(userName);
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public User getUserById(@NotNull String userId)
    {
        return new User(userId);
    }

    @NotNull
    @Undocumented
    public FileSystem getFileSystem()
    {
        return new FileSystem();
    }

    @NotNull
    @Undocumented
    public DomainSystem getDomainSystem()
    {
        return new DomainSystem();
    }

    @NotNull
    @Undocumented
    public EmbedEditor getEmbedEditor()
    {
        return new EmbedEditor();
    }

    @Nullable
    @Undocumented
    public EmbedEditor getEmbedEditor(@Nullable EmbedEditor builder)
    {
        return new EmbedEditor(builder);
    }

    @Nullable
    @Undocumented
    public EmbedEditor getEmbedEditor(@Nullable Embed embed)
    {
        return new EmbedEditor(embed);
    }

    @NotNull
    @Undocumented
    public PageDesign getPageDesign()
    {
        return new PageDesign();
    }
}
