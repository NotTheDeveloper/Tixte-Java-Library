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

import dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

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
    private String customCSS;

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
        DataObject json = DataObject.fromJson(getRawResponseData().getConfigRaw());
        DataObject data = json.getDataObject("data");

        return data.getString("custom_css");
    }

    /**
     * Sets the custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     *
     * @param customCSS The custom CSS code for your page design.
     *
     * @return The current instance of the PageDesign.
     */
    @NotNull
    public PageDesign setCustomCSS(@Nullable String customCSS)
    {
        this.customCSS = customCSS;
        return this;
    }

    /**
     * Builds the custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     * <br>Note that this method will throw an {@link TixteWantsYourMoneyException}
     * if you don't own a Tixte turbo/turbo-charged subscription.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The current instance of the PageDesign.
     */
    public PageDesign build() throws IOException
    {
        getRawResponseData().setCustomCSSRaw(customCSS == null ? "" : customCSS);
        return this;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(new PageDesign());
    }

    @NotNull
    @Override
    public String toString()
    {
        try {
            return "PageDesign{" +
                    "custom_css='" + getCustomCSS() + '\'' +
                    '}';
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
