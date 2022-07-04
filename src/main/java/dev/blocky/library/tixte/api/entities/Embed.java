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
package dev.blocky.library.tixte.api.entities;

import dev.blocky.library.tixte.annotations.Undocumented;
import dev.blocky.library.tixte.api.AccountType;
import dev.blocky.library.tixte.api.EmbedEditor;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.1
 */
@Undocumented
public class Embed
{
    /**
     * The maximum length an embed title can have.
     *
     * @see EmbedEditor#setTitle(String) EmbedEditor.setTitle(title)
     */
    public static final int TITLE_MAX_LENGTH = 256;

    /**
     * The maximum length the author name of an embed can have.
     *
     * @see EmbedEditor#setAuthorName(String) (String) EmbedEditor.setAuthor(title)
     * @see EmbedEditor#setAuthor(String, String) EmbedEditor.setAuthor(title, url)
     */
    public static final int AUTHOR_MAX_LENGTH = 256;

    /**
     * The maximum length the provider name of an embed can have.
     *
     * @see EmbedEditor#setProviderName(String) EmbedEditor.setProvider(title)
     * @see EmbedEditor#setProvider(String, String) EmbedEditor.setProvider(title, url)
     */
    public static final int PROVIDER_MAX_LENGTH = 256;

    /**
     * The maximum length the description of an embed can have.
     *
     * @see EmbedEditor#setDescription(CharSequence) EmbedEditor.setDescription(text)
     */
    public static final int DESCRIPTION_MAX_LENGTH = 4096;

    /**
     * The maximum length any URL can have inside an embed.
     *
     * @see EmbedEditor#setAuthor(String, String) EmbedEditor.setAuthor(text, url)
     */
    public static final int URL_MAX_LENGTH = 2000;

    /**
     * The maximum amount of total visible characters an embed can have.
     * <br>This limit depends on the current {@link AccountType AccountType} and applies to BOT.
     *
     * @see EmbedEditor#setDescription(CharSequence)
     * @see EmbedEditor#setTitle(String)
     */
    public static final int EMBED_MAX_LENGTH_BOT = 6000;

    /**
     * The maximum amount of total visible characters an embed can have.
     * <br>This limit depends on the current {@link AccountType AccountType} and applies to CLIENT.
     *
     * @see EmbedEditor#setDescription(CharSequence)
     * @see EmbedEditor#setTitle(String)
     */
    public static final int EMBED_MAX_LENGTH_CLIENT = 2000;

    private final transient String authorName, authorUrl, title;
    private final transient String providerName, providerURL;
    private final transient Object mutex = new Object();
    private volatile int length = -1;
    private final transient String description, color;

    @Undocumented
    public Embed(@Nullable String authorName, @Nullable String authorUrl, @Nullable String title, @Nullable String description,
          @Nullable String color, @Nullable String providerName, @Nullable String providerUrl) throws IOException
    {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.title = title;
        this.description = description;
        this.color = color;
        this.providerName = providerName;
        this.providerURL = providerUrl;

        getRawResponseData().setEmbedRaw(description, title, color, authorName, authorUrl, providerName, providerUrl);
    }

    /**
     * The title of the embed. Typically, this will be the html title of the webpage that is being embedded.
     *
     * @return Possibly-null String containing the title of the embedded resource.
     */
    @Nullable
    public String getTitle()
    {
        return title;
    }

    /**
     * The description of the embedded resource.
     * <br>This is provided only if Discord could find a description for the embedded resource using the provided url.
     * <br>Commonly, this is null. Be careful when using it.
     *
     * @return Possibly-null String containing a description of the embedded resource
     */
    @Nullable
    public String getDescription()
    {
        return description;
    }

    /**
     * The name on the creator of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     *
     * @return The name on the creator of the embedded content.
     */
    @Nullable
    public String getAuthorName()
    {
        return authorName;
    }

    /**
     * The url to a website from the creator of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     *
     * @return The url to a website from the creator of the embedded content.
     */
    @Nullable
    public String getAuthorURL()
    {
        return authorUrl;
    }

    /**
     * The name on the provider of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     *
     * @return The name on the provider of the embedded content.
     */
    @Nullable
    public String getProviderName()
    {
        return providerName;
    }

    /**
     * The url to a website of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     *
     * @return The url to a website of the embedded content.
     */
    @Nullable
    public String getProviderURL()
    {
        return providerURL;
    }

    /**
     * The color of the stripe on the side of the embed.
     * <br>If the color equals null, this will return {@code #ffffff} (no color).
     *
     * @return Possibly-null Color.
     */
    @Nullable
    public String getColor()
    {
        return color != null ? color : "#ffffff";
    }

    /**
     * The raw hex color value for this embed.
     * <br>Defaults to {@code 0xffffff} if no color is set.
     *
     * @return The raw hex color value or default
     */
    public String getColorRaw()
    {
        return color;
    }

    /**
     * The total amount of characters that is displayed when this embed is displayed by the Discord client.
     *
     * <p>The total character limit is defined by {@link #EMBED_MAX_LENGTH_BOT} as {@value #EMBED_MAX_LENGTH_BOT}.
     *
     * @return A never-negative sum of all displayed text characters.
     */
    public int getLength()
    {
        if (length > -1)
        {
            return length;
        }

        synchronized (mutex)
        {
            if (length > -1)
            {
                return length;
            }

            length = 0;

            if (title != null)
            {
                length += Checks.codePointLength(title);
            }

            if (description != null)
            {
                length += Checks.codePointLength(description.trim());
            }

            if (authorName != null)
            {
                length += Checks.codePointLength(authorName);
            }
            return length;
        }
    }
}
