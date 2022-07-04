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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import javax.annotation.CheckReturnValue;
import java.io.IOException;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * Represents your Tixte user-account.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.1
 */
public class SelfUser
{

    @Undocumented
    public SelfUser()
    {
    }

    @Undocumented
    public boolean isEmailVerified() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getBoolean("email_verified");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getPhoneNumber() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.isNull("phone") ? "": data.getString("phone");
    }

    @NotNull
    @Undocumented
    public String getLastLogin() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getString("last_login");
    }

    @Undocumented
    public int getFlagCount() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getInt("flags");
    }

    @Undocumented
    public int getPremiumTier() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getInt("premium_tier");
    }

    @Undocumented
    public boolean hasMFAEnabled() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getBoolean("mfa_enabled");
    }

    @NotNull
    @Undocumented
    public String getId() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getString("id");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getAvatarId() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.isNull("avatar") ? "": data.getString("avatar");
    }

    @NotNull
    @Undocumented
    public String getUploadRegion() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getString("upload_region");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getEmail() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.isNull("email") ? "": data.getString("email");
    }

    @NotNull
    @Undocumented
    public String getUsername() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getString("username");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getAPIKeyBySessionToken() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawAPIKeyBySessionToken());
        JSONObject data = json.getJSONObject("data");

        return data.getString("api_key");
    }

    @Nullable
    @Undocumented
    @CheckReturnValue
    public String getAPIKeyBySessionToken(@NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawAPIKeyBySessionToken(sessionToken));
        JSONObject data = json.getJSONObject("data");

        return data.getString("api_key");
    }
}
