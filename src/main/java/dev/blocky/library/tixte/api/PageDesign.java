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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * Represents the 'Page Design' tab of the Tixte dashboard.
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-alpha.1
 */
public class PageDesign
{
    /**
     * Instantiates a <b>new</b> Page-Design.
     */
    PageDesign()
    {
    }

    /**
     * Gets your custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The custom CSS code.
     */
    @NotNull
    public String getCustomCSS() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getConfigRaw());
        JSONObject data = json.getJSONObject("data");

        return data.getString("custom_css");
    }

    /**
     * Sets the custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     * <br>Note that this method will throw an {@link dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException}
     * if you don't own a Tixte turbo/turbo-charged subscription.
     *
     * @param customCSS The custom CSS code for your page design.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The current instance of the PageDesign.
     */
    @NotNull
    public PageDesign setCustomCSS(@Nullable String customCSS) throws IOException
    {
        getRawResponseData().setCustomCSSRaw(customCSS == null ? "" : customCSS);
        return this;
    }
}
