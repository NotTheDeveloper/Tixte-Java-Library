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

import dev.blocky.library.tixte.api.MyFiles;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Some basic examples, how to upload a {@link File} to Tixte.
 *
 * @author BlockyDotJar
 * @version v1.1.2
 * @since v1.0.0-beta.3
 */
public class FileExample
{
    /**
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Upload a file to Tixte by initializing a <b>new</b> {@link File}.
     */
    @NotNull
    public static MyFiles uploadFile() throws InterruptedException, IOException
    {
        MyFiles myFiles = new MyFiles();

        // Creates a *new* File object.
        // If you initialize a file, which doesn't exist, this will throw an FileNotFoundException, an NoSuchFileException and an IOException.
        File file = new File("YOUR_VALID_FILE_PATH");

        // This uses the default domain, which you can initialize with TixteClientBuilder#setDefaultDomain(@Nullable String).
        // If you haven't initialized the default domain, this will throw an exception.
        // If you don't want to use the default domain, you can use uploadFile(@NotNull File, @NotNull String) instead.
        // You can also use uploadFile(@NotNull String) if you want to initialize a String to create a file.
        // If you want to upload a file, that only you can see/user you can use uploadPrivateFile(@NotNull File) instead.
        return myFiles.uploadFile(file);
    }

    /**
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Upload a file from a specific {@link URL} to Tixte by initializing a <b>new</b> {@link File}.
     */
    public static MyFiles uploadFileFromURL() throws InterruptedException, IOException
    {
        MyFiles myFiles = new MyFiles();

        // Creates a *new* File object.
        // Here you set the path, where the file should be created, but also the name of the file.
        File file = new File("YOUR_VALID_FILE_PATH");
        // This is the url, from where the file will get downloaded.
        URL url = new URL("YOUR_VALID_URL");

        // Starts the downloading process.
        FileUtils.copyURLToFile(url, file);

        // After the file got downloaded, it will be uploaded to Tixte.
        // This uses the default domain, which you can initialize with TixteClientBuilder#setDefaultDomain(@Nullable String).
        // If you haven't initialized the default domain, this will throw an exception.
        // If you don't want to use the default domain, you can use uploadFile(@NotNull File, @NotNull String) instead.
        // You can also use uploadFile(@NotNull String) if you want to initialize a String to create a file.
        // If you want to upload a file, that only you can see/user you can use uploadPrivateFile(@NotNull File) instead.
        return myFiles.uploadFile(file);
    }

    /**
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Delete any kind of file from your Tixte dashboard.
     */
    @NotNull
    public static MyFiles deleteFile() throws InterruptedException, IOException
    {
        MyFiles myFiles = new MyFiles();

        // This could throw an HTTPException if there is no file to purge, but I am not sure about that.
        // Also note that this could throw an exception if the file takes too long to delete or if the of the fileId is invalid.
        // This will delete the file with an index of 0 (The newest file).
        return myFiles.deleteFile(myFiles.getAssetIds().get(0));
    }

    /**
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Deletes every file from your Tixte dashboard.
     */
    @NotNull
    public static MyFiles deleteAllFiles() throws InterruptedException, IOException
    {
        MyFiles myFiles = new MyFiles();

        // You must set a password (for some reason) to execute this request, because otherwise it won't work.
        // This could throw an HTTPException if there is no file to purge, but I am not sure about that.
        // But this will definitely throw an HTTPException if the password is wrong.
        return myFiles.purgeFiles("YourVerySecurePassword1234!");
    }
}
