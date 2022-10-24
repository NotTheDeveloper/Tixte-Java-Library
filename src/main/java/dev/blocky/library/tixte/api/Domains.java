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
import dev.blocky.library.tixte.internal.requests.json.DataArray;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.requests.json.DataPath;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the 'Domains' tab of the Tixte dashboard and everything else what Tixte offers you with domains.
 *
 * @author BlockyDotJar
 * @version v1.4.0
 * @since v1.0.0-alpha.1
 */
public record  Domains() implements RawResponseData
{
    private static String lastDeletedDomain;

    /**
     * Gets the count of domains that you can use.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of domains that you can use.
     */
    public int getUsableDomainCount() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUsableDomainsRaw().resultNow());
        return DataPath.getInt(json, "data.count");
    }

    /**
     * Gets a {@link List} of every usable domain.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of every usable domain.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getUsableDomainNames() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUsableDomainsRaw().resultNow());
        final DataArray domains = DataPath.getDataArray(json, "data.domains");

        final List<String> list = new ArrayList<>();

        for (int i = 0; i < domains.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.domains[" + i + "]?.domain"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of {@code booleans} that represents if the domain is active at the moment.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of {@code booleans} that represents if the domain is active at the moment.
     */
    @Nullable
    @CheckReturnValue
    public List<Boolean> isActive() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUsableDomainsRaw().resultNow());
        final DataArray domains = DataPath.getDataArray(json, "data.domains");

        final List<Boolean> list = new ArrayList<>();

        for (int i = 0; i < domains.toList().size(); i++)
        {
            list.add(DataPath.getBoolean(json, "data.domains[" + i + "]?.active"));
        }
        return list;
    }

    /**
     * Gets the count of how many domains you own.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The count of how many domains you own.
     */
    public int getDomainCount() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserDomainsRaw().resultNow());
        return DataPath.getInt(json, "data.total");
    }

    /**
     * Gets a {@link List} of every owner by id of the domain.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of every owner by id of the domain.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getOwnerIds() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserDomainsRaw().resultNow());
        final DataArray domains = DataPath.getDataArray(json, "data.domains");

        final List<String> list = new ArrayList<>();

        for (int i = 0; i < domains.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.domains[" + i + "]?.owner"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of domain names.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of domain names.
     */
    @Nullable
    @CheckReturnValue
    public List<String> getDomainNames() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserDomainsRaw().resultNow());
        final DataArray domains = DataPath.getDataArray(json, "data.domains");

        final List<String> list = new ArrayList<>();

        for (int i = 0; i < domains.toList().size(); i++)
        {
            list.add(DataPath.getString(json, "data.domains[" + i + "]?.name"));
        }
        return list;
    }

    /**
     * Gets a {@link List} of upload-counts of the domain.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return A {@link List} of upload-counts of the domain.
     */
    @Nullable
    @CheckReturnValue
    public List<Integer> getUploadCounts() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getUserDomainsRaw().resultNow());
        final DataArray domains = DataPath.getDataArray(json, "data.domains");

        final List<Integer> list = new ArrayList<>();

        for (int i = 0; i < domains.toList().size(); i++)
        {
            list.add(DataPath.getInt(json, "data.domains[" + i + "]?.uploads"));
        }
        return list;
    }

    /**
     * Generates you a random domain.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The random domain.
     */
    @NotNull
    public String generateDomain() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.generateDomainRaw().resultNow());
        return DataPath.getString(json, "data.name");
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
     * @return The current instance of the {@link Domains} class.
     */
    @Nullable
    @CheckReturnValue
    public Domains addSubdomain(@NotNull String domainName) throws InterruptedException, IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        RawResponseData.addSubdomainRaw(domainName);
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
     * @return The current instance of the {@link Domains} class.
     */
    @Nullable
    @CheckReturnValue
    public Domains addCustomDomain(@NotNull String domainName) throws InterruptedException, IOException
    {
        Checks.notEmpty(domainName, "domainName");
        Checks.noWhitespace(domainName, "domainName");

        RawResponseData.addCustomDomainRaw(domainName);
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
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of the {@link Domains} class.
     */
    @Nullable
    @CheckReturnValue
    public Domains deleteDomain(@NotNull String domainName) throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.deleteDomainRaw(domainName).resultNow());

        lastDeletedDomain = DataPath.getString(json, "data.domain");
        return this;
    }
}
