/**
 * Copyright 2022 Dominic R. (aka. BlockyDotJar)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.blocky.library.tixte.internal;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.*;
import dev.blocky.library.tixte.api.entities.Domains;
import dev.blocky.library.tixte.api.entities.Embed;
import dev.blocky.library.tixte.api.entities.SelfUser;
import dev.blocky.library.tixte.api.entities.User;
import dev.blocky.library.tixte.internal.requests.FunctionalCallback;
import dev.blocky.library.tixte.internal.requests.Route;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.io.IOUtil;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import okhttp3.*;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static dev.blocky.library.tixte.api.TixteInfo.GITHUB;
import static dev.blocky.library.tixte.api.TixteInfo.VERSION;
import static dev.blocky.library.tixte.internal.requests.Route.TIXTE_API_PREFIX;

/**
 * Represents the raw response data from Tixte API-requests.
 *
 * @author BlockyDotJar
 * @version v3.0.1
 * @since v1.0.0-beta.1
 */
@Internal
public strictfp class RawResponseData
{
    private static final Logger logger = TixteLogger.getLog(RawResponseData.class);

    protected RawResponseData()
    {
    }

    /**
     * @see MyFiles#getUsedSize() MyFiles#getUsedSize()
     * @see MyFiles#getLimit() MyFiles#getLimit()
     * @see MyFiles#getPremiumTier() MyFiles#getPremiumTier()
     * @see SelfUser#hasTixteTurboSubscription() SelfUser#hasTixteTurboSubscription()
     * @see SelfUser#hasTixteTurboChargedSubscription() SelfUser#hasTixteTurboChargedSubscription()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getSizeRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_UPLOAD_SIZE.compile();
        return request(route, false, null);
    }

    /**
     * @see MyFiles#getTotalUploadCount()
     * @see MyFiles#getResults()
     * @see MyFiles#getPermissionLevels()
     * @see MyFiles#getExtensions()
     * @see MyFiles#getSizes()
     * @see MyFiles#getUploadDates()
     * @see MyFiles#getNames()
     * @see MyFiles#getDomains()
     * @see MyFiles#getMimeTypes()
     * @see MyFiles#getExpirationTimes()
     * @see MyFiles#getAssetIds()
     * @see MyFiles#getTypes()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getUploadsRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_UPLOADS.compile();
        return request(route, false, null);
    }

    /**
     * @param file The file to be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadFile(File)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadFileRaw(@NotNull File file) throws FileNotFoundException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(null, multipartBody, false);
    }

    /**
     * @param file The file to be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadPrivateFile(File)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadPrivateFileRaw(@NotNull File file) throws FileNotFoundException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(null, multipartBody, true);
    }

    /**
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadFile(File, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadFileRaw(@NotNull File file, @NotNull String domain) throws FileNotFoundException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(domain, multipartBody, false);
    }

    /**
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadPrivateFile(File, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadPrivateFileRaw(@NotNull File file, @NotNull String domain) throws FileNotFoundException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(domain, multipartBody, true);
    }

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadFile(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadFileRaw(@NotNull String filePath) throws FileNotFoundException
    {
        Checks.notEmpty(filePath, "filePath");

        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(null, multipartBody, false);
    }

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadPrivateFile(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadPrivateFileRaw(@NotNull String filePath) throws FileNotFoundException
    {
        Checks.notEmpty(filePath, "filePath");

        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(null, multipartBody, true);
    }

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadFile(String, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadFileRaw(@NotNull String filePath, @NotNull String domain) throws FileNotFoundException
    {
        Checks.notEmpty(filePath, "filePath");

        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(domain, multipartBody, false);
    }

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     *
     * @see MyFiles#uploadPrivateFile(String, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> uploadPrivateFileRaw(@NotNull String filePath, @NotNull String domain) throws FileNotFoundException
    {
        Checks.notEmpty(filePath, "filePath");

        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();

        return postFile(domain, multipartBody, true);
    }

    /**
     * @param fileId The ID of the file as a string.
     *
     * @see MyFiles#deleteFile(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> deleteFileRaw(@NotNull String fileId)
    {
        Checks.notEmpty(fileId, "fileId");
        Checks.noWhitespace(fileId, "fileId");

        Route.CompiledRoute route = Route.Self.DELETE_FILE.compile(fileId);

        return request(route, false, null);
    }

    /**
     * @param password The password of your Tixte account.
     *
     * @see MyFiles#purgeFiles(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> purgeFilesRaw(@NotNull String password)
    {
        Checks.notEmpty(password, "password");
        Checks.noWhitespace(password, "password");

        Route.CompiledRoute route = Route.Self.DELETE_FILE.compile();

        RequestBody requestBody = RequestBody.create("{ \"password\": \"" + password + "\", \"purge\": true }",
                MediaType.parse("application/json; charset=utf-8"));

        return request(route, true, requestBody);
    }

    /**
     * @see MyFiles#getUploadRegion()
     * @see SelfUser#getUsername()
     * @see SelfUser#getId()
     * @see SelfUser#getEmail()
     * @see SelfUser#getAvatarId()
     * @see SelfUser#getFlagCount()
     * @see SelfUser#getPremiumTier()
     * @see SelfUser#getPhoneNumber()
     * @see SelfUser#getLastLogin()
     * @see SelfUser#hasMFAEnabled()
     * @see SelfUser#isEmailVerified()
     * @see SelfUser#getUploadRegion()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getUserInfoRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_SELF.compile();
        return request(route, false, null);
    }

    /**
     * @param userData A user-id oder user-name.
     *
     * @see User#getFlagCount()
     * @see User#getAvatarId()
     * @see User#getId()
     * @see User#getUsername()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getUserInfoRaw(@NotNull String userData)
    {
        Checks.notEmpty(userData, "userData");
        Checks.noWhitespace(userData, "userData");

        Route.CompiledRoute route = Route.Users.GET_USER.compile(userData);

        return request(route, true, null);
    }

    /**
     * @see Domains#getUploadCounts()
     * @see Domains#getDomainNames()
     * @see Domains#getOwnerIds()
     * @see Domains#getDomainCount()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getUserDomainsRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_DOMAINS.compile();
        return request(route, true, null);
    }

    /**
     * @see Domains#getUsableDomainNames()
     * @see Domains#getUsableDomainCount()
     * @see Domains#isActive()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getUsableDomainsRaw()
    {
        Route.CompiledRoute route = Route.Domain.GET_DOMAINS.compile();
        return request(route, false, null);
    }

    /**
     * @see Domains#generateDomain()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> generateDomainRaw()
    {
        Route.CompiledRoute route = Route.Resources.GET_GENERATED_DOMAIN.compile();
        return request(route, false, null);
    }

    /**
     * @param domainName The domain name.
     *
     * @see Domains#addSubdomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> addSubdomainRaw(@NotNull String domainName)
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        Route.CompiledRoute route = Route.Self.ADD_DOMAIN.compile(domainName);

        RequestBody requestBody = RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": false }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, true, requestBody);
    }

    /**
     * @param domainName The domain name.
     *
     * @see Domains#addCustomDomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> addCustomDomainRaw(@NotNull String domainName)
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        Route.CompiledRoute route = Route.Self.ADD_DOMAIN.compile(domainName);

        RequestBody requestBody = RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": true }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, true, requestBody);
    }

    /**
     * @param domainName The domain name.
     *
     * @see Domains#deleteDomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> deleteDomainRaw(@NotNull String domainName)
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        Route.CompiledRoute route = Route.Self.DELETE_DOMAIN.compile(domainName);

        return request(route, true, null);
    }

    /**
     * @see SelfUser#getAPIKeyBySessionToken()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getAPIKeyBySessionTokenRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_KEYS.compile();
        return request(route, true, null);
    }

    /**
     * @see EmbedEditor#getEmbedDescription()
     * @see EmbedEditor#getEmbedAuthorName()
     * @see EmbedEditor#getEmbedAuthorUrl()
     * @see EmbedEditor#getEmbedTitle()
     * @see EmbedEditor#getEmbedProviderName()
     * @see EmbedEditor#getEmbedProviderUrl()
     * @see EmbedEditor#getEmbedThemeColor()
     * @see EmbedEditor#onlyImageEnabled()
     * @see TixteClient#baseRedirect()
     * @see PageDesign#getCustomCSS()
     * @see PageDesign#hidesBranding()
     *
     * @return The raw response of the request.
     */
    @NotNull
    protected static CompletableFuture<InputStream> getConfigRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_CONFIG.compile();
        return request(route, false, null);
    }

    /**
     * @param customCSS The custom CSS code for your page design.
     *
     * @see PageDesign#setCustomCSS(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> setCustomCSSRaw(@NotNull String customCSS)
    {
        Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        RequestBody requestBody = RequestBody.create("{ \"custom_css\": \"" + customCSS + "\" }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }

    /**
     * @param authorName The author name to be built.
     * @param authorURL The author url to be built.
     * @param title The title to be built.
     * @param description The description to be built.
     * @param color The color to be built.
     * @param providerName The provider name to be built.
     * @param providerURL The provider url to be built.
     *
     * @see Embed#Embed(String, String, String, String, String, String, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> setEmbedRaw(@Nullable String description, @Nullable String title, @Nullable String color,
                                                                @Nullable String authorName, @Nullable String authorURL, @Nullable String providerName,
                                                                @Nullable String providerURL)
    {
        Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        RequestBody requestBody = RequestBody.create(
                "{ \"embed\": { \"description\": \"" + description + "\", " +
                        "\"title\": \"" + title + "\", " +
                        "\"theme_color\": \"" + color + "\", " +
                        "\"author_name\": \"" + authorName + "\", " +
                        "\"author_url\": \"" + authorURL + "\", " +
                        "\"provider_name\": \"" + providerName + "\", " +
                        "\"provider_url\": \"" + providerURL + "\" } }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }


    /**
     * @param hideBranding Whether the branding is hidden or not.
     *
     * @see PageDesign#setHideBranding(boolean)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> setHideBrandingRaw(boolean hideBranding)
    {
        Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        RequestBody requestBody = RequestBody.create("{ \"hide_branding\": " + hideBranding + " }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }

    /**
     * @param onlyImagedEnabled Whether only images are enabled or not.
     *
     * @see EmbedEditor#setOnlyImagedEnabled(boolean)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> setOnlyImageEnabledRaw(boolean onlyImagedEnabled)
    {
        Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        RequestBody requestBody = RequestBody.create("{ \"only_image\": " + onlyImagedEnabled + " }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }

    /**
     * @param redirectUrl The redirect url to be built.
     *
     * @see TixteClient#setBaseRedirect(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    protected static CompletableFuture<InputStream> setBaseRedirectRaw(@NotNull String redirectUrl)
    {
        Checks.notEmpty(redirectUrl, "redirectUrl");
        Checks.noWhitespace(redirectUrl, "redirectUrl");

        Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        RequestBody requestBody = RequestBody.create("{ \"base_redirect\": " + redirectUrl + " }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }

    /**
     * @see SelfUser#getExperimentCount()
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CheckReturnValue
    protected static CompletableFuture<InputStream> getExperimentsRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_EXPERIMENTS.compile();
        return request(route, true, null);
    }

    /**
     * @return The raw response of the request.
     */
    @NotNull
    @Experimental
    @CheckReturnValue
    protected static CompletableFuture<InputStream> getFoldersRaw()
    {
        Route.CompiledRoute route = Route.Self.GET_FOLDERS.compile();
        return request(route, false, null);
    }

    /**
     * @param query The search query for searching files.
     * @param extensions The extensions, the files shall have.
     * @param domains The domain on which the file should be uploaded.
     * @param sortBy The value, the files shall be sorted by.
     * @param minSize The minimum size of the file.
     * @param maxSize The maximal size of the file.
     *
     * @return The raw response of the request.
     */
    @NotNull
    @Experimental
    @CheckReturnValue
    protected static CompletableFuture<InputStream> setSearchQueryRaw(@NotNull String query, @Nullable String[] extensions,
                                                                      @Nullable String[] domains, @NotNull String sortBy,
                                                                      long minSize, long maxSize)
    {
        Checks.notNegative((int) minSize, "minSize");
        Checks.notNegative((int) maxSize, "maxSize");

        Route.CompiledRoute route = Route.Self.SEARCH_FILE.compile();

        RequestBody requestBody = RequestBody.create("{ \"query\": \"" + query + "\", " +
                        "\"extensions\": " + Arrays.toString(extensions) + ", \"domains\": " + Arrays.toString(domains) + ", " +
                        "\"sort_by\": \"" + sortBy + "\", \"size\": { \"min\": " + minSize + ", \"max\": " + maxSize + " } }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, true, requestBody);
    }

    @Nullable
    @NonBlocking
    @CheckReturnValue
    private static CompletableFuture<InputStream> request(@NotNull Route.CompiledRoute route, boolean sessionTokenNeeded, @Nullable RequestBody requestBody)
    {
        Request.Builder builder = new Request.Builder()
                .url(TIXTE_API_PREFIX + route.getCompiledRoute())
                .addHeader("Authorization", sessionTokenNeeded ? getClient().getSessionToken().orElse(null) : getClient().getAPIKey())
                .addHeader("User-Agent", "Tixte4J-Request (" + GITHUB + ", " + VERSION + ")");

        CompletableFuture<InputStream> future = new CompletableFuture<>();

        try
        {
            Field field = TixteClientBuilder.class.getDeclaredField("request");
            field.setAccessible(true);

            Request request = (Request) field.get(Request.class);

            switch (route.getMethod())
            {
            case GET:
                request = builder.build();
                break;
            case DELETE:
                request = requestBody == null ? builder.delete().build() : builder.delete(requestBody).build();
                break;
            case PATCH:
                request = builder.patch(requestBody).build();
                break;
            case POST:
                request = builder.post(requestBody).build();
                break;
            }

            getHttpClient().newCall(request).enqueue(FunctionalCallback
                    .onFailure((call, e) -> future.completeExceptionally(new UncheckedIOException(e)))
                    .onSuccess((call, response) ->
                    {
                        if (response.isSuccessful())
                        {
                            logger.info("Request successful: " + route.getMethod() + "/" + route.getCompiledRoute());

                            InputStream body = IOUtil.getBody(response);

                            if (!future.complete(body))
                            {
                                IOUtil.silentClose(response);
                            }
                        }
                        else
                        {
                            IOUtil.silentClose(response);
                        }
                    }).build()
            );
            return future;
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @NonBlocking
    @CheckReturnValue
    private static CompletableFuture<InputStream> postFile(@Nullable String domain, @NotNull MultipartBody multipartBody, boolean privateFile)
    {
        Request request = new Request.Builder()
                .url(TIXTE_API_PREFIX + Route.File.UPLOAD_FILE.getRoute())
                .addHeader("Authorization", getClient().getAPIKey())
                .addHeader("User-Agent", "Tixte4J-Request (" + GITHUB + ", " + VERSION + ")")
                .addHeader("domain", domain == null ? getClient().getDefaultDomain().orElse(null) : domain)
                .addHeader("type", privateFile ? "2" : "1")
                .post(multipartBody)
                .build();

        CompletableFuture<InputStream> future = new CompletableFuture<>();

        getHttpClient().newCall(request).enqueue(FunctionalCallback
                .onFailure((call, e) -> future.completeExceptionally(new UncheckedIOException(e)))
                .onSuccess((call, response) ->
                {
                    if (response.isSuccessful())
                    {
                        logger.info("Request successful: " + Route.File.UPLOAD_FILE);

                        InputStream body = IOUtil.getBody(response);

                        if (!future.complete(body))
                        {
                            IOUtil.silentClose(response);
                        }
                    }
                    else
                    {
                        IOUtil.silentClose(response);
                    }
                }).build()
        );
        return future;
    }

    @NotNull
    @NonBlocking
    @CheckReturnValue
    private static TixteClient getClient()
    {
        try
        {
            Constructor<?> constructor = TixteClient.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (TixteClient) constructor.newInstance();
        }
        catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @NonBlocking
    @CheckReturnValue
    private static OkHttpClient getHttpClient()
    {
        try
        {
            Field field = TixteClientBuilder.class.getDeclaredField("client");
            field.setAccessible(true);
            return (OkHttpClient) field.get(OkHttpClient.class);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
