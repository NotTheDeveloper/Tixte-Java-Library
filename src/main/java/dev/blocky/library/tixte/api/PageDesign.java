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

import dev.blocky.library.tixte.annotations.Undocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
@Undocumented
public class PageDesign
{
    @Undocumented
    PageDesign()
    {
    }

    @NotNull
    @Undocumented
    public String getCustomCSS() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");

        return data.getString("custom_css");
    }

    @NotNull
    @Undocumented
    public PageDesign setCustomCSS(@Nullable String customCSS) throws IOException
    {
        getRawResponseData().setCustomCSSRaw(customCSS == null ? "" : customCSS);
        return this;
    }
}
