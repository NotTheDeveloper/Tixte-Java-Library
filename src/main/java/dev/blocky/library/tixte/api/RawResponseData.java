/**
 * Copyright 2022 Dominic (aka. BlockyDotJar)
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
import dev.blocky.library.tixte.api.enums.AccountType;
import dev.blocky.library.tixte.internal.utils.Checks;
import okhttp3.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import static dev.blocky.library.tixte.api.TixteClientBuilder.*;
import static dev.blocky.library.tixte.internal.requests.Route.Account.ACCOUNT_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.Account.USERS_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.BASE_URL;
import static dev.blocky.library.tixte.internal.requests.Route.Config.CONFIG_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.Domain.ACCOUNT_DOMAINS_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.Domain.DOMAINS_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.File.*;
import static dev.blocky.library.tixte.internal.requests.Route.Resources.GENERATE_DOMAIN_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.SLASH;

/**
 * Represents the raw response data from Tixte API-requests.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.1
 */
public strictfp class RawResponseData
{

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#getUsedSize() MyFiles#getUsedSize()
     * @see MyFiles#getLimit() MyFiles#getLimit()
     * @see MyFiles#getPremiumTier() MyFiles#getPremiumTier()
     * @see SelfUser#hasTixteTurboSubscription() SelfUser#hasTixteTurboSubscription()
     * @see SelfUser#hasTixteTurboChargedSubscription() SelfUser#hasTixteTurboChargedSubscription()
     *
     * @return The raw response of the request.
     */
    @NotNull
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

    /**
     * @param page The page you want to get the xyz from.
     * 
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
     * @see MyFiles#getTotalUploadCount() 
     * @see MyFiles#getResults(int)
     * @see MyFiles#getPermissionLevel(int, int) 
     * @see MyFiles#getExtension(int, int) 
     * @see MyFiles#getSize(int, int) 
     * @see MyFiles#getUploadDate(int, int) 
     * @see MyFiles#getName(int, int) 
     * @see MyFiles#getDomain(int, int) 
     * @see MyFiles#getMimeType(int, int) 
     * @see MyFiles#getExpirationTime(int, int) 
     * @see MyFiles#getAssetId(int, int) 
     * @see MyFiles#getType(int, int) 
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String getRawUploads(int page) throws IOException
    {
        Checks.notNegative(page, "page");

        request = new Request.Builder()
                .url(BASE_URL + FILE_ENDPOINT + PAGE + page)
                .addHeader("Authorization", apiKey)
                .build();

        try (Response response = client.newCall(request).execute())
        {
            return response.body().string();
        }
    }

    /**
     * @param file The file to be uploaded.
     * 
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
     * @see MyFiles#uploadFile(File) 
     *
     * @return The raw response of the request.
     */
    @NotNull
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

    /**
     * @param file The file to be uploaded.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
     * @see MyFiles#uploadPrivateFile(File)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @ApiStatus.Experimental
    public String uploadPrivateFileRaw(@NotNull File file) throws IOException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        request = new Request.Builder()
                .url(BASE_URL + UPLOAD_ENDPOINT)
                .addHeader("Authorization", apiKey)
                .addHeader("domain", defaultDomain)
                .addHeader("type", "2")
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

    /**
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
     * @see MyFiles#uploadFile(File, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String uploadFileRaw(@NotNull File file, @NotNull String domain) throws IOException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

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

    /**
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadPrivateFile(File, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @ApiStatus.Experimental
    public String uploadPrivateFileRaw(@NotNull File file, @NotNull String domain) throws IOException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        request = new Request.Builder()
                .url(BASE_URL + UPLOAD_ENDPOINT)
                .addHeader("Authorization", apiKey)
                .addHeader("domain", domain)
                .addHeader("type", "2")
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

    /**
     * @param filePath The {@link URI} to initialize the file, which should be uploaded.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadFile(URI)
     *
     * @return The raw response of the request.
     */
    @NotNull
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

    /**
     * @param filePath The {@link URI} to initialize the file, which should be uploaded.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadPrivateFile(URI)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @ApiStatus.Experimental
    public String uploadPrivateFileRaw(@NotNull URI filePath) throws IOException
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
                .addHeader("type", "2")
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

    /**
     * @param filePath The {@link URI} to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadFile(URI, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String uploadFileRaw(@NotNull URI filePath, @NotNull String domain) throws IOException
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

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

    /**
     * @param filePath The {@link URI} to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadPrivateFile(URI, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @ApiStatus.Experimental
    public String uploadPrivateFileRaw(@NotNull URI filePath, @NotNull String domain) throws IOException
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        request = new Request.Builder()
                .url(BASE_URL + UPLOAD_ENDPOINT)
                .addHeader("Authorization", apiKey)
                .addHeader("domain", domain)
                .addHeader("type", "2")
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

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadFile(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String uploadFileRaw(@NotNull String filePath) throws IOException
    {
        Checks.notEmpty(filePath, "filePath");

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

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadPrivateFile(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @ApiStatus.Experimental
    public String uploadPrivateFileRaw(@NotNull String filePath) throws IOException
    {
        Checks.notEmpty(filePath, "filePath");

        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        request = new Request.Builder()
                .url(BASE_URL + UPLOAD_ENDPOINT)
                .addHeader("Authorization", apiKey)
                .addHeader("domain", defaultDomain)
                .addHeader("type", "2")
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

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadFile(String, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String uploadFileRaw(@NotNull String filePath, @NotNull String domain) throws IOException
    {
        Checks.notEmpty(filePath, "filePath");

        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

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

    /**
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see MyFiles#uploadPrivateFile(String, String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @ApiStatus.Experimental
    public String uploadPrivateFileRaw(@NotNull String filePath, @NotNull String domain) throws IOException
    {
        Checks.notEmpty(filePath, "filePath");

        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(domain, "domain");
        Checks.noWhitespace(domain, "domain");

        request = new Request.Builder()
                .url(BASE_URL + UPLOAD_ENDPOINT)
                .addHeader("Authorization", apiKey)
                .addHeader("domain", domain)
                .addHeader("type", "2")
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

    /**
     * @param fileId The ID of the file as a string.
     * 
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
     * @see MyFiles#deleteFile(String) 
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    public String deleteFileRaw(@NotNull String fileId) throws IOException
    {
        Checks.notEmpty(fileId, "fileId");
        Checks.noWhitespace(fileId, "fileId");

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

    /**
     * @param password The password of your Tixte account.
     * 
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
     * @see MyFiles#purgeFiles(String) 
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    public String purgeFilesRaw(@NotNull String password) throws IOException
    {
        Checks.notEmpty(password, "password");
        Checks.noWhitespace(password, "password");

        request = new Request.Builder()
                .url(BASE_URL + FILE_ENDPOINT)
                .addHeader("Authorization", sessionToken)
                .delete(RequestBody.create("{ \"password\": \"" + password + "\", \"purge\": true }",
                        MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute())
        {
            return response.body().string();
        }
    }

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
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
    public String getUserInfoRaw() throws IOException
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

    /**
     * @param userData A user-id oder user-name.
     * 
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *                      
     * @see User#getFlagCount() 
     * @see User#getAvatarId() 
     * @see User#getId() 
     * @see User#getUsername() 
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String getUserInfoRaw(@NotNull String userData) throws IOException
    {
        Checks.notEmpty(userData, "userData");
        Checks.noWhitespace(userData, "userData");

        request = new Request.Builder()
                .url(BASE_URL + USERS_ENDPOINT + userData)
                .addHeader("Authorization", sessionToken)
                .build();

        try (Response response = client.newCall(request).execute())
        {
            return response.body().string();
        }
    }

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see Domains#getUploadCount(int)
     * @see Domains#getDomainName(int) 
     * @see Domains#getOwnerId(int) 
     * @see Domains#getDomainCount() 
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String getUserDomainsRaw() throws IOException
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

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see Domains#getUsableDomains(int)
     * @see Domains#getUsableDomainCount()
     * @see Domains#isActive(int)
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String getUsableDomainsRaw() throws IOException
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

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see Domains#generateDomain()
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String generateDomainRaw() throws IOException
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

    /**
     * @param domainName The domain name.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see Domains#addSubdomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    public String addSubdomainRaw(@NotNull String domainName) throws IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

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

    /**
     * @param domainName The domain name.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see Domains#addCustomDomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    public String addCustomDomainRaw(@NotNull String domainName) throws IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

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

    /**
     * @param domainName The domain name.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see Domains#deleteDomain(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String deleteDomainRaw(@NotNull String domainName) throws IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

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

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see SelfUser#getAPIKeyBySessionToken()
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String getAPIKeyBySessionTokenRaw() throws IOException
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

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see EmbedEditor#getEmbedDescription()
     * @see EmbedEditor#getEmbedAuthorName()
     * @see EmbedEditor#getEmbedAuthorURL()
     * @see EmbedEditor#getEmbedTitle()
     * @see EmbedEditor#getEmbedProviderName()
     * @see EmbedEditor#getEmbedProviderURL()
     * @see EmbedEditor#getEmbedThemeColor()
     * @see PageDesign#getCustomCSS()
     *
     * @return The raw response of the request.
     */
    @NotNull
    public String getConfigRaw() throws IOException
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

    /**
     * @param customCSS The custom CSS code for your page design.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see PageDesign#setCustomCSS(String)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    public String setCustomCSSRaw(@NotNull String customCSS) throws IOException
    {
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

    /**
     * @param authorName The author name to be built.
     * @param authorURL The author url to be built.
     * @param title The title to be built.
     * @param description The description to be built.
     * @param color The color to be built.
     * @param providerName The provider name to be built.
     * @param providerURL The provider url to be built.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see Embed#Embed(String, String, String, String, String, String, String, AccountType)
     *
     * @return The raw response of the request.
     */
    @NotNull
    @CanIgnoreReturnValue
    public String setEmbedRaw(@Nullable String description, @Nullable String title, @Nullable String color,
                              @Nullable String authorName, @Nullable String authorURL, @Nullable String providerName,
                              @Nullable String providerURL) throws IOException
    {
        request = new Request.Builder()
                .url(BASE_URL + CONFIG_ENDPOINT)
                .addHeader("Authorization", apiKey)
                .patch(RequestBody.create(
                        "{ \"embed\": { \"description\": \"" + description == null ? "" : description + "\", " +
                                "\"title\": \"" + title == null ? "" : title + "\", " +
                                "\"theme_color\": \"" + color == null ? "" : color + "\", " +
                                "\"author_name\": \"" + authorName == null ? "" : authorName + "\", " +
                                "\"author_url\": \"" + authorURL == null ? "" : authorURL + "\", " +
                                "\"provider_name\": \"" + providerName == null ? "" : providerName + "\", " +
                                "\"provider_url\": \"" + providerURL == null ? "" : providerURL + "\" } }",
                        MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute())
        {
            return response.body().string();
        }
    }
}
