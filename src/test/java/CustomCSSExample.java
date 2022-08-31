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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.PageDesign;
import dev.blocky.library.tixte.api.TixteClient;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Some basic examples, how to change the {@link PageDesign} of Tixte.
 *
 * @author BlockyDotJar
 * @version v1.1.1
 * @since v1.0.0-beta.3
 */
public class CustomCSSExample
{
    /**
     * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
     *
     * @return Builds the custom CSS code from the 'Page Design' tab of the Tixte dashboard.
     */
    @NotNull
    @CheckReturnValue
    public static PageDesign setCustomCSS() throws IOException
    {
        TixteClient client = BasicTixteClientExample.getTixteClient();
        PageDesign pageDesign = client.getPageDesign();

        // Creates a new File object for the CSS file. (You don't have to do this, but it's a good idea to do so.)
        // You can do this much simpler, by just using a string instead of a File object.
        // There are some pretty sweet themes that you can use. (some themes are in the resource folder of the test directory)
        File file = new File("YOUR_VALID_FILE_PATH");

        return pageDesign
                // This will throw an exception if the CSS is invalid.
                // This also will throw an TixteWantsYourMoneyException if you don't own a Tixte subscription, so I recommend
                // that you check if a subscription is active before you try to use this.
                .setCustomCSS(
                        // Reads the CSS file and gets the input of the file.
                        Files.readAllLines(file.toPath()).toString()
                );
    }
}
