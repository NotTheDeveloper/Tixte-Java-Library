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
import dev.blocky.library.tixte.annotations.Undocumented;
import dev.blocky.library.tixte.internal.utils.Checks;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import static dev.blocky.library.tixte.api.TixteClientBuilder.*;
import static dev.blocky.library.tixte.internal.requests.Route.Account.*;
import static dev.blocky.library.tixte.internal.requests.Route.BASE_URL;
import static dev.blocky.library.tixte.internal.requests.Route.Config.CONFIG_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.Domain.ACCOUNT_DOMAINS_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.Domain.DOMAINS_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.File.*;
import static dev.blocky.library.tixte.internal.requests.Route.File.PAGE;
import static dev.blocky.library.tixte.internal.requests.Route.Resources.GENERATE_DOMAIN_ENDPOINT;
import static dev.blocky.library.tixte.internal.requests.Route.SLASH;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.1
 */
@Undocumented
public class RawResponseData
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

        try (
                Response response = client.newCall(request).execute())
        {
            return response.body().string();
        }
    }

    @NotNull
    @Undocumented
    public String uploadFilePrivateRaw(@NotNull File file) throws IOException
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

    @NotNull
    @Undocumented
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

    @NotNull
    @Undocumented
    public String uploadFilePrivateRaw(@NotNull File file, @NotNull String domain) throws IOException
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
    public String uploadFilePrivateRaw(@NotNull URI filePath) throws IOException
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

    @NotNull
    @Undocumented
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

    @NotNull
    @Undocumented
    public String uploadFilePrivateRaw(@NotNull URI filePath, @NotNull String domain) throws IOException
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

    @NotNull
    @Undocumented
    public String uploadFileRaw(@NotNull String filePath) throws IOException
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(filePath, "filePath");

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
    public String uploadFilePrivateRaw(@NotNull String filePath) throws IOException
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(filePath, "filePath");

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

    @NotNull
    @Undocumented
    public String uploadFileRaw(@NotNull String filePath, @NotNull String domain) throws IOException
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(filePath, "filePath");

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

    @NotNull
    @Undocumented
    public String uploadFilePrivateRaw(@NotNull String filePath, @NotNull String domain) throws IOException
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }

        Checks.notEmpty(filePath, "filePath");

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

    @NotNull
    @Undocumented
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
    public String getRawUserInfo(@NotNull String userData) throws IOException
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

    @NotNull
    @Undocumented
    public String getRawUserInfo(@NotNull String userData, @NotNull String sessionToken) throws IOException
    {
        Checks.notEmpty(userData, "userData");
        Checks.noWhitespace(userData, "userData");

        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

        if (userData.isEmpty())
        {
            throw new IllegalArgumentException("\"userData\" cannot be undefined.");
        }

        request = new Request.Builder()
                .url(BASE_URL + USERS_ENDPOINT + userData)
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
        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

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

    @NotNull
    @Undocumented
    public String addSubdomainRaw(@NotNull String domainName, @NotNull String sessionToken) throws IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

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

    @NotNull
    @Undocumented
    public String addCustomDomainRaw(@NotNull String domainName, @NotNull String sessionToken) throws IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

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

    @NotNull
    @Undocumented
    public String deleteDomainRaw(@NotNull String domainName, @NotNull String sessionToken) throws IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

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
        Checks.notEmpty(sessionToken, "sessionToken");
        Checks.noWhitespace(sessionToken, "sessionToken");

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

    @NotNull
    @Undocumented
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
