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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Represents the 'Domains' tab of the Tixte dashboard and everything else what Tixte offers you with domains.
 *
 * @author BlockyDotJar
 * @version v1.2.0
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
     * Gets a {@link List} of every usable domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of every usable domain.
     */

    @Nullable
    @CheckReturnValue
    public List<String> getUsableDomainNames() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUsableDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("domain"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of {@code booleans} that represents if the domain is active at the moment.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of {@code booleans} that represents if the domain is active at the moment.
     */
    public List<Boolean> isActive() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUsableDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        List<Boolean> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getBoolean("active"));
        }
        return new ArrayList<>(list);
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
     * Gets a {@link List} of every owner by id of the domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of every owner by id of the domain.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getOwnerIds() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("owner"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of domain names.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of domain names.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getDomainNames() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getString("name"));
        }
        return new ArrayList<>(list);
    }

    /**
     * Gets a {@link List} of upload-counts of the domain.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of upload-counts of the domain.
     */
    public List<Integer> getUploadCounts() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getUserDomainsRaw().get());
        DataObject data = json.getDataObject("data");
        DataArray array = data.getArray("domains");

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < array.toList().size(); i++)
        {
            list.add(array.getDataObject(i).getInt("uploads"));
        }
        return new ArrayList<>(list);
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
