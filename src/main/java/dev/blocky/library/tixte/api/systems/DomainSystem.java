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
package dev.blocky.library.tixte.api.systems;

import dev.blocky.library.tixte.annotations.Undocumented;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
@Undocumented
public class DomainSystem
{
    private transient volatile String domain;
    private transient volatile boolean isCustom;

    @Undocumented
    public DomainSystem()
    {
    }

    @Undocumented
    public int getUsableDomainCount() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawOwnedDomains());
        JSONObject data = json.getJSONObject("data");

        return data.getInt("count");
    }

    @Undocumented
    public String getUsableDomains(int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawOwnedDomains());
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getString("domain");
    }

    @Undocumented
    public boolean isActive(int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawOwnedDomains());
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getBoolean("active");
    }

    @Undocumented
    public int getDomainCount() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains());
        JSONObject data = json.getJSONObject("data");

        return data.getInt("total");
    }

    @Undocumented
    public String getOwnerId(int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains());
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getString("owner");
    }

    @Undocumented
    public String getDomainName(int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains());
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getString("name");
    }

    @Undocumented
    public int getUploadCount(int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains());
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getInt("uploads");
    }

    @Undocumented
    public int getDomainCount(@NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains(sessionToken));
        JSONObject data = json.getJSONObject("data");

        return data.getInt("total");
    }

    @Undocumented
    public String getOwnerId(int index, @NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains(sessionToken));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getString("owner");
    }

    @Undocumented
    public String getDomainName(int index, @NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains(sessionToken));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getString("name");
    }

    @Undocumented
    public int getUploadCount(int index, @NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserDomains(sessionToken));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("domains");

        return array.getJSONObject(index).getInt("uploads");
    }

    @NotNull
    @Undocumented
    public String generateDomain() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().generateRawDomain());
        JSONObject data = json.getJSONObject("data");

        return data.getString("name") + "." + data.getString("domain");
    }

    @Undocumented
    public boolean isCustom()
    {
        return isCustom;
    }

    @Undocumented
    public String getLastDeletedDomain()
    {
        return Optional.ofNullable(domain).orElseThrow(() -> new IllegalStateException("No last deleted domains found. Probably because you use this method before a request is made."));
    }

    @Undocumented
    public DomainSystem addSubdomain(@NotNull String domainName) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().addSubdomainRaw(domainName));
        JSONObject data = json.getJSONObject("data");

        isCustom = data.getBoolean("custom");
        return this;
    }

    @Undocumented
    public DomainSystem addSubdomain(@NotNull String domainName, @NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().addSubdomainRaw(domainName, sessionToken));
        JSONObject data = json.getJSONObject("data");

        isCustom = data.getBoolean("custom");
        return this;
    }

    @Undocumented
    public DomainSystem addCustomDomain(@NotNull String domainName) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().addCustomDomainRaw(domainName));
        JSONObject data = json.getJSONObject("data");

        isCustom = data.getBoolean("custom");
        return this;
    }

    @Undocumented
    public DomainSystem addCustomDomain(@NotNull String domainName, @NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().addCustomDomainRaw(domainName, sessionToken));
        JSONObject data = json.getJSONObject("data");

        isCustom = data.getBoolean("custom");
        return this;
    }

    @Undocumented
    public DomainSystem deleteDomain(@NotNull String domainName) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().deleteDomainRaw(domainName));
        JSONObject data = json.getJSONObject("data");

        domain = data.getString("domain");
        return this;
    }

    @Undocumented
    public DomainSystem deleteDomain(@NotNull String domainName, @NotNull String sessionToken) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().deleteDomainRaw(domainName, sessionToken));
        JSONObject data = json.getJSONObject("data");

        domain = data.getString("domain");
        return this;
    }
}
