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
import dev.blocky.library.tixte.api.entities.SelfUser;
import dev.blocky.library.tixte.internal.RawResponseData;
import dev.blocky.library.tixte.internal.requests.json.DataArray;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Represents the 'My Files' tab of the Tixte dashboard and everything else what Tixte offers you with files.
 *
 * @author BlockyDotJar
 * @version v1.3.0
 * @since v1.0.0-alpha.1
 */
public class MyFiles extends RawResponseData
{
    private String url, directURL, deletionURL;

    MyFiles() { }

    /**
     * Gets the current used file size in bytes.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current used file size in bytes.
     */
    public long getUsedSize() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getSizeRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("used");
    }

    /**
     * Gets the current limit of storage in bytes.
     * <br><br>You can have a storage of 15.0 GB without a Tixte turbo/turbo-charged subscription.
     * <br>You can have a storage of 200.0 GB with a Tixte turbo subscription and with a Tixte turbo-charged subscription
     * you can have up to 500.0 GB storage.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current limit of storage in bytes.
     */
    public long getLimit() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getSizeRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("limit");
    }


    /**
     * Gets the current remaining storage size in bytes.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see #getLimit()
     * @see #getUsedSize()
     *
     * @return The current remaining storage size in bytes.
     */
    public long getRemainingSize() throws ExecutionException, InterruptedException
    {
        return getLimit() - getUsedSize();
    }

    /**
     * Gets your current premium tier as an integer.
     * <br><br>0 = No subscription.
     * <br>1 = Tixte turbo subscription.
     * <br>2 = Tixte turbo-charged subscription.
     * <br><br>If you don't want to make your own method for checking, if the user has a subscription you can use
     * {@link SelfUser#hasTixteSubscription()} instead.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your current premium tier as an integer.
     */
    public int getPremiumTier() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getSizeRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("premium_tier");
    }

    /**
     * Gets your current upload count.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your current upload count.
     */
    public int getTotalUploadCount() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("total");
    }

    /**
     * Gets your current upload count on a specific page.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your current upload count on a specific page.
     */
    public int getResults() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("results");
    }

    /**
     * Gets a {@link List} of permission level, which the files are containing.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of permission level, which the files are containing.
     */
    public List<Integer> getPermissionLevels() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getInt("permission_level"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of extensions from the files.
     * <br><br>An extension is the file type.
     * <br>For example: "png" or "jpg".
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of extensions from the files.
     */
    @NotNull
    public List<String> getExtensions() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("extension"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of sizes from the files in bytes.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of sizes from the files in bytes.
     */
    public List<Integer> getSizes() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getInt("size"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of upload dates from the files as a ISO string.
     * <br><br>Example for ISO string: <b>2022-07-08T11:32:51.913Z</b>.
     * <br>There is an <a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/java/DateFormatExample.java">example</a>
     * for formatting the ISO string.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of upload dates from the files as a ISO string.
     */
    @NotNull
    public List<String> getUploadDates() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("uploaded_at"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of domains, on which the files got uploaded.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of domains, on which the files got uploaded.
     */
    @NotNull
    public List<String> getDomains() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("domain"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of names from the files.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of names from the files.
     */
    @NotNull
    public List<String> getNames() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("name"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of mime-types from the files.
     * <br><br>An mime-type is a string that describes the type of the file.
     * <br>An example of a mime-type is: <b>image/png</b>.
     * <br>An extension is the file type.
     * <br>For example: "png" or "jpg".
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of mime-types from the files.
     */
    @NotNull
    public List<String> getMimeTypes() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("mimetype"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of expiration times from the file as a ISO string.
     * <br><br>Example for ISO string: <b>2022-07-08T11:32:51.913Z</b>
     * <br>There is an <a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/java/DateFormatExample.java">example</a>
     * for formatting the ISO string.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of expiration times from the files as a ISO string.
     */
    @Nullable
    @CheckReturnValue
    public List<Object> getExpirationTimes() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<Object> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            if (array.getDataObject(i).isNull("expiration"))
            {
                list.add(0);
            }
            else
            {
                list.add(array.getDataObject(i).get("expiration"));
            }
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of IDs from the files.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} od IDs from the files.
     */
    @NotNull
    public List<String> getAssetIds() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("asset_id"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of types from the files. (public/private)
     * <br><br>1 = public file
     * <br>2 = private file
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of types from the files. (public/private)
     */
    public List<Integer> getTypes() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUploadsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("uploads");

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getInt("type"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Checks if the specified file is public or not.
     *
     * @param index The file at the specific index.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *      
     * @see #getTypes()
     *
     * @return <b>true</b> - If the file is public.
     *         <br><b>false</b> - If the file is private.
     */
    public boolean isPublic(int index) throws ExecutionException, InterruptedException
    {
        Checks.notNegative(index, "index");

        return getTypes().get(index) == 1;
    }

    /**
     * Checks if the specified file is private or not.
     *
     * @param index The file at the specific index.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *      
     * @see #getTypes()
     *
     * @return <b>true</b> - If the file is private.
     *         <br><b>false</b> - If the file is public.
     */
    public boolean isPrivate(int index) throws ExecutionException, InterruptedException
    {
        Checks.notNegative(index, "index");

        return getTypes().get(index) == 2;
    }

    /**
     * Gets the complete name of the file.
     * <br><br>This is composed of the name of the file and the extension.
     * <br>An extension is the file type.
     * <br>For example: "png" or "jpg".
     * <br><br>index 0 = the newest file
     * <br>index 1 = the second-newest file
     * <br>And so on.
     *
     * @param index The file at the specific index.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *      
     * @see #getNames()
     * @see #getExtensions()
     *
     * @return The complete name of the file.
     */
    @NotNull
    public String getFileName(int index) throws ExecutionException, InterruptedException
    {
        return getNames().get(index) + "." + getExtensions().get(index);
    }

    /**
     * Gets the current upload-region, which you are using.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current upload-region, which you are using.
     */
    @NotNull
    public String getUploadRegion() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("upload_region");
    }

    /**
     * Gets the url of the current uploaded file.
     * <br><br>Note that you should only use this method after the file upload method has been called.
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
     * <br><br>Note that you should only use this method after the file upload method has been called.
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
     * <br><br>Note that you should only use this method after the file upload method has been called.
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
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadFile(File, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadFile(File, String)} instead.
     *
     * @param file The file to be uploaded.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull File file)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadFileRaw(file).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link File}.
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadPrivateFile(File, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadPrivateFile(File, String)} instead.
     *
     * @param file The file to be uploaded.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull File file)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadPrivateFileRaw(file).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Upload a file to Tixte by initializing a <b>new</b> {@link File} and a domain.
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadFile(File)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull File file, @NotNull String domain)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadFileRaw(file, domain).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link File} and a domain.
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadPrivateFile(File)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param file The file to be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull File file, @NotNull String domain)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadPrivateFileRaw(file, domain).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Upload a file to Tixte by initializing a <b>new</b> {@link String}.
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadFile(String, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadFile(String, String)} instead.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull String filePath)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadFileRaw(filePath).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link String}.
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses {@link TixteClient#getDefaultDomain() the default domain} to upload the file.
     * <br>If you don't initialize the client with a domain, you should use {@link #uploadPrivateFile(String, String)} instead, or
     * you initialize the client with a domain by calling {@link TixteClientBuilder#setDefaultDomain(String)}.
     * <br>But if you don't want to use the default domain, you can use another domain by calling
     * {@link #uploadPrivateFile(String, String)} instead.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull String filePath)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadPrivateFileRaw(filePath).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Upload a file to Tixte by initializing a <b>new</b> {@link String} and a domain.
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadFile(String)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadFile(@NotNull String filePath, @NotNull String domain)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadFileRaw(filePath, domain).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Upload a private file to Tixte by initializing a <b>new</b> {@link String} and a domain.
     * <br><br>Note that you can only upload one file at a time and that this file cannot be greater than the maximum storage.
     * <br>You can check the maximum storage by calling {@link #getLimit()} and you can check the remaining storage by
     * calling {@link #getRemainingSize()}.
     * <br><br>This method will throw an exception if the file is too big or if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to upload or if the name of the file
     * is invalid.
     * <br><br>This method uses a specific domain to upload the file.
     * <br>If you want to use the default domain, you don't have to call this method, you can call
     * {@link #uploadPrivateFile(String)} instead.
     * <br>Note that you must have initialized the client with a default domain before you can use this method.
     *
     * @param filePath The string to initialize the file, which should be uploaded.
     * @param domain The domain to upload the file to.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles uploadPrivateFile(@NotNull String filePath, @NotNull String domain)
    {
        try
        {
            DataObject json = DataObject.fromJson(uploadPrivateFileRaw(filePath, domain).get());
            DataObject data = json.getDataObject("data");

            url = data.getString("url");
            directURL = data.getString("direct_url");
            deletionURL = data.getString("deletion_url");
        }
        catch (ExecutionException | InterruptedException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Delete any kind of file from your Tixte dashboard.
     * <br><br>You can get the file's ID by calling {@link #getAssetIds()} or you can directly get
     * {@link #getDeletionURL() the deletion url}.
     * <br> <br>This method will throw an exception if the file was not found/doesn't exist.
     * <br>Also note that this could throw an exception if the file takes too long to delete or if the of the fileId
     * is invalid.
     * <br>if you want to delete every file in your Tixte dashboard, you can call {@link #purgeFiles(String)} instead.
     *
     * @param fileId The ID of the file as a string.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles deleteFile(@NotNull String fileId) 
    {
        deleteFileRaw(fileId);
        return this;
    }

    /**
     * Deletes every file from your Tixte dashboard.
     * <br><br>You must set a password for this request, because otherwise it won't work.
     * <br><br>Also note that this could throw an exception if the file takes too long to delete.
     * <br>if you want to delete a single file in your Tixte dashboard, you can call {@link #deleteFile(String)} instead.
     *
     * @param password The password of your Tixte account.
     *
     * @return The current instance of this class.
     */
    @NotNull
    public MyFiles purgeFiles(@NotNull String password) 
    {
        purgeFilesRaw(password);
        return this;
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

        MyFiles myFiles = (MyFiles) o;

        return Objects.equals(url, myFiles.url) && Objects.equals(directURL, myFiles.directURL) &&
                Objects.equals(deletionURL, myFiles.deletionURL);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(new MyFiles());
    }

    @NotNull
    @Override
    public String toString()
    {
        try
        {
            return "MyFiles{" +
                    "used=" + getUsedSize() + ", " +
                    "limit=" + getLimit() + ", " +
                    "remaining=" + getRemainingSize() + ", " +
                    "premium_tier=" + getPremiumTier() + ", " +
                    "total=" + getTotalUploadCount() + ", " +
                    "url='" + url + "', " +
                    "directURL='" + directURL + "', " +
                    "deletionURL='" + deletionURL + '\'' +
                    '}';
        }
        catch (ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}