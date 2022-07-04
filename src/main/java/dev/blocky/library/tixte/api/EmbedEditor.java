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
import dev.blocky.library.tixte.api.entities.Embed;
import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import static dev.blocky.library.tixte.api.TixteClient.getRawResponseData;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.1
 */
@Undocumented
public class EmbedEditor
{
    private final Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*", Pattern.CASE_INSENSITIVE);
    private final StringBuilder description = new StringBuilder();
    private String authorName, authorURL, title;
    private String providerName, providerURL, color;

    /**
     * Constructs a new EmbedEditor instance, which can be used to create {@link Embed embeds}.
     * <br>Every part of an embed can be removed or cleared by providing {@code null} to the setter method.
     */
    EmbedEditor() { }

    /**
     * Creates an EmbedEditor using fields from an existing builder.
     *
     * @param  builder The existing builder
     */
    EmbedEditor(@Nullable EmbedEditor builder)
    {
        copyFrom(builder);
    }

    /**
     * Creates an EmbedEditor using fields in an existing embed.
     *
     * @param  embed The existing embed
     */
    EmbedEditor(@Nullable Embed embed)
    {
        copyFrom(embed);
    }

    /**
     * Returns a {@link Embed embed} that has been checked as being valid for sending.
     *
     * @throws java.lang.IllegalStateException
     *         If the embed is empty. Can be checked with {@link #isEmpty()}.
     *
     * @return The built, sendable {@link Embed embed}
     */
    @NotNull
    public Embed build(@NotNull AccountType accountType) throws IOException {
        if (isEmpty())
        {
            throw new IllegalStateException("Cannot build an empty embed!");
        }

        if (description.length() > Embed.DESCRIPTION_MAX_LENGTH)
        {
            throw new IllegalStateException(Checks.format("Description is longer than %d! Please limit your input!", Embed.DESCRIPTION_MAX_LENGTH));
        }

        if (length() > Embed.EMBED_MAX_LENGTH_BOT && accountType == AccountType.BOT)
        {
            throw new IllegalStateException("Cannot build an embed with more than " + Embed.EMBED_MAX_LENGTH_BOT + " characters!");
        }

        if (length() > Embed.EMBED_MAX_LENGTH_CLIENT && accountType == AccountType.CLIENT)
        {
            throw new IllegalStateException("Cannot build an embed with more than " + Embed.EMBED_MAX_LENGTH_CLIENT + " characters!");
        }

        final String description = this.description.length() < 1 ? null : this.description.toString();

        return new Embed(authorName, authorURL, title, description, color, providerName, providerURL);
    }

    /**
     * Resets this builder to default state.
     * <br>All parts will be either empty or null after this method has returned.
     *
     * @return The current EmbedEditor with default values
     */
    @NotNull
    public EmbedEditor clear()
    {
        authorName = null;
        authorURL = null;
        title = null;
        description.setLength(0);
        color = null;
        providerName = null;
        providerURL = null;
        return this;
    }

    /**
     * Copies the data from the given builder into this builder.
     * <br>All the parts of the given builder will be applied to this one.
     *
     * @param  builder The existing builder
     */
    public void copyFrom(@Nullable EmbedEditor builder)
    {
        if (builder != null)
        {
            setDescription(builder.description.toString());
            this.authorName = builder.authorName;
            this.authorURL = builder.authorURL;
            this.title = builder.title;
            this.color = builder.color;
            this.providerName = builder.providerName;
            this.providerURL = builder.providerURL;
        }
    }

    /**
     * Copies the data from the given embed into this builder.
     * <br>All the parts of the given embed will be applied to this builder.
     *
     * @param  embed The existing embed
     */
    public void copyFrom(@Nullable Embed embed)
    {
        if(embed != null)
        {
            setDescription(embed.getDescription());
            this.authorName = embed.getAuthorName();
            this.authorURL = embed.getAuthorURL();
            this.title = embed.getTitle();
            this.color = embed.getColorRaw();
            this.providerName = embed.getProviderName();
            this.providerURL = embed.getProviderURL();
        }
    }

    /**
     * Checks if the given embed is empty. Empty embeds will throw an exception if built.
     *
     * @return <b>true</b> - If the embed is empty and cannot be built
     */
    public boolean isEmpty()
    {
        return (title == null || title.trim().isEmpty())
                && authorName == null
                && authorURL == null
                && title == null
                && description.length() == 0
                && color == null
                && providerName == null
                && providerURL == null;
    }

    /**
     * The overall length of the current EmbedEditor in displayed characters.
     * <br>Represents the {@link Embed#getLength() Embed.getLength()} value.
     *
     * @return Length of the current builder state
     */
    public int length()
    {
        int length = description.toString().trim().length();

        if (title != null)
        {
            length += title.length();
        }

        if (authorName != null)
        {
            length += authorName.length();
        }
        return length;
    }

    /**
     * Sets the title of the embed.
     *
     * <p><b><a href="https://raw.githubusercontent.com/DV8FromTheWorld/JDA/assets/assets/docs/embeds/04-setTitle.png">Example</a></b>
     *
     * @param  title The title of the embed
     *
     * @throws java.lang.IllegalArgumentException
     *         <ul>
     *             <li>If the provided {@code title} is an empty String.</li>
     *             <li>If the character limit for {@code title}, defined by {@link Embed#TITLE_MAX_LENGTH} as {@value Embed#TITLE_MAX_LENGTH},
     *             is exceeded.</li>
     *         </ul>
     *
     * @return The builder after the title has been set
     */
    @NotNull
    public EmbedEditor setTitle(@Nullable String title)
    {
        if (title == null)
        {
            this.title = null;
        }
        else
        {
            Checks.notEmpty(title, "Title");
            Checks.check(title.length() <= Embed.TITLE_MAX_LENGTH, "Title cannot be longer than %d characters.", Embed.TITLE_MAX_LENGTH);

            this.title = title;
        }
        return this;
    }

    /**
     * Sets the description of the embed. This is where the main chunk of text for an embed is typically placed.
     *
     * <p><b><a href="https://raw.githubusercontent.com/DV8FromTheWorld/JDA/assets/assets/docs/embeds/05-setDescription.png">Example</a></b>
     *
     * @param  description The description of the embed, {@code null} to reset
     *
     * @throws java.lang.IllegalArgumentException
     *         If {@code description} is longer than {@value Embed#DESCRIPTION_MAX_LENGTH} characters,
     *         as defined by {@link Embed#DESCRIPTION_MAX_LENGTH}
     *
     * @return The builder after the description has been set
     */
    @NotNull
    public final EmbedEditor setDescription(@Nullable CharSequence description)
    {
        this.description.setLength(0);

        if (description != null && description.length() >= 1)
        {
            appendDescription(description);
        }
        return this;
    }

    /**
     * Appends to the description of the embed. This is where the main chunk of text for an embed is typically placed.
     *
     * <p><b><a href="https://raw.githubusercontent.com/DV8FromTheWorld/JDA/assets/assets/docs/embeds/05-setDescription.png">Example</a></b>
     *
     * @param  description The string to append to the description of the embed
     *
     * @throws java.lang.IllegalArgumentException
     *         <ul>
     *             <li>If the provided {@code description} String is null.</li>
     *             <li>If the character limit for {@code description}, defined by {@link Embed#DESCRIPTION_MAX_LENGTH} as {@value Embed#DESCRIPTION_MAX_LENGTH},
     *             is exceeded.</li>
     *         </ul>
     *
     * @return The builder after the description has been set
     */
    @NotNull
    public EmbedEditor appendDescription(@NotNull CharSequence description)
    {
        Checks.notNull(description, "description");
        Checks.check(this.description.length() + description.length() <= Embed.DESCRIPTION_MAX_LENGTH,
                "Description cannot be longer than %d characters.", Embed.DESCRIPTION_MAX_LENGTH);
        this.description.append(description);
        return this;
    }

    /**
     * Sets the color of the embed.
     *
     * <p><b><a href="https://raw.githubusercontent.com/DV8FromTheWorld/JDA/assets/assets/docs/embeds/02-setColor.png" target="_blank">Example</a></b>
     *
     * @param  hexColor The {@link java.awt.Color Color} of the embed or {@code null} to use no color
     *
     * @return The builder after the color has been set
     */
    @NotNull
    public EmbedEditor setColor(@Nullable String hexColor)
    {
        this.color = hexColor;
        return this;
    }

    /**
     * Sets the author of the embed. The author appears in the top left of the embed and can have a small
     * image beside it along with the author's name being made clickable by way of providing an url.
     * This convenience method just sets the name.
     *
     * <p><b><a href="https://raw.githubusercontent.com/DV8FromTheWorld/JDA/assets/assets/docs/embeds/03-setAuthor.png">Example</a></b>
     *
     * @param  authorName The name of the author of the embed. If this is not set, the author will not appear in the embed
     *
     * @throws java.lang.IllegalArgumentException
     *         If {@code name} is longer than {@value Embed#AUTHOR_MAX_LENGTH} characters,
     *         as defined by {@link Embed#AUTHOR_MAX_LENGTH}
     *
     * @return The builder after the author has been set
     */
    @NotNull
    public EmbedEditor setAuthorName(@Nullable String authorName)
    {
        return setAuthor(authorName, null);
    }

    /**
     * Sets the author of the embed. The author appears in the top left of the embed and can have a small
     * image beside it along with the author's name being made clickable by way of providing an url.
     * This convenience method just sets the name and the url.
     *
     * <p><b><a href="https://raw.githubusercontent.com/DV8FromTheWorld/JDA/assets/assets/docs/embeds/03-setAuthor.png">Example</a></b>
     *
     * @param  authorName The name of the author of the embed. If this is not set, the author will not appear in the embed
     * @param  authorURL The url of the author of the embed
     *
     * @throws java.lang.IllegalArgumentException
     *         <ul>
     *             <li>If the character limit for {@code name}, defined by {@link Embed#AUTHOR_MAX_LENGTH} as {@value Embed#AUTHOR_MAX_LENGTH},
     *             is exceeded.</li>
     *             <li>If the character limit for {@code url}, defined by {@link Embed#URL_MAX_LENGTH} as {@value Embed#URL_MAX_LENGTH},
     *             is exceeded.</li>
     *             <li>If the provided {@code url} is not a properly formatted http or https url.</li>
     *         </ul>
     *
     * @return The builder after the author has been set
     */
    @NotNull
    public EmbedEditor setAuthor(@Nullable String authorName, @Nullable String authorURL)
    {
        if (authorName == null)
        {
            this.authorName = null;
        }
        else
        {
            Checks.check(authorName.length() <= Embed.AUTHOR_MAX_LENGTH, "Name cannot be longer than %d characters.", Embed.AUTHOR_MAX_LENGTH);
            urlCheck(authorURL);

            this.authorName = authorName;
            this.authorURL = authorURL;
        }
        return this;
    }

    /**
     * Sets the provider of the embed.
     *
     * @param  providerName The name of the provider of the embed. If this is not set, the provider will not appear in the embed
     *
     * @throws java.lang.IllegalArgumentException
     *         If {@code name} is longer than {@value Embed#PROVIDER_MAX_LENGTH} characters,
     *         as defined by {@link Embed#PROVIDER_MAX_LENGTH}
     *
     * @return The builder after the provider has been set
     */
    @NotNull
    public EmbedEditor setProviderName(@Nullable String providerName)
    {
        return setProvider(providerName, null);
    }

    /**
     * Sets the provider of the embed.
     * This convenience method just sets the name and the url.
     *
     * @param  providerName The name of the provider of the embed. If this is not set, the provider will not appear in the embed
     * @param  providerURL The url of the provider of the embed
     *
     * @throws java.lang.IllegalArgumentException
     *         <ul>
     *             <li>If the character limit for {@code name}, defined by {@link Embed#PROVIDER_MAX_LENGTH} as {@value Embed#PROVIDER_MAX_LENGTH},
     *             is exceeded.</li>
     *             <li>If the character limit for {@code url}, defined by {@link Embed#URL_MAX_LENGTH} as {@value Embed#URL_MAX_LENGTH},
     *             is exceeded.</li>
     *             <li>If the provided {@code url} is not a properly formatted http or https url.</li>
     *         </ul>
     *
     * @return The builder after the author has been set
     */
    @NotNull
    public EmbedEditor setProvider(@Nullable String providerName, @Nullable String providerURL)
    {
        if (providerName == null)
        {
            this.providerName = null;
        }
        else
        {
            Checks.check(providerName.length() <= Embed.PROVIDER_MAX_LENGTH, "Name cannot be longer than %d characters.", Embed.PROVIDER_MAX_LENGTH);
            urlCheck(providerURL);

            this.providerName = providerName;
            this.providerURL = providerURL;
        }
        return this;
    }

    @Undocumented
    private void urlCheck(@Nullable String url)
    {
        if (url != null)
        {
            Checks.check(url.length() <= Embed.URL_MAX_LENGTH, "URL cannot be longer than %d characters.", Embed.URL_MAX_LENGTH);
            Checks.check(URL_PATTERN.matcher(url).matches(), "URL must be a valid http(s) or attachment url.");
        }
    }

    @NotNull
    @Undocumented
    public String getEmbedTitle() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");
        JSONObject embed = data.getJSONObject("embed");

        return embed.getString("title");
    }

    @NotNull
    @Undocumented
    public String getEmbedThemeColor() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");
        JSONObject embed = data.getJSONObject("embed");

        return embed.getString("theme_color");
    }

    @NotNull
    @Undocumented
    public String getEmbedAuthorName() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");
        JSONObject embed = data.getJSONObject("embed");

        return embed.getString("author_name");
    }

    @NotNull
    @Undocumented
    public String getEmbedAuthorURL() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");
        JSONObject embed = data.getJSONObject("embed");

        return embed.getString("author_url");
    }

    @NotNull
    @Undocumented
    public String getEmbedProviderName() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");
        JSONObject embed = data.getJSONObject("embed");

        return embed.getString("provider_name");
    }

    @NotNull
    @Undocumented
    public String getEmbedProviderURL() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");
        JSONObject embed = data.getJSONObject("embed");

        return embed.getString("provider_url");
    }

    @NotNull
    @Undocumented
    public String getEmbedDescription() throws IOException
    {
        JSONObject json = new JSONObject(getRawResponseData().getRawConfig());
        JSONObject data = json.getJSONObject("data");
        JSONObject embed = data.getJSONObject("embed");

        return embed.getString("description");
    }
}