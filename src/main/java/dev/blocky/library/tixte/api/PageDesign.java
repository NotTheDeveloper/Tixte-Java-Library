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

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.requests.json.DataPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Represents the 'Page Design' tab of the Tixte dashboard.
 *
 * @author BlockyDotJar
 * @version v1.4.0
 * @since v1.0.0-alpha.1
 */
public record PageDesign() implements RawResponseData
{
    private static final SelfUser self = new SelfUser();

    /**
     * Gets your custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The custom CSS code.
     */
    @NotNull
    public String getCustomCSS() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getConfigRaw().resultNow());
        return DataPath.getString(json, "data.custom_css");
    }

    /**
     * Sets the custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     * <br>Note that this method will throw an {@link TixteWantsYourMoneyException}
     * if you don't own a Tixte turbo/turbo-charged subscription.
     *
     * @param customCSS The custom CSS code for your page design.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout.
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of the {@link PageDesign}.
     */
    @NotNull
    public PageDesign setCustomCSS(@Nullable String customCSS) throws InterruptedException, IOException
    {
        RawResponseData.setCustomCSSRaw(customCSS == null ? "" : customCSS);
        return this;
    }

    /**
     * Sets the visibility of the branding on the left top corner of the page.
     * <br>This requires a Tixte turbo/turbo-charged subscription or else there will be thrown a {@link TixteWantsYourMoneyException}.
     *
     * @param hideBranding If the page should have the Tixte branding.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return The current instance of the {@link PageDesign}.
     */
    @NotNull
    @CanIgnoreReturnValue
    public PageDesign setHideBranding(boolean hideBranding) throws InterruptedException, IOException
    {
        if (!self.hasTixteSubscription())
        {
            throw new TixteWantsYourMoneyException("Payment required: This feature requires a turbo subscription");
        }

        RawResponseData.setHideBrandingRaw(hideBranding);
        return this;
    }

    /**
     * Checks if the branding of the {@link Embed} is hidden.
     *
     * @throws IOException If the request could not be executed due to cancellation, a connectivity problem or timeout. 
     *                     Because networks can fail during an exchange, it is possible that the remote server accepted 
     *                     the request before the failure.
     * @throws InterruptedException If the current thread was interrupted.
     *
     * @return <b>true</b> - If the branding is hidden.
     *         <br><b>false</b> - If the branding is not hidden.
     */
    public boolean hidesBranding() throws InterruptedException, IOException
    {
        final DataObject json = DataObject.fromJson(RawResponseData.getConfigRaw().resultNow());
        return DataPath.getBoolean(json, "data.hide_branding");
    }
}
