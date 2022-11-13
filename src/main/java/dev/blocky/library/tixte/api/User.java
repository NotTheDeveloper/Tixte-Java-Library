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

import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.requests.json.DataPath;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Represents a Tixte user-account.
 *
 * @param userData A specific user-name or id.
 *
 * @author BlockyDotJar
 * @version v1.4.1
 * @since v1.0.0-beta.1
 */
public record User(@NotNull String userData) implements RawResponseData
{

    /**
     * Gets the count of all enabled flags of the specific user.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of all enabled flags.
     */
    public int getFlagCount() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw(userData).resultNow());
        return DataPath.getInt(json, "data.flags");
    }

    /**
     * Gets the id of the specific user.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The id of the specific user.
     */
    @NotNull
    public String getId() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw(userData).resultNow());
        return DataPath.getString(json, "data.id");
    }

    /**
     * Gets the avatar id of the specific user.
     * <br>This returns an empty string if there is no avatar given.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The avatar id of the specific user.
     */
    @NotNull
    public String getAvatarId() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw(userData).resultNow());
        return DataPath.getString(json, "data.avatar?");
    }

    /**
     * Gets the username of the specific user.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The username of the specific user.
     */
    @NotNull
    public String getUsername() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw(userData).resultNow());
        return DataPath.getString(json, "data.username");
    }
}

