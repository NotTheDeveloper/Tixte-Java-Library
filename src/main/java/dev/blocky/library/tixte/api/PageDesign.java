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
import dev.blocky.library.tixte.internal.RawResponseData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Represents the 'Page Design' tab of the Tixte dashboard.
 *
 * @author BlockyDotJar
 * @version v1.2.0
 * @since v1.0.0-alpha.1
 */
public class PageDesign extends RawResponseData
{
    private String customCSS;

    PageDesign() { }

    /**
     * Gets your custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The custom CSS code.
     */
    @NotNull
    public String getCustomCSS() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getConfigRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getString("custom_css");
    }

    /**
     * Sets the custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     *
     * @param customCSS The custom CSS code for your page design.
     *
     * @return The current instance of the {@link PageDesign}.
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
     * @return The current instance of the {@link PageDesign}.
     */
    @NotNull
    public PageDesign send()
    {
        setCustomCSSRaw(customCSS == null ? "" : customCSS);
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
        try
        {
            return "PageDesign{" +
                    "custom_css='" + getCustomCSS() + '\'' +
                    '}';
        }
        catch (ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
