/**
 * Copyright 2022 Dominic (aka. BlockyDotJar)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.blocky.library.tixte.api;

import dev.blocky.library.tixte.api.exceptions.NotFound;
import dev.blocky.library.tixte.api.exceptions.TixteException;
import dev.blocky.library.tixte.internal.APIEndpoints;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TixteClient extends APIEndpoints
{
    private static final CloseableHttpClient connection = HttpClientBuilder.create().build();
    private final Logger logger = LoggerFactory.getLogger(TixteClient.class);
    private final StringBuffer responseContent = new StringBuffer();
    private final String apiKey;
    private static HttpResponse response;
    private String sessionToken;
    private String defaultDomain;
    private BufferedReader reader;
    private String line;

    protected TixteClient(@NotNull String apiKey, @Nullable String sessionToken, @Nullable String defaultDomain)
    {
        this.apiKey = apiKey;
        this.sessionToken = sessionToken;
        this.defaultDomain = defaultDomain;

        if (apiKey.isEmpty())
        {
            logger.error("\"apiKey\" cannot be undefined.", new TixteException());
        }
    }

    protected TixteClient(@NotNull String apiKey, @Nullable String sessionToken)
    {
        this.apiKey = apiKey;
        this.sessionToken = sessionToken;

        if (apiKey.isEmpty())
        {
            logger.error("\"apiKey\" cannot be undefined.", new TixteException());
        }
    }

    protected TixteClient(@NotNull String apiKey)
    {
        this.apiKey = apiKey;

        if (apiKey.isEmpty())
        {
            logger.error("\"apiKey\" cannot be undefined.", new TixteException());
        }
    }

    @NotNull
    public synchronized String getSize() throws IOException
    {
        try
        {
            HttpGet request = new HttpGet(BASE_URL + SIZE_ENDPOINT);
            request.addHeader("Authorization", apiKey);
            response = connection.execute(request);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String getUploads(int amount, int page) throws IOException {
        try
        {
            if (amount == 0 || page == 0)
            {
                logger.error("The amount/page can not be 0!", new IllegalArgumentException());
            }

            if (amount < 0 || page < 0)
            {
                logger.error("The amount/page can not be under 0!", new IllegalArgumentException());
            }

            HttpGet request = new HttpGet(BASE_URL + FILE_ENDPOINT + "?page=" + page + "&amount=" + amount);
            request.addHeader("Authorization", apiKey);
            response = connection.execute(request);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String getUserInfo() throws IOException
    {
        try
        {
            HttpGet request = new HttpGet(BASE_URL + ACCOUNT_ENDPOINT);
            request.addHeader("Authorization", apiKey);
            response = connection.execute(request);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String getUserInformationByName(@NotNull String user) throws IOException
    {
        try
        {
            if (user.isEmpty())
            {
                logger.error("\"user\" cannot be undefined.", new TixteException());
            }

            HttpGet request = new HttpGet(BASE_URL + "/users/" + user);
            request.addHeader("Authorization", sessionToken);
            response = connection.execute(request);

            if (response.getStatusLine().getStatusCode() == 404)
            {
                logger.error("User " + user + " was not found.", new NotFound());
            }

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String getUserInformationByName(@NotNull String sessionToken, @NotNull String user) throws IOException
    {
        try
        {
            if (sessionToken.isEmpty())
            {
                logger.error("\"sessionToken\" cannot be undefined.", new TixteException());
            }

            if (user.isEmpty())
            {
                logger.error("\"user\" cannot be undefined.", new TixteException());
            }

            HttpGet request = new HttpGet(BASE_URL + "/users/" + user);
            request.addHeader("Authorization", sessionToken);
            response = connection.execute(request);

            if (response.getStatusLine().getStatusCode() == 404)
            {
                logger.error("User " + user + " was not found.", new NotFound());
            }

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String getUserDomains() throws IOException
    {
        try
        {
            HttpGet request = new HttpGet(BASE_URL + DOMAINS_ENDPOINT);
            request.addHeader("Authorization", sessionToken);
            response = connection.execute(request);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String getUserDomains(@NotNull String sessionToken) throws IOException
    {
        try
        {
            if (sessionToken.isEmpty())
            {
                logger.error("\"sessionToken\" cannot be undefined.", new TixteException());
            }

            HttpGet request = new HttpGet(BASE_URL + DOMAINS_ENDPOINT);
            request.addHeader("Authorization", sessionToken);
            response = connection.execute(request);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String uploadFile(@NotNull String filePath) throws IOException
    {
        try
        {
            File file = new File(filePath);

            if (!file.exists())
            {
                logger.error("File " + file.getName() + " was not found.", new NotFound());
            }

            HttpEntity entity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setBoundary("===" + System.currentTimeMillis() + "===")
                    .setCharset(StandardCharsets.UTF_8)
                    .setContentType(
                            ContentType.MULTIPART_FORM_DATA)
                    .addPart(file.getName(),
                            new FileBody(file))
                    .build();
            HttpPost request = new HttpPost(BASE_URL + UPLOAD_ENDPOINT);
            request.addHeader("domain", defaultDomain);
            request.addHeader("Authorization", apiKey);
            request.setEntity(entity);
            response = connection.execute(request);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String uploadFile(@NotNull String imagePath, @NotNull String domain) throws IOException
    {
        try
        {
            File file = new File(imagePath);

            if (!file.exists())
            {
                logger.error("File " + file.getName() + " was not found.", new NotFound());
            }

            if (domain.isEmpty())
            {
                logger.error("\"domain\" cannot be undefined.", new TixteException());
            }

            HttpEntity entity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setBoundary("===" + System.currentTimeMillis() + "===")
                    .setCharset(StandardCharsets.UTF_8)
                    .setContentType(
                            ContentType.MULTIPART_FORM_DATA)
                    .addPart(file.getName(),
                            new FileBody(file))
                    .build();
            HttpPost request = new HttpPost(BASE_URL + UPLOAD_ENDPOINT);
            request.addHeader("domain", domain);
            request.addHeader("Authorization", apiKey);
            request.setEntity(entity);
            response = connection.execute(request);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public synchronized String deleteFile(@NotNull String fileId) throws IOException
    {
        try
        {
            if (fileId.isEmpty())
            {
                logger.error("\"fileId\" cannot be undefined.", new TixteException());
            }

            HttpDelete request = new HttpDelete(BASE_URL + FILE_ENDPOINT + "/" + fileId);
            request.addHeader("Authorization", apiKey);
            response = connection.execute(request);

            if (response.getStatusLine().getStatusCode() == 404)
            {
                logger.error("File with Id " + fileId + " was not found.", new NotFound());
            }

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = reader.readLine()) != null)
            {
                responseContent.append(line);
            }

            reader.close();
        }
        finally
        {
            connection.close();
        }
        return responseContent.toString();
    }

    @NotNull
    public String getAPIKey()
    {
        return apiKey;
    }

    @Nullable
    public String getDefaultDomain()
    {
        return defaultDomain;
    }

    @Nullable
    public String getSessionToken()
    {
        return sessionToken;
    }
}
