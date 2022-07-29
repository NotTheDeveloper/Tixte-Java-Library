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

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.blocky.library.tixte.api.Embed;
import dev.blocky.library.tixte.api.EmbedEditor;
import dev.blocky.library.tixte.api.TixteClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Some basic examples, how to use an {@link EmbedEditor}.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.3
 */
public class EmbedEditorExample
{
    /**
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return A {@link Embed} that has been checked as being valid for sending.
     */
    @NotNull
    @CanIgnoreReturnValue
    public static Embed buildEmbed() throws IOException
    {
        TixteClient client = BasicTixteClientExample.getTixteClient();

        // Creates a new EmbedEditor.
        EmbedEditor editor = client.getEmbedEditor();

        // Sets the author name and url of the embed. If you only want to set the author name, you can use setAuthorName(@Nullable String).
        // You can not only set the author url, because that doesn't make much sense.
        editor.setAuthor("{{owner.username}}", "https://discord.com/users/731080543503908895 ")
                // Sets the title of the embed.
                .setTitle("Here is a {{filetype}} i made for you!")
                // Sets the description of the embed.
                .setDescription("{{size}} | {{uploaded_at}}")
                // Sets the color of the embed.
                .setColor("#5d6dfd")
                // Sets the provider name and url of the embed. If you only want to set the author name, you can use setProviderName(@Nullable String).
                // You can not only set the provider url, because that doesn't make much sense.
                .setProvider("You don't need to know more than: Java > *", "https://github.com/BlockyDotJar");

        // Builds the embed and sends it to Tixte.
        return editor.build();
    }
}
