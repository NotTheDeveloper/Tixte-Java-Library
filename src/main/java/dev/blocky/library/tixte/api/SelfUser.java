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
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

import static dev.blocky.library.tixte.api.RawResponseData.*;

/**
 * Represents your Tixte user-account.
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-beta.1
 */
public class SelfUser
{

    /**
     * Instantiates a <b>new</b> Self-User.
     */
    SelfUser()
    {
    }

    /**
     * Checks if your account is email verified.
     *
     * @return  <b>true</b> - If your account is email verified.
     *          <br><b>false</b> - If your account is not email verified.
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     */
    public boolean isEmailVerified() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getBoolean("email_verified");
    }

    /**
     * Gets the phone number of your account.
     * <br>This returns an empty string if there is no phone number given.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     * @return Your phone number if not null or else it will return an empty string.
     */
    @Nullable
    @CheckReturnValue
    public String getPhoneNumber() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.isNull("phone") ? "": data.getString("phone");
    }

    /**
     * Gets your last login date as a ISO string.
     * <br>Example for ISO string: <b>2022-07-08T11:32:51.913Z</b>
     * <br>You can format this string with the {@link java.time.format.DateTimeFormatter} class.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Your last login date as a ISO string.
     */
    @NotNull
    public String getLastLogin() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getString("last_login");
    }

    /**
     * Gets the count of all enabled flags.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The count of all enabled flags.
     */
    public int getFlagCount() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getInt("flags");
    }

    /**
     * Gets the premium tier as an integer.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The premium tier as an integer.
     */
    public int getPremiumTier() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getInt("premium_tier");
    }

    /**
     * If your account has mfa enabled.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return  <b>true</b> - If your account has mfa enabled.
     *          <br><b>false</b> - If your account has mfa disabled.
     */
    public boolean hasMFAEnabled() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getBoolean("mfa_enabled");
    }

    /**
     * Gets the id of your account.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The id of your account.
     */
    @NotNull
    public String getId() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getString("id");
    }

    /**
     * Gets the avatar id of your account.
     * <br>This returns an empty string if there is no avatar given.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The avatar id of your account.
     */
    @Nullable
    @CheckReturnValue
    public String getAvatarId() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.isNull("avatar") ? "": data.getString("avatar");
    }

    /**
     * Gets the upload region of your account.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The upload region of your account.
     */
    @NotNull
    public String getUploadRegion() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getString("upload_region");
    }

    /**
     * Gets the email of your account.
     * <br>This returns an empty string if there is no email given.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The email of your account.
     */
    @Nullable
    @CheckReturnValue
    public String getEmail() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.isNull("email") ? "": data.getString("email");
    }

    /**
     * Gets the username of your account.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The username of your account.
     */
    @NotNull
    public String getUsername() throws IOException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw());
        DataObject data = json.getDataObject("data");

        return data.getString("username");
    }

    /**
     * Gets your API-key by session token.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Your API-key.
     */
    @Nullable
    @CheckReturnValue
    public String getAPIKeyBySessionToken() throws IOException
    {
        DataObject json = DataObject.fromJson(getAPIKeyBySessionTokenRaw());
        DataObject data = json.getDataObject("data");

        return data.getString("api_key");
    }

    /**
     * Checks if you have a Tixte turbo/turbo-charged subscription.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @see #hasTixteTurboSubscription()
     * @see #hasTixteTurboChargedSubscription()
     *
     * @return <b>true</b> - If you have a Tixte turbo/turbo-charged subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo/turbo-charged subscription.
     */
    public boolean hasTixteSubscription() throws IOException
    {
        return hasTixteTurboSubscription() || hasTixteTurboChargedSubscription();
    }

    /**
     * Checks if you have a Tixte turbo subscription.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return <b>true</b> - If you have a Tixte turbo subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo subscription.
     */
    public boolean hasTixteTurboSubscription() throws IOException
    {
        return getPremiumTier() == 1;
    }

    /**
     * Checks if you have a Tixte turbo-charged subscription.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return <b>true</b> - If you have a Tixte turbo-charged subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo-charged subscription.
     */
    public boolean hasTixteTurboChargedSubscription() throws IOException
    {
        return getPremiumTier() == 2;
    }

    /**
     * Gets the count of every available experiment you can use.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The count of every available experiment you can use.
     */
    public int getExperimentCount() throws IOException
    {
        DataObject json = DataObject.fromJson(getExperimentsRaw());

        return json.getInt("data");
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(new SelfUser());
    }

    @NotNull
    @Override
    public String toString()
    {
        try
        {
            return "SelfUser{" +
                    "email_verified=" + isEmailVerified() + ", " +
                    "phone='" + getPhoneNumber() + "', " +
                    "last_login='" + getLastLogin() + "', " +
                    "flags=" + getFlagCount() + ", " +
                    "premium_tier=" + getPremiumTier() + ", " +
                    "mfa_enabled=" + hasMFAEnabled() + ", " +
                    "id='" + getId() + "', " +
                    "avatar='" + getAvatarId() + "', " +
                    "upload_region='" + getUploadRegion() + "', " +
                    "email='" + getEmail() + "', " +
                    "username='" + getUsername() + "', " +
                    "apiKey='" + getAPIKeyBySessionToken() + "', " +
                    "hasTixteSubscription=" + hasTixteSubscription() + ", " +
                    "hasTixteTurboSubscription=" + hasTixteTurboSubscription() + ", " +
                    "hasTixteTurboChargedSubscription=" + hasTixteTurboChargedSubscription() + ", " +
                    "data=" + getExperimentCount() +
                    '}';
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
