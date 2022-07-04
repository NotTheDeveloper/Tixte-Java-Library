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
package dev.blocky.library.tixte.api.entities;

import dev.blocky.library.tixte.annotations.Undocumented;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import javax.annotation.CheckReturnValue;
import java.io.IOException;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * Represents a Tixte user-account.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.1
 */
public class User
{
    private final transient String userData;

    @Undocumented
    public User(@NotNull String userData)
    {
        Checks.notEmpty(userData, "userData");
        Checks.noWhitespace(userData, "userData");

        this.userData = userData;
    }

    @Undocumented
    public int getFlagCount() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData));
        JSONObject data = json.getJSONObject("data");

        return data.getInt("flags");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getId() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData));
        JSONObject data = json.getJSONObject("data");

        return data.getString("id");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getAvatarId() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData));
        JSONObject data = json.getJSONObject("data");

        return data.isNull("avatar") ? "": data.getString("avatar");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getUsername() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData));
        JSONObject data = json.getJSONObject("data");

        return data.getString("username");
    }

    @Undocumented
    @CheckReturnValue
    public int getFlagCount(@NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData, sessionToken));
        JSONObject data = json.getJSONObject("data");

        return data.getInt("flags");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getId(@NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData, sessionToken));
        JSONObject data = json.getJSONObject("data");

        return data.getString("id");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getAvatarId(@NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData, sessionToken));
        JSONObject data = json.getJSONObject("data");

        return data.isNull("avatar") ? "": data.getString("avatar");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getUsername(@NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo(userData, sessionToken));
        JSONObject data = json.getJSONObject("data");

        return data.getString("username");
    }
}

