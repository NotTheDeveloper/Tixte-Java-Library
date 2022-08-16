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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.internal.RawResponseData;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Represents your Tixte user-account.
 *
 * @author BlockyDotJar
 * @version v1.2.0
 * @since v1.0.0-beta.1
 */
public class SelfUser extends RawResponseData
{
    @Internal
    public SelfUser() { }

    /**
     * Checks if your account is email verified.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return  <b>true</b> - If your account is email verified.
     *          <br><b>false</b> - If your account is not email verified.
     */
    public boolean isEmailVerified() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getBoolean("email_verified");
    }

    /**
     * Gets the phone number of your account.
     * <br>This returns an empty string if there is no phone number given.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your phone number if not null or else it will return an empty string.
     */
    @Nullable
    @CheckReturnValue
    public String getPhoneNumber() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.isNull("phone") ? "": data.getString("phone");
    }

    /**
     * Gets your last login date as a ISO string.
     * <br>Example for ISO string: <b>2022-07-08T11:32:51.913Z</b>
     * <br>There is an <a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/java/DateFormatExample.java">example</a>
     * for formatting the ISO string.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your last login date as a ISO string.
     */
    @NotNull
    public String getLastLogin() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("last_login");
    }

    /**
     * Gets the count of all enabled flags.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of all enabled flags.
     */
    public int getFlagCount() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("flags");
    }

    /**
     * Gets the premium tier as an integer.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The premium tier as an integer.
     */
    public int getPremiumTier() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("premium_tier");
    }

    /**
     * If your account has mfa enabled.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return  <b>true</b> - If your account has mfa enabled.
     *          <br><b>false</b> - If your account has mfa disabled.
     */
    public boolean hasMFAEnabled() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getBoolean("mfa_enabled");
    }

    /**
     * Gets the id of your account.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The id of your account.
     */
    @NotNull
    public String getId() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("id");
    }

    /**
     * Gets the avatar id of your account.
     * <br>This returns an empty string if there is no avatar given.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The avatar id of your account.
     */
    @Nullable
    @CheckReturnValue
    public String getAvatarId() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.isNull("avatar") ? "": data.getString("avatar");
    }

    /**
     * Gets the upload region of your account.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The upload region of your account.
     */
    @NotNull
    public String getUploadRegion() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("upload_region");
    }

    /**
     * Gets the email of your account.
     * <br>This returns an empty string if there is no email given.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The email of your account.
     */
    @Nullable
    @CheckReturnValue
    public String getEmail() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.isNull("email") ? "": data.getString("email");
    }

    /**
     * Gets the username of your account.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The username of your account.
     */
    @NotNull
    public String getUsername() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("username");
    }

    /**
     * Gets your API-key by session token.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Your API-key.
     */
    @Nullable
    @CheckReturnValue
    public String getAPIKeyBySessionToken() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getAPIKeyBySessionTokenRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("api_key");
    }

    /**
     * Checks if you have a Tixte turbo/turbo-charged subscription.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @see #hasTixteTurboSubscription()
     * @see #hasTixteTurboChargedSubscription()
     *
     * @return <b>true</b> - If you have a Tixte turbo/turbo-charged subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo/turbo-charged subscription.
     */
    public boolean hasTixteSubscription() throws ExecutionException, InterruptedException
    {
        return hasTixteTurboSubscription() || hasTixteTurboChargedSubscription();
    }

    /**
     * Checks if you have a Tixte turbo subscription.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return <b>true</b> - If you have a Tixte turbo subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo subscription.
     */
    public boolean hasTixteTurboSubscription() throws ExecutionException, InterruptedException
    {
        return getPremiumTier() == 1;
    }

    /**
     * Checks if you have a Tixte turbo-charged subscription.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return <b>true</b> - If you have a Tixte turbo-charged subscription.
     *         <br><b>false</b> - If you do not have a Tixte turbo-charged subscription.
     */
    public boolean hasTixteTurboChargedSubscription() throws ExecutionException, InterruptedException
    {
        return getPremiumTier() == 2;
    }

    /**
     * Gets the count of every available experiment you can use.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of every available experiment you can use.
     */
    public int getExperimentCount() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getExperimentsRaw().get());

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
        catch (ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
