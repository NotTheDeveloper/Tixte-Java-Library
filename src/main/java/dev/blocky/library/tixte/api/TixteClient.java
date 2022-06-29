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

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.blocky.library.tixte.annotations.Undocumented;
import dev.blocky.library.tixte.internal.interceptor.ErrorResponseInterceptor;
import dev.blocky.library.tixte.internal.interceptor.RateLimitInterceptor;
import dev.blocky.library.tixte.internal.utils.TixteLogger;
import okhttp3.*;
import org.conscrypt.Conscrypt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.annotation.CheckReturnValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.security.Security;
import java.util.Objects;

import static dev.blocky.library.tixte.internal.requests.Route.Account.*;
import static dev.blocky.library.tixte.internal.requests.Route.BASE_URL;
import static dev.blocky.library.tixte.internal.requests.Route.Config.CONFIG_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.Domain.ACCOUNT_DOMAINS_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.Domain.DOMAINS_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.File.*;
import static dev.blocky.library.tixte.internal.requests.Route.Resources.GENERATE_DOMAIN_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.SLASH;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
@Undocumented
public strictfp class TixteClient
{
    private final transient Logger logger = TixteLogger.getLog(TixteClient.class);
    private final transient Dispatcher dispatcher = new Dispatcher();
    private final transient OkHttpClient client;
    private final transient String apiKey;
    private transient volatile String url, directURL, deletionURL, domain;
    private transient volatile String sessionToken, defaultDomain;
    private transient volatile Request request;
    private transient volatile boolean isCustom;

    @Undocumented
    protected TixteClient(@NotNull String apiKey, @Nullable String sessionToken, @Nullable String defaultDomain)
    {
        this.apiKey = apiKey;
        this.sessionToken = sessionToken;
        this.defaultDomain = defaultDomain;

        if (apiKey.isEmpty())
        {
            throw new IllegalArgumentException("\"apiKey\" cannot be undefined.");
        }

        if (sessionToken.isEmpty() || sessionToken == null)
        {
            logger.warn("\"sessionToken\" is undefined.");
        }

        if (defaultDomain.isEmpty() || defaultDomain == null)
        {
            logger.warn("\"defaultDomain\" is undefined. Because of that you can simply use createTixteClient(@NotNull String, @NotNull String).");
        }

        dispatcher.setMaxRequestsPerHost(100);

        client = new OkHttpClient.Builder()
                .addInterceptor(new RateLimitInterceptor())
                .addInterceptor(new ErrorResponseInterceptor())
                .dispatcher(dispatcher)
                .build();

        Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }

    @Undocumented
    protected TixteClient(@NotNull String apiKey, @Nullable String sessionToken)
    {
        this.apiKey = apiKey;
        this.sessionToken = sessionToken;

        if (apiKey.isEmpty())
        {
            throw new IllegalArgumentException("\"apiKey\" cannot be undefined.");
        }

        if (sessionToken.isEmpty() || sessionToken == null)
        {
            logger.warn("\"sessionToken\" is undefined. Because of that you can simply use createTixteClient(@NotNull String)");
        }

        dispatcher.setMaxRequestsPerHost(100);

        client = new OkHttpClient.Builder()
                .addInterceptor(new RateLimitInterceptor())
                .addInterceptor(new ErrorResponseInterceptor())
                .dispatcher(dispatcher)
                .build();

        Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }

    @Undocumented
    protected TixteClient(@NotNull String apiKey)
    {
        this.apiKey = apiKey;

        if (apiKey.isEmpty())
        {
            throw new IllegalArgumentException("\"apiKey\" cannot be undefined.");
        }

        dispatcher.setMaxRequestsPerHost(100);

        client = new OkHttpClient.Builder()
                .addInterceptor(new RateLimitInterceptor())
                .addInterceptor(new ErrorResponseInterceptor())
                .dispatcher(dispatcher)
                .build();

        Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }

    @NotNull
    @Undocumented
    public String getHeader() throws IOException
    {
        try (Response response = client.newCall(request).execute())
        {
            return response.headers().toString();
        }
    }

    @NotNull
    @Undocumented
    public String getAPIKey()
    {
        return apiKey;
    }

    @Nullable
    @Undocumented
    public String getDefaultDomain()
    {
        return defaultDomain;
    }

    @Nullable
    @Undocumented
    public String getSessionToken()
    {
        return sessionToken;
    }

    @NotNull
    @Undocumented
    public TixteClient.User getTixteUser()
    {
        return new TixteClient.User();
    }

    @NotNull
    @Undocumented
    public TixteClient.FileSystem getTixteFileSystem()
    {
        return new TixteClient.FileSystem();
    }

    @NotNull
    @Undocumented
    public TixteClient.Domains getTixteDomains()
    {
        return new TixteClient.Domains();
    }

    @NotNull
    @Undocumented
    public TixteClient.Config getTixteConfig()
    {
        return new TixteClient.Config();
    }

    public TixteClient.Raw getRawTixteClient()
    {
        return new TixteClient.Raw();
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

        TixteClient that = (TixteClient) o;

        return apiKey.equals(that.apiKey) &&
                Objects.equals(request, that.request) &&
                Objects.equals(sessionToken, that.sessionToken) &&
                Objects.equals(defaultDomain, that.defaultDomain);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(apiKey, request, sessionToken, defaultDomain);
    }

    @Override
    public String toString() {
        return "TixteClient{" +
                "apiKey='" + apiKey + '\'' +
                ", request=" + request +
                ", sessionToken='" + sessionToken + '\'' +
                ", defaultDomain='" + defaultDomain + '\'' +
                '}';
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////7/////

    /**
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.1
     */
    @Undocumented
    public class User
    {
        @Undocumented
        public boolean isEmailVerified() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getBoolean("email_verified");
        }

        @Undocumented
        public String getPhoneNumber() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("phone");
        }

        @Undocumented
        public String getLastLogin() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("last_login");
        }

        @Undocumented
        public int getFlagCount() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getInt("flags");
        }

        @Undocumented
        public int getPremiumTier() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getInt("premium_tier");
        }

        @Undocumented
        public boolean hasMFAEnabled() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getBoolean("mfa_enabled");
        }

        @Undocumented
        public String getId() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("id");
        }

        @Undocumented
        public String getAvatarId() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("avatar");
        }

        @Undocumented
        public String getUploadRegion() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("upload_region");
        }

        @Undocumented
        public String getEmail() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("email");
        }

        @Undocumented
        @CheckReturnValue
        public String getUsername() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("username");
        }

        @Undocumented
        public int getFlagCount(@NotNull String user) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user));
            JSONObject data = json.getJSONObject("data");

            return data.getInt("flags");
        }

        @Undocumented
        public String getId(@NotNull String user) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user));
            JSONObject data = json.getJSONObject("data");

            return data.getString("id");
        }

        @Undocumented
        public String getAvatarId(@NotNull String user) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user));
            JSONObject data = json.getJSONObject("data");

            return data.getString("avatar");
        }

        @Undocumented
        @CheckReturnValue
        public String getUsername(@NotNull String user) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user));
            JSONObject data = json.getJSONObject("data");

            return data.getString("username");
        }

        @Undocumented
        public int getFlagCount(@NotNull String user, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user, sessionToken));
            JSONObject data = json.getJSONObject("data");

            return data.getInt("flags");
        }

        @Undocumented
        public String getId(@NotNull String user, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user, sessionToken));
            JSONObject data = json.getJSONObject("data");

            return data.getString("id");
        }

        @Undocumented
        public String getAvatarId(@NotNull String user, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user, sessionToken));
            JSONObject data = json.getJSONObject("data");

            return data.getString("avatar");
        }

        @Undocumented
        @CheckReturnValue
        public String getUsername(@NotNull String user, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfoByName(user, sessionToken));
            JSONObject data = json.getJSONObject("data");

            return data.getString("username");
        }

        @Undocumented
        @CheckReturnValue
        public String getAPIKeyBySessionToken() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawAPIKeyBySessionToken());
            JSONObject data = json.getJSONObject("data");

            return data.getString("api_key");
        }

        @Undocumented
        @CheckReturnValue
        public String getAPIKeyBySessionToken(@NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawAPIKeyBySessionToken(sessionToken));
            JSONObject data = json.getJSONObject("data");

            return data.getString("api_key");
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////7/////

    /**
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.1
     */
    @Undocumented
    public class FileSystem
    {
        @Undocumented
        public long getUsedSize() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawSize());
            JSONObject data = json.getJSONObject("data");
            return data.getInt("used");
        }

        @Undocumented
        public long getLimit() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawSize());
            JSONObject data = json.getJSONObject("data");
            return data.getInt("limit");
        }

        @Undocumented
        public int getPremiumTier() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawSize());
            JSONObject data = json.getJSONObject("data");
            return data.getInt("premium_tier");
        }

        @Undocumented
        public int getTotalUploadCount(int page) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            return data.getInt("total");
        }

        @Undocumented
        public int getResults(int page) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            return data.getInt("results");
        }

        @Undocumented
        public int getPermissionLevel(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getInt("permission_level");
        }

        @Undocumented
        public String getExtension(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getString("extension");
        }

        @Undocumented
        public long getSize(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getInt("size");
        }

        @Undocumented
        public String getUploadDate(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getString("uploaded_at");
        }

        @Undocumented
        public String getDomain(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getString("domain");
        }

        @Undocumented
        public String getName(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getString("name");
        }

        @Undocumented
        public String getMimeType(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getString("mimetype");
        }

        @Undocumented
        public String getExpirationTime(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getString("expiration");
        }

        @Undocumented
        public String getAssetId(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getString("asset_id");
        }

        @Undocumented
        public int getType(int page, int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUploads(page));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("uploads");

            return array.getJSONObject(index).getInt("type");
        }

        @Undocumented
        public String getFileName(int page, int index) throws IOException
        {
            return getName(page, index) + "." + getExtension(page, index);
        }

        @Undocumented
        public String getUploadRegion() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserInfo());
            JSONObject data = json.getJSONObject("data");

            return data.getString("upload_region");
        }

        @Undocumented
        public String getURL()
        {
            return url;
        }

        @Undocumented
        public String getDirectURL()
        {
            return directURL;
        }

        @Undocumented
        public String getDeletionURL()
        {
            return deletionURL;
        }

        @Undocumented
        public void uploadFile(@NotNull File file) throws IOException
        {
                JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(file));
                JSONObject data = json.getJSONObject("data");

                url = data.getString("url");
                directURL = data.getString("direct_url");
                deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull File file, boolean isPrivate) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(file, isPrivate));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull File file, @NotNull String domain) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(file, domain));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull File file, @NotNull String domain, boolean isPrivate) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(file, domain, isPrivate));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull URI filePath) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull URI filePath, boolean isPrivate) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath, isPrivate));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull URI filePath, @NotNull String domain) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath, domain));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull URI filePath, @NotNull String domain, boolean isPrivate) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath, domain, isPrivate));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull String filePath) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull String filePath, boolean isPrivate) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath, isPrivate));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull String filePath, @NotNull String domain) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath, domain));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void uploadFile(@NotNull String filePath, @NotNull String domain, boolean isPrivate) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().uploadFileRaw(filePath, domain, isPrivate));
            JSONObject data = json.getJSONObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }

        @Undocumented
        public void deleteFile(@NotNull String fileId) throws IOException
        {
            getRawTixteClient().deleteFileRaw(fileId);
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////7/////

    /**
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.1
     */
    @Undocumented
    public class Domains
    {
        @Undocumented
        public int getUsableDomainCount() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawOwnedDomains());
            JSONObject data = json.getJSONObject("data");

            return data.getInt("count");
        }

        @Undocumented
        public String getUsableDomains(int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawOwnedDomains());
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getString("domain");
        }

        @Undocumented
        public boolean isActive(int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawOwnedDomains());
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getBoolean("active");
        }

        @Undocumented
        public int getDomainCount() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains());
            JSONObject data = json.getJSONObject("data");

            return data.getInt("total");
        }

        @Undocumented
        public String getOwnerId(int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains());
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getString("owner");
        }

        @Undocumented
        public String getDomainName(int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains());
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getString("name");
        }

        @Undocumented
        public int getUploadCount(int index) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains());
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getInt("uploads");
        }

        @Undocumented
        public int getDomainCount(@NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains(sessionToken));
            JSONObject data = json.getJSONObject("data");

            return data.getInt("total");
        }

        @Undocumented
        public String getOwnerId(int index, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains(sessionToken));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getString("owner");
        }

        @Undocumented
        public String getDomainName(int index, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains(sessionToken));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getString("name");
        }

        @Undocumented
        public int getUploadCount(int index, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawUserDomains(sessionToken));
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("domains");

            return array.getJSONObject(index).getInt("uploads");
        }

        @NotNull
        @Undocumented
        public String generateDomain() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().generateRawDomain());
            JSONObject data = json.getJSONObject("data");

            return data.getString("name") + "." + data.getString("domain");
        }

        @Undocumented
        public boolean isCustom()
        {
            return isCustom;
        }

        @Undocumented
        public String getLastDeletedDomain()
        {
            return domain;
        }

        @Undocumented
        public void addSubdomain(@NotNull String domainName) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().addSubdomainRaw(domainName));
            JSONObject data = json.getJSONObject("data");

            isCustom = data.getBoolean("custom");
        }

        @Undocumented
        public void addSubdomain(@NotNull String domainName, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().addSubdomainRaw(domainName, sessionToken));
            JSONObject data = json.getJSONObject("data");

            isCustom = data.getBoolean("custom");
        }

        @Undocumented
        public void addCustomDomain(@NotNull String domainName) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().addCustomDomainRaw(domainName));
            JSONObject data = json.getJSONObject("data");

            isCustom = data.getBoolean("custom");
        }

        @Undocumented
        public void addCustomDomain(@NotNull String domainName, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().addCustomDomainRaw(domainName, sessionToken));
            JSONObject data = json.getJSONObject("data");

            isCustom = data.getBoolean("custom");
        }

        @Undocumented
        public void deleteDomain(@NotNull String domainName) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().deleteDomainRaw(domainName));
            JSONObject data = json.getJSONObject("data");

            domain = data.getString("domain");
        }

        @Undocumented
        public void deleteDomain(@NotNull String domainName, @NotNull String sessionToken) throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().deleteDomainRaw(domainName, sessionToken));
            JSONObject data = json.getJSONObject("data");

            domain = data.getString("domain");
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////7/////

    /**
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.3
     */
    public class Config
    {
        @NotNull
        @Undocumented
        public String getCustomCSS() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");

            return data.getString("custom_css");
        }

        @Undocumented
        public boolean hidesBranding() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");

            return data.getBoolean("hide_branding");
        }

        @Undocumented
        public boolean isBaseRedirected() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");

            return data.getBoolean("base_redirect");
        }

        @Undocumented
        public boolean onlyImageActivated() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");

            return data.getBoolean("only_image");
        }

        @NotNull
        @Undocumented
        public String getEmbedTitle() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");
            JSONObject embed = data.getJSONObject("embed");

            return embed.getString("title");
        }

        @NotNull
        @Undocumented
        public String getEmbedThemeColor() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");
            JSONObject embed = data.getJSONObject("embed");

            return embed.getString("theme_color");
        }

        @NotNull
        @Undocumented
        public String getEmbedAuthorName() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");
            JSONObject embed = data.getJSONObject("embed");

            return embed.getString("author_name");
        }

        @NotNull
        @Undocumented
        public String getEmbedAuthorURL() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");
            JSONObject embed = data.getJSONObject("embed");

            return embed.getString("author_url");
        }

        @NotNull
        @Undocumented
        public String getEmbedProviderName() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");
            JSONObject embed = data.getJSONObject("embed");

            return embed.getString("provider_name");
        }

        @NotNull
        @Undocumented
        public String getEmbedProviderURL() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");
            JSONObject embed = data.getJSONObject("embed");

            return embed.getString("provider_url");
        }

        @NotNull
        @Undocumented
        public String getEmbedDescription() throws IOException
        {
            JSONObject json = new JSONObject(getRawTixteClient().getRawConfig());
            JSONObject data = json.getJSONObject("data");
            JSONObject embed = data.getJSONObject("embed");

            return embed.getString("description");
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////7/////

    /**
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.1
     */
    public class Raw
    {
        @NotNull
        @Undocumented
        public String getRawSize() throws IOException 
        {
            request = new Request.Builder()
                    .url(BASE_URL + SIZE_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawUploads(int page) throws IOException
        {
            if (page < 0)
            {
                throw new IllegalArgumentException("\"page\" cannot be under 0.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + FILE_ENDPOINT + PAGE + page)
                    .addHeader("Authorization", apiKey)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull File file) throws IOException 
        {
            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", defaultDomain)
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull File file, boolean isPrivate) throws IOException 
        {
            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", defaultDomain)
                    .addHeader("type", isPrivate ? "2": "1")
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull File file, @NotNull String domain) throws IOException 
        {
            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            if (domain.isEmpty())
            {
                throw new IllegalArgumentException("\"domain\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", domain)
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull File file, @NotNull String domain, boolean isPrivate) throws IOException
        {
            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", domain)
                    .addHeader("type", isPrivate ? "2": "1")
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull URI filePath) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", defaultDomain)
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull URI filePath, boolean isPrivate) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", defaultDomain)
                    .addHeader("type", isPrivate ? "2": "1")
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull URI filePath, @NotNull String domain) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            if (domain.isEmpty())
            {
                throw new IllegalArgumentException("\"domain\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", domain)
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull URI filePath, @NotNull String domain, boolean isPrivate) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", domain)
                    .addHeader("type", isPrivate ? "2": "1")
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
               return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull String filePath) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", defaultDomain)
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull String filePath, boolean isPrivate) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", defaultDomain)
                    .addHeader("type", isPrivate ? "2": "1")
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull String filePath, @NotNull String domain) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            if (domain.isEmpty())
            {
                throw new IllegalArgumentException("\"domain\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", domain)
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String uploadFileRaw(@NotNull String filePath, @NotNull String domain, boolean isPrivate) throws IOException
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                throw new FileNotFoundException("File " + file.getName() + " was not found.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + UPLOAD_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .addHeader("domain", domain)
                    .addHeader("type", isPrivate ? "2": "1")
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(),
                                    RequestBody.create(file, MediaType.get("multipart/form-data")))
                            .build()
                    )
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        @CanIgnoreReturnValue
        public String deleteFileRaw(@NotNull String fileId) throws IOException
        {
            if (fileId.isEmpty())
            {
                throw new IllegalArgumentException("\"fileId\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + FILE_ENDPOINT + SLASH + fileId)
                    .addHeader("Authorization", apiKey)
                    .delete()
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawUserInfo() throws IOException
        {
            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawUserInfoByName(@NotNull String user) throws IOException
        {
            if (user.isEmpty())
            {
                throw new IllegalArgumentException("\"user\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + USERS_ENDPOINT + user)
                    .addHeader("Authorization", sessionToken)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawUserInfoByName(@NotNull String user, @NotNull String sessionToken) throws IOException
        {
            if (sessionToken.isEmpty())
            {
                throw new IllegalArgumentException("\"sessionToken\" cannot be undefined.");
            }

            if (user.isEmpty())
            {
                throw new IllegalArgumentException("\"user\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + "/users/" + user)
                    .addHeader("Authorization", sessionToken)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawUserDomains() throws IOException
        {
            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT)
                    .addHeader("Authorization", sessionToken)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawUserDomains(@NotNull String sessionToken) throws IOException
        {
            if (sessionToken.isEmpty())
            {
                throw new IllegalArgumentException("\"sessionToken\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT)
                    .addHeader("Authorization", sessionToken)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawOwnedDomains() throws IOException
        {
            request = new Request.Builder()
                    .url(BASE_URL + DOMAINS_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String generateRawDomain() throws IOException
        {
            request = new Request.Builder()
                    .url(BASE_URL + GENERATE_DOMAIN_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String addSubdomainRaw(@NotNull String domainName) throws IOException
        {
            if (domainName.isEmpty())
            {
                throw new IllegalArgumentException("\"domainName\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT)
                    .addHeader("Authorization", sessionToken)
                    .patch(RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": false }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String addSubdomainRaw(@NotNull String domainName, @NotNull String sessionToken) throws IOException
        {
            if (domainName.isEmpty())
            {
                throw new IllegalArgumentException("\"domainName\" cannot be undefined.");
            }

            if (sessionToken.isEmpty())
            {
                throw new IllegalArgumentException("\"sessionToken\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT)
                    .addHeader("Authorization", sessionToken)
                    .patch(RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": false }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String addCustomDomainRaw(@NotNull String domainName) throws IOException
        {
            if (domainName.isEmpty())
            {
                throw new IllegalArgumentException("\"domainName\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT)
                    .addHeader("Authorization", sessionToken)
                    .patch(RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": true }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String addCustomDomainRaw(@NotNull String domainName, @NotNull String sessionToken) throws IOException
        {
            if (domainName.isEmpty())
            {
                throw new IllegalArgumentException("\"domainName\" cannot be undefined.");
            }

            if (sessionToken.isEmpty())
            {
                throw new IllegalArgumentException("\"sessionToken\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT)
                    .addHeader("Authorization", sessionToken)
                    .patch(RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": true }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String deleteDomainRaw(@NotNull String domainName) throws IOException
        {
            if (domainName.isEmpty())
            {
                throw new IllegalArgumentException("\"domainName\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT + SLASH + domainName)
                    .addHeader("Authorization", sessionToken)
                    .delete()
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String deleteDomainRaw(@NotNull String domainName, @NotNull String sessionToken) throws IOException
        {
            if (domainName.isEmpty())
            {
                throw new IllegalArgumentException("\"domainName\" cannot be undefined.");
            }

            if (sessionToken.isEmpty())
            {
                throw new IllegalArgumentException("\"sessionToken\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_DOMAINS_ENDPOINT + SLASH + domainName)
                    .addHeader("Authorization", sessionToken)
                    .delete()
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawAPIKeyBySessionToken() throws IOException
        {
            request = new Request.Builder()
                    .url(BASE_URL + ACCOUNT_ENDPOINT + "/keys")
                    .addHeader("Authorization", sessionToken)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawAPIKeyBySessionToken(@NotNull String sessionToken) throws IOException
        {
            request = new Request.Builder()
                    .url(BASE_URL + KEYS_ENDPOINT)
                    .addHeader("Authorization", sessionToken)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String getRawConfig() throws IOException
        {
            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setCustomCSSRaw(@NotNull String customCSS) throws IOException
        {
            if (customCSS.isEmpty())
            {
                throw new IllegalArgumentException("\"customCSS\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create("{ \"custom_css\": \"" + customCSS + "\" }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setEmbedTitleRaw(@NotNull String title) throws IOException
        {
            if (title.isEmpty())
            {
                throw new IllegalArgumentException("\"title\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create(
                            "{ \"embed\": { \"description\": \"" + getTixteConfig().getEmbedDescription() + "\", " +
                                    "\"title\": \"" + title + "\", " +
                                    "\"theme_color\": \"" + getTixteConfig().getEmbedThemeColor() + "\", " +
                                    "\"author_name\": \"" + getTixteConfig().getEmbedAuthorName() + "\", " +
                                    "\"author_url\": \"" + getTixteConfig().getEmbedAuthorURL() + "\", " +
                                    "\"provider_name\": \"" + getTixteConfig().getEmbedProviderName() + "\", " +
                                    "\"provider_url\": \"" + getTixteConfig().getEmbedProviderURL() + "\" } }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setEmbedThemeColorRaw(@NotNull String themeColor) throws IOException
        {
            if (themeColor.isEmpty())
            {
                throw new IllegalArgumentException("\"themeColor\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create(
                            "{ \"embed\": { \"description\": \"" + getTixteConfig().getEmbedDescription() + "\", " +
                                    "\"title\": \"" + getTixteConfig().getEmbedTitle() + "\", " +
                                    "\"theme_color\": \"" + themeColor + "\", " +
                                    "\"author_name\": \"" + getTixteConfig().getEmbedAuthorName() + "\", " +
                                    "\"author_url\": \"" + getTixteConfig().getEmbedAuthorURL() + "\", " +
                                    "\"provider_name\": \"" + getTixteConfig().getEmbedProviderName() + "\", " +
                                    "\"provider_url\": \"" + getTixteConfig().getEmbedProviderURL() + "\" } }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setEmbedAuthorNameRaw(@NotNull String authorName) throws IOException
        {
            if (authorName.isEmpty())
            {
                throw new IllegalArgumentException("\"authorName\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create(
                            "{ \"embed\": { \"description\": \"" + getTixteConfig().getEmbedDescription() + "\", " +
                                    "\"title\": \"" + getTixteConfig().getEmbedTitle() + "\", " +
                                    "\"theme_color\": \"" + getTixteConfig().getEmbedThemeColor() + "\", " +
                                    "\"author_name\": \"" + authorName + "\", " +
                                    "\"author_url\": \"" + getTixteConfig().getEmbedAuthorURL() + "\", " +
                                    "\"provider_name\": \"" + getTixteConfig().getEmbedProviderName() + "\", " +
                                    "\"provider_url\": \"" + getTixteConfig().getEmbedProviderURL() + "\" } }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setEmbedAuthorURLRaw(@NotNull String authorURL) throws IOException
        {
            if (authorURL.isEmpty())
            {
                throw new IllegalArgumentException("\"authorURL\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create(
                            "{ \"embed\": { \"description\": \"" + getTixteConfig().getEmbedDescription() + "\", " +
                                    "\"title\": \"" + getTixteConfig().getEmbedTitle() + "\", " +
                                    "\"theme_color\": \"" + getTixteConfig().getEmbedThemeColor() + "\", " +
                                    "\"author_name\": \"" + getTixteConfig().getEmbedAuthorName() + "\", " +
                                    "\"author_url\": \"" + authorURL + "\", " +
                                    "\"provider_name\": \"" + getTixteConfig().getEmbedProviderName() + "\", " +
                                    "\"provider_url\": \"" + getTixteConfig().getEmbedProviderURL() + "\" } }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setEmbedProviderNameRaw(@NotNull String providerName) throws IOException
        {
            if (providerName.isEmpty())
            {
                throw new IllegalArgumentException("\"providerName\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create(
                            "{ \"embed\": { \"description\": \"" + getTixteConfig().getEmbedDescription() + "\", " +
                                    "\"title\": \"" + getTixteConfig().getEmbedTitle() + "\", " +
                                    "\"theme_color\": \"" + getTixteConfig().getEmbedThemeColor() + "\", " +
                                    "\"author_name\": \"" + getTixteConfig().getEmbedAuthorName() + "\", " +
                                    "\"author_url\": \"" + getTixteConfig().getEmbedAuthorURL() + "\", " +
                                    "\"provider_name\": \"" + providerName + "\", " +
                                    "\"provider_url\": \"" + getTixteConfig().getEmbedProviderURL() + "\" } }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setEmbedProviderURLRaw(@NotNull String providerURL) throws IOException
        {
            if (providerURL.isEmpty())
            {
                throw new IllegalArgumentException("\"providerURL\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create(
                            "{ \"embed\": { \"description\": \"" + getTixteConfig().getEmbedDescription() + "\", " +
                                    "\"title\": \"" + getTixteConfig().getEmbedTitle() + "\", " +
                                    "\"theme_color\": \"" + getTixteConfig().getEmbedThemeColor() + "\", " +
                                    "\"author_name\": \"" + getTixteConfig().getEmbedAuthorName() + "\", " +
                                    "\"author_url\": \"" + getTixteConfig().getEmbedAuthorURL() + "\", " +
                                    "\"provider_name\": \"" + getTixteConfig().getEmbedProviderName() + "\", " +
                                    "\"provider_url\": \"" + providerURL + "\" } }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }

        @NotNull
        @Undocumented
        public String setEmbedDescriptionRaw(@NotNull String description) throws IOException
        {
            if (description.isEmpty())
            {
                throw new IllegalArgumentException("\"description\" cannot be undefined.");
            }

            request = new Request.Builder()
                    .url(BASE_URL + CONFIG_ENDPOINT)
                    .addHeader("Authorization", apiKey)
                    .patch(RequestBody.create(
                            "{ \"embed\": { \"description\": \"" + description + "\", " +
                                    "\"title\": \"" + getTixteConfig().getEmbedTitle() + "\", " +
                                    "\"theme_color\": \"" + getTixteConfig().getEmbedThemeColor() + "\", " +
                                    "\"author_name\": \"" + getTixteConfig().getEmbedAuthorName() + "\", " +
                                    "\"author_url\": \"" + getTixteConfig().getEmbedAuthorURL() + "\", " +
                                    "\"provider_name\": \"" + getTixteConfig().getEmbedProviderName() + "\", " +
                                    "\"provider_url\": \"" + getTixteConfig().getEmbedProviderURL() + "\" } }",
                            MediaType.get("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute())
            {
                return response.body().string();
            }
        }
    }
}
