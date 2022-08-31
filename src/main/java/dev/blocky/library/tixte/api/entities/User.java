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
package dev.blocky.library.tixte.api.entities;

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.internal.RawResponseData;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.requests.json.DataPath;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Represents a Tixte user-account.
 *
 * @author BlockyDotJar
 * @version v1.3.0
 * @since v1.0.0-beta.1
 */
public class User extends RawResponseData
{
    private final String userData;

    User(@NotNull String userData)
    {
        super();

        Checks.notEmpty(userData, "userData");
        Checks.noWhitespace(userData, "userData");

        this.userData = userData;
    }

    /**
     * Gets the count of all enabled flags of the specific user.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of all enabled flags.
     */
    public int getFlagCount() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw(userData).get());
        return DataPath.getInt(json, "data.flags");
    }

    /**
     * Gets the id of the specific user.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The id of the specific user.
     */
    @Nullable
    @CheckReturnValue
    public String getId() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw(userData).get());
        return DataPath.getString(json, "data.id");
    }

    /**
     * Gets the avatar id of the specific user.
     * <br>This returns an empty string if there is no avatar given.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The avatar id of the specific user.
     */
    @Nullable
    @CheckReturnValue
    public String getAvatarId() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw(userData).get());
        return DataPath.getString(json, "data.avatar?");
    }

    /**
     * Gets the username of the specific user.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The username of the specific user.
     */
    @Nullable
    @CheckReturnValue
    public String getUsername() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserInfoRaw(userData).get());
        return DataPath.getString(json, "data.username");
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

        User user = (User) o;

        return Objects.equals(userData, user.userData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(new User(userData));
    }

    @NotNull
    @Override
    public String toString()
    {
        try
        {
            return "User{" +
                    "flags=" + getFlagCount() + ", " +
                    "id='" + getId() + "', " +
                    "avatar='" + getAvatarId() + "', " +
                    "username='" + getUsername() + '\'' +
                    '}';
        }
        catch (ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}

