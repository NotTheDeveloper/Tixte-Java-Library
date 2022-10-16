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
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.requests.json.DataPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Represents your Tixte user-account.
 *
 * @author BlockyDotJar
 * @version v1.4.0
 * @since v1.0.0-beta.1
 */
public record SelfUser() implements RawResponseData
{

    /**
     * Checks if your account is email verified.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return  <b>true</b> - If your account is email verified.
     *          <br><b>false</b> - If your account is not email verified.
     */
    public boolean isEmailVerified() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getBoolean(json, "data.email_verified");
    }

    /**
     * Gets the phone number of your account.
     * <br>This returns an empty string if there is no phone number given.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your phone number if not null or else it will return an empty string.
     */
    @Nullable
    @CheckReturnValue
    public String getPhoneNumber() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.phone?");
    }

    /**
     * Gets your last login date as a ISO string.
     * <br>Example for ISO string: <b>2022-07-08T11:32:51.913Z</b>
     * <br>There is an <a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/java/DateFormatExample.java">example</a>
     * for formatting the ISO string.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your last login date as a ISO string.
     */
    @NotNull
    public String getLastLogin() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.last_login");
    }

    /**
     * Gets the count of all enabled flags.
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
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getInt(json, "data.flags");
    }

    /**
     * Gets the premium tier as an integer.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The premium tier as an integer.
     */
    public int getPremiumTier() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getInt(json, "data.premium_tier");
    }

    /**
     * If your account has mfa enabled.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return  <b>true</b> - If your account has mfa enabled.
     *          <br><b>false</b> - If your account has mfa disabled.
     */
    public boolean hasMFAEnabled() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getBoolean(json, "data.mfa_enabled");
    }

    /**
     * Gets the id of your account.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The id of your account.
     */
    @NotNull
    public String getId() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.id");
    }

    /**
     * Gets the avatar id of your account.
     * <br>This returns an empty string if there is no avatar given.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The avatar id of your account.
     */
    @Nullable
    @CheckReturnValue
    public String getAvatarId() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.avatar?");
    }

    /**
     * Gets the upload region of your account.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The upload region of your account.
     */
    @NotNull
    public String getUploadRegion() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.upload_region");
    }

    /**
     * Gets the email of your account.
     * <br>This returns an empty string if there is no email given.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The email of your account.
     */
    @Nullable
    @CheckReturnValue
    public String getEmail() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.email?");
    }

    /**
     * Gets the username of your account.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The username of your account.
     */
    @NotNull
    public String getUsername() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getUserInfoRaw().resultNow());
        return DataPath.getString(json, "data.username");
    }

    /**
     * Gets your API-key by session token.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your API-key.
     */
    @Nullable
    @CheckReturnValue
    public String getAPIKeyBySessionToken() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getAPIKeyBySessionTokenRaw().resultNow());
        return DataPath.getString(json, "data.api_key");
    }

    /**
     * Checks if you have a Tixte turbo/turbo-charged subscription.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see #hasTixteTurboSubscription()
     * @see #hasTixteTurboChargedSubscription()
     *
     * @return <b>true</b> - If you have a Tixte turbo/turbo-charged subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo/turbo-charged subscription.
     */
    public boolean hasTixteSubscription() throws InterruptedException, IOException
    {
        return hasTixteTurboSubscription() || hasTixteTurboChargedSubscription();
    }

    /**
     * Checks if you have a Tixte turbo subscription.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return <b>true</b> - If you have a Tixte turbo subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo subscription.
     */
    public boolean hasTixteTurboSubscription() throws InterruptedException, IOException
    {
        return getPremiumTier() == 1;
    }

    /**
     * Checks if you have a Tixte turbo-charged subscription.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return <b>true</b> - If you have a Tixte turbo-charged subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo-charged subscription.
     */
    public boolean hasTixteTurboChargedSubscription() throws InterruptedException, IOException
    {
        return getPremiumTier() == 2;
    }

    /**
     * Gets the count of every available experiment you can use.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of every available experiment you can use.
     */
    public int getExperimentCount() throws InterruptedException, IOException
    {
        DataObject json = DataObject.fromJson(RawResponseData.getExperimentsRaw().resultNow());
        return json.getInt("data");
    }
}
