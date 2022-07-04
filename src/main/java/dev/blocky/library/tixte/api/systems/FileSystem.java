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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
@Undocumented
public class FileSystem
{
    private transient volatile String url, directURL, deletionURL;

    @Undocumented
    public FileSystem()
    {
    }

    @Undocumented
    public long getUsedSize() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawSize());
        JSONObject data = json.getJSONObject("data");
        return data.getInt("used");
    }

    @Undocumented
    public long getLimit() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawSize());
        JSONObject data = json.getJSONObject("data");
        return data.getInt("limit");
    }

    @Undocumented
    public int getPremiumTier() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawSize());
        JSONObject data = json.getJSONObject("data");
        return data.getInt("premium_tier");
    }

    @Undocumented
    public int getTotalUploadCount(int page) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        return data.getInt("total");
    }

    @Undocumented
    public int getResults(int page) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        return data.getInt("results");
    }

    @Undocumented
    public int getPermissionLevel(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getInt("permission_level");
    }

    @Undocumented
    public String getExtension(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getString("extension");
    }

    @Undocumented
    public long getSize(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getInt("size");
    }

    @Undocumented
    public String getUploadDate(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getString("uploaded_at");
    }

    @Undocumented
    public String getDomain(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getString("domain");
    }

    @Undocumented
    public String getName(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getString("name");
    }

    @Undocumented
    public String getMimeType(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getString("mimetype");
    }

    @Undocumented
    public String getExpirationTime(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).isNull("expiration") ? "": array.getJSONObject(index).getString("expiration");
    }

    @Undocumented
    public String getAssetId(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getString("asset_id");
    }

    @Undocumented
    public int getType(int page, int index) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUploads(page));
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("uploads");

        return array.getJSONObject(index).getInt("type");
    }

    @Undocumented
    public String getFileName(int page, int index) throws IOException
    {
        return getName(page, index) + "." + getExtension(page, index);
    }

    @Undocumented
    public String getUploadRegion() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawUserInfo());
        JSONObject data = json.getJSONObject("data");

        return data.getString("upload_region");
    }

    @Undocumented
    public String getURL()
    {
        return Optional.ofNullable(url).orElseThrow(() -> new IllegalStateException("URL is unavailable, because this method is called without calling uploadFile<BonusType>(...) before."));
    }

    @Undocumented
    public String getDirectURL()
    {
        return Optional.ofNullable(directURL).orElseThrow(() -> new IllegalStateException("Direct URL is unavailable, because this method is called without calling uploadFile<BonusType>(...) before."));
    }

    @Undocumented
    public String getDeletionURL()
    {
        return Optional.ofNullable(deletionURL).orElseThrow(() -> new IllegalStateException("Deletion URL is unavailable, because this method is called without calling uploadFile<BonusType>(...) before."));
    }

    @Undocumented
    public FileSystem uploadFile(@NotNull File file) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFileRaw(file));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadPrivateFile(@NotNull File file) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFilePrivateRaw(file));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadFile(@NotNull File file, @NotNull String domain) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFileRaw(file, domain));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadPrivateFile(@NotNull File file, @NotNull String domain) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFilePrivateRaw(file, domain));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadFile(@NotNull URI filePath) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFileRaw(filePath));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadPrivateFile(@NotNull URI filePath) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFilePrivateRaw(filePath));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadFile(@NotNull URI filePath, @NotNull String domain) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFileRaw(filePath, domain));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadPrivateFile(@NotNull URI filePath, @NotNull String domain) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFilePrivateRaw(filePath, domain));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadFile(@NotNull String filePath) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFileRaw(filePath));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadPrivateFile(@NotNull String filePath) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFilePrivateRaw(filePath));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadFile(@NotNull String filePath, @NotNull String domain) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFileRaw(filePath, domain));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem uploadPrivateFile(@NotNull String filePath, @NotNull String domain) throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().uploadFilePrivateRaw(filePath, domain));
        JSONObject data = json.getJSONObject("data");

        url = data.getString("url");
        directURL = data.getString("direct_url");
        deletionURL = data.getString("deletion_url");
        return this;
    }

    @Undocumented
    public FileSystem deleteFile(@NotNull String fileId) throws IOException
    {
        getRawResponseData().deleteFileRaw(fileId);
        return this;
    }
}