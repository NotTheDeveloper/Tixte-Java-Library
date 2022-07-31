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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.MyFiles;
import dev.blocky.library.tixte.api.TixteClient;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Some basic examples, how to upload a {@link File}.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.3
 */
public class FileExample
{
    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Upload a file to Tixte by initializing a <b>new</b> {@link File}.
     */
    @NotNull
    @CheckReturnValue
    public static MyFiles uploadFile() throws IOException
    {
        TixteClient client = BasicTixteClientExample.getTixteClient();
        MyFiles myFiles = client.getFileSystem();

        // Creates a new file object.
        // If you initialize a file, which doesn't exist, this will throw an FileNotFoundException, an NoSuchFileException and an IOException.
        File file = new File("src/test/resources/tixte-logo.png");

        // This uses the default domain, which you can initialize with TixteClientBuilder#setDefaultDomain(@Nullable String).
        // If you haven't initialized the default domain, this will throw an exception.
        // If you don't want to use the default domain, you can use uploadFile(@NotNull File, @NotNull String) instead.
        // You can also use uploadFile(@NotNull URI) if you want to initialize a URI to create a file or uploadFile(@NotNull String) if you want to initialize a String to create a file.
        // If you want to upload a file, that only you can see/user you can use uploadPrivateFile(@NotNull File) instead.
        return myFiles.uploadFile(file);
    }

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Delete any kind of file from your Tixte dashboard.
     */
    @NotNull
    @CheckReturnValue
    public static MyFiles deleteFile() throws IOException
    {
        TixteClient client = BasicTixteClientExample.getTixteClient();
        MyFiles myFiles = client.getFileSystem();

        // This could throw a HTTPException if there is no file to purge, but I am not sure about that.
        // This will throw an HTTPException if the password is wrong.
        // This will delete the file with an index of 0 (This is the newest file).
        return myFiles.deleteFile(myFiles.getAssetId(0, 0 ));
    }

    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Deletes every file from your Tixte dashboard.
     */
    @NotNull
    @CheckReturnValue
    public static MyFiles deleteAllFiles() throws IOException
    {
        TixteClient client = BasicTixteClientExample.getTixteClient();
        MyFiles myFiles = client.getFileSystem();

        // You must set a password (for some reason) for this request, because otherwise it won't work.
        // This could throw a HTTPException if there is no file to purge, but I am not sure about that.
        // This will throw an HTTPException if the password is wrong.
        return myFiles.purgeFiles("YourVerySecurePassword1234!");
    }
}
