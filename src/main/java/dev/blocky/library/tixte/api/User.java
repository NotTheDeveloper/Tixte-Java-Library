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

import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import com.google.errorprone.annotations.CheckReturnValue;
import java.io.IOException;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * Represents a Tixte user-account.
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-beta.1
 */
public class User
{
    private final String userData;

    /**
     * Instantiates a <b>new</b> user.
     *
     * @param userData A user-id oder user-name.
     */
    User(@NotNull String userData)
    {
        Checks.notEmpty(userData, "userData");
        Checks.noWhitespace(userData, "userData");

        this.userData = userData;
    }

    /**
     * Gets the count of all enabled flags of the specific user.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The count of all enabled flags.
     */
    public int getFlagCount() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getUserInfoRaw(userData));
        JSONObject data = json.getJSONObject("data");

        return data.getInt("flags");
    }

    /**
     * Gets the id of the specific user.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The id of the specific user.
     */
    @Nullable
    @CheckReturnValue
    public String getId() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getUserInfoRaw(userData));
        JSONObject data = json.getJSONObject("data");

        return data.getString("id");
    }

    /**
     * Gets the avatar id of the specific user.
     * <br>This returns an empty string if there is no avatar given.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The avatar id of the specific user.
     */
    @Nullable
    @CheckReturnValue
    public String getAvatarId() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getUserInfoRaw(userData));
        JSONObject data = json.getJSONObject("data");

        return data.isNull("avatar") ? "": data.getString("avatar");
    }

    /**
     * Gets the username of the specific user.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The username of the specific user.
     */
    @Nullable
    @CheckReturnValue
    public String getUsername() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getUserInfoRaw(userData));
        JSONObject data = json.getJSONObject("data");

        return data.getString("username");
    }
}

