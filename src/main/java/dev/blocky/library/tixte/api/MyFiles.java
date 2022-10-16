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
import dev.blocky.library.tixte.internal.requests.json.DataArray;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.requests.json.DataPath;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Represents the 'My Files' tab of the Tixte dashboard and everything else what Tixte offers you with files.
 *
 * @author BlockyDotJar
 * @version v1.5.0
 * @since v1.0.0-alpha.1
 */
public record MyFiles() implements RawResponseData
{
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("^(?!.*https?://)([a-zA-Z\\d_-])+.([a-zA-Z-])+.([a-zA-Z])+$", Pattern.CASE_INSENSITIVE);
    private static final SearchBar searchBar = new SearchBar();
    private static final Folders folders = new Folders();
    private static String url, directURL, deletionURL;

    /**
     * Gets the current used file size in bytes.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current used file size in bytes.
     */
    public long getUsedSize() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getSizeRaw().resultNow());
        return DataPath.getInt(json, "data.used");
    }

    /**
     * Gets the current limit of storage in bytes.
     *
     * <p>You can have a storage of 15.0 GB without a Tixte turbo/turbo-charged subscription.
     * <br>You can have a storage of 200.0 GB with a Tixte turbo subscription and with a Tixte turbo-charged subscription
     * you can have up to 500.0 GB storage.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current limit of storage in bytes.
     */
    public long getLimit() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getSizeRaw().resultNow());
        return DataPath.getInt(json, "data.limit");
    }


    /**
     * Gets the current remaining storage size in bytes.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see #getLimit()
     * @see #getUsedSize()
     *
     * @return The current remaining storage size in bytes.
     */
    public long getRemainingSize() throws InterruptedException, IOException
    {
        return getLimit() - getUsedSize();
    }

    /**
     * Gets your current premium tier as an integer.
     *
     * <p>0 = No subscription.
     * <br>1 = Tixte turbo subscription.
     * <br>2 = Tixte turbo-charged subscription.
     *
     * <p>If you don't want to make your own method for checking, if the user has a subscription you can use
     * {@link SelfUser#hasTixteSubscription()} instead.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your current premium tier as an integer.
     */
    public int getPremiumTier() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getSizeRaw().resultNow());
        return DataPath.getInt(json, "data.premium_tier");
    }

    /**
     * Gets your current upload count.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your current upload count.
     */
    public int getTotalUploadCount() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        return DataPath.getInt(json, "data.total");
    }

    /**
     * Gets your current upload count on a specific page.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your current upload count on a specific page.
     */
    public int getResults() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        return DataPath.getInt(json, "data.results");
    }

    /**
     * Gets a {@link List} of permission level, which the files are containing.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of permission level, which the files are containing.
     */
    @Nullable
    @CheckReturnValue
    public List<Integer> getPermissionLevels() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getInt(json, "data.uploads[" + i + "]?.permission_level"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of extensions from the files.
     *
     * <p>An extension is the file type.
     * <br>For example: "png" or "jpg".
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of extensions from the files.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getExtensions() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.uploads[" + i + "]?.extension"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of sizes from the files in bytes.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of sizes from the files in bytes.
     */
    @Nullable
    @CheckReturnValue
    public List<Integer> getSizes() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getInt(json, "data.uploads[" + i + "]?.size"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of upload dates from the files as a ISO string.
     *
     * <p>Example for ISO string: <b>2022-07-08T11:32:51.913Z</b>.
     * <br>There is an <a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/java/DateFormatExample.java">example</a>
     * for formatting the ISO string.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of upload dates from the files as a ISO string.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getUploadDates() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.uploads[" + i + "]?.uploaded_at"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of domains, on which the files got uploaded.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of domains, on which the files got uploaded.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getDomains() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.uploads[" + i + "]?.domain"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of names from the files.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of names from the files.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getNames() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.uploads[" + i + "]?.name"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of mime-types from the files.
     *
     * <p>An mime-type is a string that describes the type of the file.
     * <br>An example of a mime-type is: <b>image/png</b>.
     * <br>An extension is the file type.
     * <br>For example: "png" or "jpg".
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of mime-types from the files.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getMimeTypes() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.uploads[" + i + "]?.mimetype"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of expiration times from the file as a ISO string.
     *
     * <p>Example for ISO string: <b>2022-07-08T11:32:51.913Z</b>
     * <br>There is an <a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/java/DateFormatExample.java">example</a>
     * for formatting the ISO string.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of expiration times from the files as a ISO string.
     */
    @Nullable
    @CheckReturnValue
    public List<Object> getExpirationTimes() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<Object> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.uploads[" + i + "]?.expiration?"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of IDs from the files.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} od IDs from the files.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getAssetIds() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.uploads[" + i + "]?.asset_id"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of types from the files. (public/private)
     *
     * <p>1 = public file
     * <br>2 = private file
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of types from the files. (public/private)
     */
    @Nullable
    @CheckReturnValue
    public List<Integer> getTypes() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(DataPath.getInt(json, "data.uploads[" + i + "]?.type"));
        }
        return list;
    }

    /**
     * Checks if the specified file is public or not.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see #getTypes()
     *
     * @return <b>true</b> - If the file is public.
     *         <br><b>false</b> - If the file is private.
     */
    @Nullable
    @CheckReturnValue
    public List<Boolean> isPublic() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<Boolean> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(getTypes().get(i) == 1);
        }
        return list;
    }

    /**
     * Checks if the specified file is private or not.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see #getTypes()
     *
     * @return <b>true</b> - If the file is private.
     *         <br><b>false</b> - If the file is public.
     */
    @Nullable
    @CheckReturnValue
    public List<Boolean> isPrivate() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<Boolean> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(getTypes().get(i) == 2);
        }
        return list;
    }

    /**
     * Gets the complete name of the file.
     *
     * <p>This is composed of the name of the file and the extension.
     * <br>An extension is the file type.
     * <br>For example: "png" or "jpg".
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see #getNames()
     * @see #getExtensions()
     *
     * @return The complete name of the file.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getFileNames() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUploadsRaw().resultNow());
        DataArray uploads = DataPath.getDataArray(json, "data.uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < uploads.toList().size(); i++)
        {
            list.add(getNames().get(i) + "." + getExtensions().get(i));
        }
        return list;
    }

    /**
     * Gets the current upload-region, which you are using.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current upload-region, which you are using.
     */
    @NotNull
    public String getUploadRegion() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.upload_region");
    }

    /**
     * Gets the url of the current uploaded file.
     *
     * <p>Note that you should only use this method after the file upload method has been called.
     *
     * @return The url of the current uploaded file.
     */
    @Nullable
    @CheckReturnValue
    public Optional<String> getURL()
    {
        return Optional.ofNullable(url);
    }

    /**
     * Gets the direct-url of the current uploaded file.
     *
     * <p>Note that you should only use this method after the file upload method has been called.
     *
     * @return The direct-url of the current uploaded file.
     */
    @Nullable
    @CheckReturnValue
    public Optional<String> getDirectURL()
    {
        return Optional.ofNullable(directURL);
    }

    /**
     * Gets the deletion-url of the current uploaded file.
     *
     * <p>Note that you should only use this method after the file upload method has been called.
     *
     * @return The deletion-url of the current uploaded file.
     */
    @Nullable
    @CheckReturnValue
    public Optional<String> getDeletionURL()
    {
        return Optional.ofNullable(deletionURL);
    }

    /**
     * Upload a file to Tixte by initializing a <b>new</b> {@link File}.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadFile(File, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadFile(File, String)} instead.
     *
     * @param file The file to be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull File file) throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.uploadFileRaw(file).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link File}.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadPrivateFile(File, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadPrivateFile(File, String)} instead.
     *
     * @param file The file to be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull File file) throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.uploadPrivateFileRaw(file).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Upload a file to Tixte by initializing a <b>new</b> {@link File} and a domain.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadFile(File)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull File file, @NotNull String domain) throws InterruptedException, IOException
    {
        domainCheck(domain);

        DataObject json = DataObject.fromJson(RawResponseData.uploadFileRaw(file, domain).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link File} and a domain.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadPrivateFile(File)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull File file, @NotNull String domain) throws InterruptedException, IOException
    {
        domainCheck(domain);

        DataObject json = DataObject.fromJson(RawResponseData.uploadPrivateFileRaw(file, domain).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Upload a file to Tixte by initializing a <b>new</b> {@link String}.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadFile(String, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadFile(String, String)} instead.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull String filePath) throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.uploadFileRaw(filePath).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link String}.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadPrivateFile(String, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadPrivateFile(String, String)} instead.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull String filePath) throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.uploadPrivateFileRaw(filePath).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Upload a file to Tixte by initializing a <b>new</b> {@link String} and a domain.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadFile(String)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull String filePath, @NotNull String domain) throws InterruptedException, IOException
    {
        domainCheck(domain);

        DataObject json = DataObject.fromJson(RawResponseData.uploadFileRaw(filePath, domain).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link String} and a domain.
     *
     * <p>Note that this file cannot be greater than 200MB, without a turbo/turbo-charged subscription.
     * <br>With a turbo subscription you can upload files as big as 4GB and with a turbo-charged subscription you
     * can upload files up to 10GB.
     *
     * <p>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     *
     * <p>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadPrivateFile(String)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull String filePath, @NotNull String domain) throws InterruptedException, IOException
    {
        domainCheck(domain);

        DataObject json = DataObject.fromJson(RawResponseData.uploadPrivateFileRaw(filePath, domain).resultNow());

        url = DataPath.getString(json, "data.url");
        directURL = DataPath.getString(json, "data.direct_url");
        deletionURL = DataPath.getString(json, "data.deletion_url");
        return this;
    }

    /**
     * Delete any kind of file from your Tixte dashboard.
     *
     * <p>You can get the file's ID by calling {@link #getAssetIds()} or you can directly get
     * {@link #getDeletionURL() the deletion url}.
     * <br> <br>This method will throw an exception if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to delete or if the of the fileId
     * is invalid.
     * <br>if you want to delete every file in your Tixte dashboard, you can call {@link #purgeFiles(String)} instead.
     *
     * @param fileId The ID of the file as a string.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles deleteFile(@NotNull String fileId) throws InterruptedException, IOException
    {
        RawResponseData.deleteFileRaw(fileId);
        return this;
    }

    /**
     * Deletes every file from your Tixte dashboard.
     *
     * <p>You must set a password for this request, because otherwise it won't work.
     *
     * <p>Also note that this could throw an exception if the file takes too long to delete.
     * <br>if you want to delete a single file in your Tixte dashboard, you can call {@link #deleteFile(String)} instead.
     *
     * @param password The password of your Tixte account.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles purgeFiles(@NotNull String password) throws InterruptedException, IOException
    {
        RawResponseData.purgeFilesRaw(password);
        return this;
    }

    /**
     * Gets every folder of the 'My Files' tab.
     * <br>Getting this at the moment is pretty useless because the actual record doesn't have something in it.
     *
     * @return The current instance of the {@link Folders} subclass.
     */
    @NotNull
    @Experimental
    public Folders getFolders()
    {
        return folders;
    }

    /**
     * Gets the search bar of the 'My Files' tab.
     * <br>Getting this at the moment is pretty useless because the actual record doesn't have something in it.
     *
     * @return The current instance of the {@link SearchBar} subclass.
     */
    @NotNull
    @Experimental
    public SearchBar getSearchBar()
    {
        return searchBar;
    }

    private void domainCheck(@Nullable String domain)
    {
        if (domain != null)
        {
            Checks.check(DOMAIN_PATTERN.matcher(domain).matches(), "Regex doesn't match with your specified domain. Please check if you specified a valid domain.");
        }
    }

    /**
     * Represents every folder of the 'My Files' tab.
     * <br>Because this is an experimental feature and I don't have access to it, I can't implement it already.
     *
     * @author BlockyDotJar
     * @version v1.0.0-alpha.1
     * @since v1.0.2
     */
    @Experimental
    @SuppressWarnings("unused")
    public record Folders()
    {
        // Can't implement because I don't have access to it.
    }

    /**
     * Represents the search bar of the 'My Files' tab.
     * <br>Because this is an experimental feature and I don't have access to it, I can't implement it already.
     *
     * @author BlockyDotJar
     * @version v1.0.0-alpha.1
     * @since v1.0.2
     */
    @Experimental
    @SuppressWarnings("unused")
    public record SearchBar()
    {
        // Can't implement because I don't have access to it.
    }
}
