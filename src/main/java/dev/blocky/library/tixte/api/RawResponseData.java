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
package dev.blocky.library.tixte.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.internal.requests.Route;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.io.IOUtil;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import jdk.incubator.concurrent.StructuredTaskScope;
import okhttp3.*;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Future;

import static dev.blocky.library.tixte.api.TixteInfo.GITHUB;
import static dev.blocky.library.tixte.api.TixteInfo.VERSION;
import static dev.blocky.library.tixte.internal.requests.Route.TIXTE_API_PREFIX;

/**
 * Represents the raw response data from Tixte API-requests.
 *
 * @author BlockyDotJar
 * @version v3.0.2
 * @since v1.0.0-beta.1
 */
@Internal
public interface RawResponseData
{
    Logger logger = TixteLogger.getLog(RawResponseData.class);
    TixteClient tixteClient = new TixteClient();

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
    static Future<String> getSizeRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_UPLOAD_SIZE.compile();
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
    static Future<String> getUploadsRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_UPLOADS.compile();
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
    static Future<String> uploadFileRaw(@NotNull File file) throws IOException, InterruptedException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> uploadPrivateFileRaw(@NotNull File file) throws IOException, InterruptedException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> uploadFileRaw(@NotNull File file, @NotNull String domain) throws IOException, InterruptedException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> uploadPrivateFileRaw(@NotNull File file, @NotNull String domain) throws IOException, InterruptedException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> uploadFileRaw(@NotNull String filePath) throws IOException, InterruptedException
    {
        Checks.notEmpty(filePath, "filePath");

        final File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> uploadPrivateFileRaw(@NotNull String filePath) throws IOException, InterruptedException
    {
        Checks.notEmpty(filePath, "filePath");

        final File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> uploadFileRaw(@NotNull String filePath, @NotNull String domain) throws IOException, InterruptedException
    {
        Checks.notEmpty(filePath, "filePath");

        final File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> uploadPrivateFileRaw(@NotNull String filePath, @NotNull String domain) throws IOException, InterruptedException
    {
        Checks.notEmpty(filePath, "filePath");

        final File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        final RequestBody requestBody = RequestBody.create(file, MediaType.get("multipart/form-data"));

        final MultipartBody multipartBody = new MultipartBody.Builder()
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
    static Future<String> deleteFileRaw(@NotNull String fileId) throws IOException, InterruptedException
    {
        Checks.notEmpty(fileId, "fileId");
        Checks.noWhitespace(fileId, "fileId");

        final Route.CompiledRoute route = Route.Self.DELETE_FILE.compile(fileId);

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
    static Future<String> purgeFilesRaw(@NotNull String password) throws IOException, InterruptedException
    {
        Checks.notEmpty(password, "password");
        Checks.noWhitespace(password, "password");

        final Route.CompiledRoute route = Route.Self.DELETE_FILE.compile();

        final RequestBody requestBody = RequestBody.create("{ \"password\": \"" + password + "\", \"purge\": true }",
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
    static Future<String> getUserInfoRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_SELF.compile();
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
    static Future<String> getUserInfoRaw(@NotNull String userData) throws IOException, InterruptedException
    {
        Checks.notEmpty(userData, "userData");
        Checks.noWhitespace(userData, "userData");

        final Route.CompiledRoute route = Route.Users.GET_USER.compile(userData);

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
    static Future<String> getUserDomainsRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_DOMAINS.compile();
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
    static Future<String> getUsableDomainsRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Domain.GET_DOMAINS.compile();
        return request(route, false, null);
    }

    /**
     * @see Domains#generateDomain()
     *
     * @return The raw response of the request.
     */
    @NotNull
    static Future<String> generateDomainRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Resources.GET_GENERATED_DOMAIN.compile();
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
    static Future<String> addSubdomainRaw(@NotNull String domainName) throws IOException, InterruptedException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        final Route.CompiledRoute route = Route.Self.ADD_DOMAIN.compile(domainName);

        final RequestBody requestBody = RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": false }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, true, requestBody);
    }

    /**
     * @param domainName The domain name.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see Domains#addCustomDomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    static Future<String> addCustomDomainRaw(@NotNull String domainName) throws IOException, InterruptedException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        final Route.CompiledRoute route = Route.Self.ADD_DOMAIN.compile(domainName);

        final RequestBody requestBody = RequestBody.create("{ \"domain\": \"" + domainName + "\", \"custom\": true }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, true, requestBody);
    }

    /**
     * @param domainName The domain name.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see Domains#deleteDomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    static Future<String> deleteDomainRaw(@NotNull String domainName) throws IOException, InterruptedException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        final Route.CompiledRoute route = Route.Self.DELETE_DOMAIN.compile(domainName);

        return request(route, true, null);
    }

    /**
     * @see SelfUser#getAPIKeyBySessionToken()
     *
     * @return The raw response of the request.
     */
    @NotNull
    static Future<String> getAPIKeyBySessionTokenRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_KEYS.compile();
        return request(route, true, null);
    }

    /**
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
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
    static Future<String> getConfigRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_CONFIG.compile();
        return request(route, false, null);
    }

    /**
     * @param customCSS The custom CSS code for your page design.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see PageDesign#setCustomCSS(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    static Future<String> setCustomCSSRaw(@NotNull String customCSS) throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        final RequestBody requestBody = RequestBody.create("{ \"custom_css\": \"" + customCSS + "\" }",
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
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see Embed#Embed(String, String, String, String, String, String, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    static Future<String> setEmbedRaw(@Nullable String description, @Nullable String title, @Nullable String color,
                                      @Nullable String authorName, @Nullable String authorURL, @Nullable String providerName,
                                      @Nullable String providerURL) throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        final RequestBody requestBody = RequestBody.create(
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
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see PageDesign#setHideBranding(boolean)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    static Future<String> setHideBrandingRaw(boolean hideBranding) throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        final RequestBody requestBody = RequestBody.create("{ \"hide_branding\": " + hideBranding + " }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }

    /**
     * @param onlyImagedEnabled Whether only images are enabled or not.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see EmbedEditor#setOnlyImagedEnabled(boolean)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    static Future<String> setOnlyImageEnabledRaw(boolean onlyImagedEnabled) throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        final RequestBody requestBody = RequestBody.create("{ \"only_image\": " + onlyImagedEnabled + " }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }

    /**
     * @param redirectUrl The redirect url to be built.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see TixteClient#setBaseRedirect(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    static Future<String> setBaseRedirectRaw(@NotNull String redirectUrl) throws IOException, InterruptedException
    {
        Checks.notEmpty(redirectUrl, "redirectUrl");
        Checks.noWhitespace(redirectUrl, "redirectUrl");

        final Route.CompiledRoute route = Route.Self.PATCH_CONFIG.compile();

        final RequestBody requestBody = RequestBody.create("{ \"base_redirect\": " + redirectUrl + " }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, false, requestBody);
    }

    /**
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see SelfUser#getExperimentCount()
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CheckReturnValue
    static Future<String> getExperimentsRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_EXPERIMENTS.compile();
        return request(route, true, null);
    }

    /**
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The raw response of the request.
     */
    @NotNull
    @Experimental
    @CheckReturnValue
    static Future<String> getFoldersRaw() throws IOException, InterruptedException
    {
        final Route.CompiledRoute route = Route.Self.GET_FOLDERS.compile();
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
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The raw response of the request.
     */
    @NotNull
    @Experimental
    @CheckReturnValue
    static Future<String> setSearchQueryRaw(@NotNull String query, @Nullable String[] extensions,
                                            @Nullable String[] domains, @NotNull String sortBy,
                                            long minSize, long maxSize) throws IOException, InterruptedException
    {
        Checks.notNegative((int) minSize, "minSize");
        Checks.notNegative((int) maxSize, "maxSize");

        final Route.CompiledRoute route = Route.Self.SEARCH_FILE.compile();

        final RequestBody requestBody = RequestBody.create("{ \"query\": \"" + query + "\", " +
                        "\"extensions\": " + Arrays.toString(extensions) + ", \"domains\": " + Arrays.toString(domains) + ", " +
                        "\"sort_by\": \"" + sortBy + "\", \"size\": { \"min\": " + minSize + ", \"max\": " + maxSize + " } }",
                MediaType.get("application/json; charset=utf-8"));

        return request(route, true, requestBody);
    }

    /**
     * Gets the HTTP-headers of the request.
     * <br>Note that you must send a request to an endpoint before using this method.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The HTTP-headers of the request.
     */
    @Nullable
    @CheckReturnValue
    static Optional<String> getHeader() throws IOException, InterruptedException
    {
        final Call call = tixteClient.getHttpClient().newCall(tixteClient.getRequest().orElse(null));

        try (final Response response = call.execute(); final var scope = new StructuredTaskScope<String>())
        {
            final Future<String> responseStream = scope.fork(response.body()::string);
            scope.join();

            IOUtil.silentClose(response);

            return Optional.ofNullable(responseStream.resultNow());
        }
    }

    @Nullable
    @NonBlocking
    @CheckReturnValue
    private static Future<String> request(@NotNull Route.CompiledRoute route, boolean sessionTokenNeeded, @Nullable RequestBody requestBody) throws IOException, InterruptedException
    {
        final Request.Builder builder = new Request.Builder()
                .url(TIXTE_API_PREFIX + route.getCompiledRoute())
                .addHeader("Authorization", sessionTokenNeeded ? tixteClient.getSessionToken().orElse(null) : tixteClient.getAPIKey())
                .addHeader("User-Agent", "Tixte4J-Request (" + GITHUB + ", " + VERSION + ")");

        final Request request = switch (route.getMethod())
                {
                    case GET -> builder.build();
                    case DELETE -> requestBody == null ? builder.delete().build() : builder.delete(requestBody).build();
                    case PUT -> builder.put(requestBody).build();
                    case PATCH -> builder.patch(requestBody).build();
                    case POST -> builder.post(requestBody).build();
                };

        final Call call = tixteClient.getHttpClient().newCall(request);

        try (final Response response = call.execute(); final var scope = new StructuredTaskScope<String>())
        {
            final Future<String> responseString = scope.fork(response.body()::string);
            scope.join();

            IOUtil.silentClose(response);

            if (!TixteClientBuilder.prettyResponsePrinting)
            {
                logger.info("Request successful: " + route.getMethod() + "/" + route.getCompiledRoute());
            }
            else
            {
                System.out.println(prettyString(responseString, route));
            }

            return responseString;
        }
    }

    @Nullable
    @NonBlocking
    @CheckReturnValue
    private static Future<String> postFile(@Nullable String domain, @NotNull MultipartBody multipartBody, boolean privateFile) throws IOException, InterruptedException
    {
        final Request request = new Request.Builder()
                .url(TIXTE_API_PREFIX + Route.File.UPLOAD_FILE.getRoute())
                .addHeader("Authorization", tixteClient.getAPIKey())
                .addHeader("User-Agent", "Tixte4J-Request (" + GITHUB + ", " + VERSION + ")")
                .addHeader("domain", domain == null ? tixteClient.getDefaultDomain().orElse(null) : domain)
                .addHeader("type", privateFile ? "2" : "1")
                .post(multipartBody)
                .build();

        final Call call = tixteClient.getHttpClient().newCall(request);

        try (final Response response = call.execute(); final var scope = new StructuredTaskScope<String>())
        {
            final Future<String> responseString = scope.fork(response.body()::string);
            scope.join();

            IOUtil.silentClose(response);

            final Route.CompiledRoute route = Route.File.UPLOAD_FILE.compile();

            if (!TixteClientBuilder.prettyResponsePrinting)
            {
                logger.info("Request successful: " + route.getMethod() + "/" + route.getCompiledRoute());
            }
            else
            {
                System.out.println(prettyString(responseString, route));
            }

            return responseString;
        }
    }

    @NotNull
    @CheckReturnValue
    private static String prettyString(@NotNull Future<String> body, @NotNull Route.CompiledRoute route)
    {
        final DataObject object = DataObject.fromJson(body.resultNow());

        logger.info("'---->>>> Incoming Request: " + route.getMethod() + "/" + route.getCompiledRoute() + "<<<<----'");

        return object.toPrettyString();
    }
}
