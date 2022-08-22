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

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.blocky.library.tixte.api.entities.Embed;
import dev.blocky.library.tixte.api.entities.SelfUser;
import dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException;
import dev.blocky.library.tixte.internal.RawResponseData;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
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
    private final SelfUser self = new SelfUser();

    PageDesign()
    {
    }

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
     * <br>Note that this method will throw an {@link TixteWantsYourMoneyException}
     * if you don't own a Tixte turbo/turbo-charged subscription.
     *
     * @param customCSS The custom CSS code for your page design.
     *
     * @return The current instance of the {@link PageDesign}.
     */
    @NotNull
    public PageDesign setCustomCSS(@Nullable String customCSS)
    {
        setCustomCSSRaw(customCSS == null ? "" : customCSS);
        return this;
    }

    /**
     * Sets the visibility of the branding on the left top corner of the page.
     * <br>This requires a Tixte turbo/turbo-charged subscription or else there will be thrown a {@link TixteWantsYourMoneyException}.
     *
     * @param hideBranding If the page should have the Tixte branding.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of the {@link PageDesign}.
     */
    @NotNull
    @CanIgnoreReturnValue
    public PageDesign setHideBranding(boolean hideBranding) throws ExecutionException, InterruptedException
    {
        if (!self.hasTixteSubscription())
        {
            throw new TixteWantsYourMoneyException("Payment required: This feature requires a turbo subscription");
        }

        setHideBrandingRaw(hideBranding);
        return this;
    }

    /**
     * Checks if the branding of the {@link Embed} is hidden.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return <b>true</b> - If the branding is hidden.
     *         <br><b>false</b> - If the branding is not hidden.
     */
    public boolean hidesBranding() throws ExecutionException, InterruptedException
    {
        DataObject json = DataObject.fromJson(getConfigRaw().get());
        DataObject data = json.getDataObject("data");

        return data.getBoolean("hide_branding");
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
                    "hide_branding=" + hidesBranding() +
                    '}';
        }
        catch (ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
