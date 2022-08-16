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
import dev.blocky.library.tixte.internal.requests.json.DataArray;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Represents the 'Domains' tab of the Tixte dashboard and everything else what Tixte offers you with domains.
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-alpha.1
 */
public class Domains extends RawResponseData
{
    private String lastDeletedDomain;

    @Internal
    public Domains() { }

    /**
     * Gets the count of domains that you can use.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of domains that you can use.
     */
    public int getUsableDomainCount() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUsableDomainsRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("count");
    }

    /**
     * Gets every domain that you can use.
     * <br>The first index is (like an array) 0.
     * <br>If you want to get every domain at once you can use {@link RawResponseData#getUsableDomainsRaw()
     * RawResponseData#getUsableDomainsRaw()} and then you can print everything out by using a for loop, but if you want
     * you also can use an Optional.
     * <br>Because I don't know the best way to do this, it would be very nice to contact me if you know a better way
     * to implement this method/use this method.
     *
     * @param index The index for getting the domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return Every domain at its index.
     */

    @Nullable
    @CheckReturnValue
    public String getUsableDomainName(int index) throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUsableDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        Checks.notNegative(index, "index");

        return array.getDataObject(index).getString("domain");
    }

    /**
     * Checks if the domain is active at the moment.
     * <br>The first index is (like an array) 0.
     * <br>If you want to get every domain at once you can use {@link RawResponseData#getUsableDomainsRaw()
     * RawResponseData#getUsableDomainsRaw()} and then you can print everything out by using a for loop, but if you want
     * you also can use an Optional.
     * <br>Because I don't know the best way to do this, it would be very nice to contact me if you know a better way
     * to implement this method/use this method.
     *
     * @param index The index for checking the activity.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return  <b>true</b> - If the domain is active.
     *          <br><b>false</b> - If the domain is not active.
     */
    public boolean isActive(int index) throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUsableDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        Checks.notNegative(index, "index");

        return array.getDataObject(index).getBoolean("active");
    }

    /**
     * Gets the count of how many domains you own.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of how many domains you own.
     */
    public int getDomainCount() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserDomainsRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getInt("total");
    }

    /**
     * Gets the owner by id of the domain at the index.
     * <br>The first index is (like an array) 0.
     * <br>If you want to get every domain at once you can use {@link RawResponseData#getUsableDomainsRaw()
     * RawResponseData#getUsableDomainsRaw()} and then you can print everything out by using a for loop, but if you want
     * you also can use an Optional.
     * <br>Because I don't know the best way to do this, it would be very nice to contact me if you know a better way
     * to implement this method/use this method.
     *
     * @param index The index for getting the owner id of the domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The owner by id of the domain at the index.
     */
    @Nullable
    @CheckReturnValue
    public String getOwnerId(int index) throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        Checks.notNegative(index, "index");

        return array.getDataObject(index).getString("owner");
    }

    /**
     * Gets the name of the domain at the index.
     * <br>The first index is (like an array) 0.
     * <br>If you want to get every domain at once you can use {@link RawResponseData#getUsableDomainsRaw()
     * RawResponseData#getUsableDomainsRaw()} and then you can print everything out by using a for loop, but if you want
     * you also can use an Optional.
     * <br>Because I don't know the best way to do this, it would be very nice to contact me if you know a better way
     * to implement this method/use this method.
     *
     * @param index The index for getting the name of the domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The name of the domain at the index.
     */
    @Nullable
    @CheckReturnValue
    public String getDomainName(int index) throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        Checks.notNegative(index, "index");

        return array.getDataObject(index).getString("name");
    }

    /**
     * Gets the upload-count of the domain at the index.
     * <br>The first index is (like an array) 0.
     * <br>If you want to get every domain at once you can use {@link RawResponseData#getUsableDomainsRaw()
     * RawResponseData#getUsableDomainsRaw()} and then you can print everything out by using a for loop, but if you want
     * you also can use an Optional.
     * <br>Because I don't know the best way to do this, it would be very nice to contact me if you know a better way
     * to implement this method/use this method.
     *
     * @param index The index for getting the upload-count of the domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The upload-count of the domain at the index.
     */
    public int getUploadCount(int index) throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        Checks.notNegative(index, "index");

        return array.getDataObject(index).getInt("uploads");
    }

    /**
     * Generates you a random domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The random domain.
     */
    @NotNull
    public String generateDomain() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(generateDomainRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("name");
    }

    /**
     * Gets last deleted domain.
     * <br>Note that you should only use this method after the delete method has been called.
     *
     * @return The last deleted domain.
     */
    @Nullable
    @CheckReturnValue
    public Optional<String> getLastDeletedDomain()
    {
        return Optional.ofNullable(lastDeletedDomain);
    }

    /**
     * Adds a subdomain to our other domains.
     * <br>Note that you only can have 3 domains at once, if you don't have a Tixte turbo/turbo-charged subscription.
     * <br>If you have a turbo subscription, you can have up to 6 domains and if you have a turbo-charged subscription,
     * you can have up to 12 domains.
     * <br>Also note that the domain name cannot contain whitespaces or must not be empty.
     * <br>The domain name must be in the format of "domainName.tixte.co" and must not contain {@code http://} or {@code https://}.
     * <br>You also cannot add a domain that already exists.
     *
     * @param domainName The domain name.
     *
     * @return The current instance of the domain system.
     */
    @Nullable
    @CheckReturnValue
    public Domains addSubdomain(@NotNull String domainName) 
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        addSubdomainRaw(domainName);
        return this;
    }

    /**
     * Adds a custom domain to our other domains.
     * <br>Note that you only can have 3 domains at once, if you don't have a Tixte turbo/turbo-charged subscription.
     * <br>If you have a turbo subscription, you can have up to 6 domains and if you have a turbo-charged subscription,
     * you can have up to 12 domains.
     * <br>Also note that the domain name cannot contain whitespaces or must not be empty.
     * <br>You cannot add a domain that already exists.
     * <br>You also cannot add a custom domain that you don't own.
     *
     * @param domainName The domain name.
     *
     * @return The current instance of the domain system.
     */
    @Nullable
    @CheckReturnValue
    public Domains addCustomDomain(@NotNull String domainName) 
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        addCustomDomainRaw(domainName);
        return this;
    }

    /**
     * Deletes a domain of your domain collection.
     * <br>Note that the domain must not be empty.
     * <br>The domain name must be in the format of "domainName.tixte.co" and must not contain {@code http://} or {@code https://}.
     * <br>You also cannot delete a domain that already exists.
     *
     * @param domainName The domain name.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of the domain system.
     */
    @Nullable
    @CheckReturnValue
    public Domains deleteDomain(@NotNull String domainName) throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(deleteDomainRaw(domainName).get());
        DataObject data = json.getDataObject("data");

        lastDeletedDomain = data.getString("domain");
        return this;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(new Domains());
    }

    @NotNull
    @Override
    public String toString()
    {
        try
        {
            return "Domains{" +
                    "count=" + getUsableDomainCount() + ", " +
                    "total=" + getDomainCount() + ", " +
                    "lastDeletedDomain='" + lastDeletedDomain + '\'' +
                    '}';
        }
        catch (ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
