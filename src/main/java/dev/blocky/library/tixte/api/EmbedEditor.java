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
import dev.blocky.library.tixte.api.enums.AccountType;
import dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.Helpers;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import static dev.blocky.library.tixte.api.RawResponseData.*;

/**
 * Builder system used to build {@link Embed embeds}.
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-beta.1
 */
public class EmbedEditor
{
    private final Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*",
            Pattern.CASE_INSENSITIVE);
    private final Logger logger = TixteLogger.getLog(EmbedEditor.class);
    private final StringBuilder description = new StringBuilder();
    private String providerName, providerURL, color;
    private String authorName, authorURL, title;
    private AccountType accountType;

    /**
     * Constructs a <b>new</b> {@link EmbedEditor} instance, which can be used to create {@link Embed embeds}.
     * <br>Every part of an embed can be removed or cleared by providing {@code null} to the setter method.
     */
    EmbedEditor() { }

    /**
     * Creates an {@link EmbedEditor} using fields from an existing editor.
     *
     * @param editor The existing editor.
     */
    EmbedEditor(@Nullable EmbedEditor editor)
    {
        copyFrom(editor);
    }

    /**
     * Creates an {@link EmbedEditor} using fields in an existing embed.
     *
     * @param embed The existing embed.
     */
    EmbedEditor(@Nullable Embed embed)
    {
        copyFrom(embed);
    }

    /**
     * Returns a {@link Embed} that has been checked as being valid for sending.
     *
     * @param accountType The type of account that is sending the embed.
     *
     * @throws java.lang.IllegalStateException
     *         If the embed is empty. Can be checked with {@link #isEmpty()}.
     * @throws IOException If the request could not be executed due to cancellation,
     *         a connectivity problem or timeout. Because networks can fail during an exchange,
     *         it is possible that the remote server accepted the request before the failure.
     *
     * @return The built, sendable {@link Embed}.
     */
    @NotNull
    public Embed build(@NotNull AccountType accountType) throws IOException
    {
        if (accountType == null)
        {
            accountType = AccountType.CLIENT;
            logger.info("'accountType' equals null, setting to CLIENT");
        }

        if (isEmpty())
        {
            throw new IllegalStateException("Cannot build an empty embed!");
        }

        if (description.length() > Embed.DESCRIPTION_MAX_LENGTH)
        {
            throw new IllegalStateException(Helpers.format("Description is longer than %d! Please limit your input!",
                    Embed.DESCRIPTION_MAX_LENGTH));
        }

        if (length() > Embed.EMBED_MAX_LENGTH_BOT && accountType == AccountType.BOT)
        {
            throw new IllegalStateException("Cannot build an embed with more than " + Embed.EMBED_MAX_LENGTH_BOT +
                    " characters!");
        }

        if (length() > Embed.EMBED_MAX_LENGTH_CLIENT && accountType == AccountType.CLIENT)
        {
            throw new IllegalStateException("Cannot build an embed with more than " + Embed.EMBED_MAX_LENGTH_CLIENT +
                    " characters!");
        }

        String description = this.description.length() < 1 ? null : this.description.toString();
        
        this.accountType = accountType;

        return new Embed(authorName, authorURL, title, description, color, providerName, providerURL, accountType);
    }

    /**
     * Returns a {@link Embed} that has been checked as being valid for sending.
     * <br>The account type is set to {@link AccountType#CLIENT}.
     *
     * @throws java.lang.IllegalStateException
     *         If the embed is empty. Can be checked with {@link #isEmpty()}.
     * @throws IOException If the request could not be executed due to cancellation,
     *         a connectivity problem or timeout. Because networks can fail during an exchange,
     *         it is possible that the remote server accepted the request before the failure.
     *
     * @return The built, sendable {@link Embed}.
     */
    @NotNull
    @CanIgnoreReturnValue
    public Embed build() throws IOException
    {
        this.accountType = AccountType.CLIENT;
        return build(AccountType.CLIENT);
    }

    /**
     * Resets this editor to default state.
     * <br>All parts will be either empty or null after this method has returned.
     *
     * @return The current {@link EmbedEditor} with default values.
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
        accountType = null;
        return this;
    }

    /**
     * Copies the data from the given editor into this editor.
     * <br>All the parts of the given editor will be applied to this one.
     *
     * @param editor The existing editor.
     */
    public void copyFrom(@Nullable EmbedEditor editor)
    {
        if (editor != null)
        {
            setDescription(editor.description.toString());
            this.authorName = editor.authorName;
            this.authorURL = editor.authorURL;
            this.title = editor.title;
            this.color = editor.color;
            this.providerName = editor.providerName;
            this.providerURL = editor.providerURL;
            this.accountType = editor.accountType;
        }
    }

    /**
     * Copies the data from the given embed into this editor.
     * <br>All the parts of the given embed will be applied to this editor.
     *
     * @param embed The existing embed.
     */
    public void copyFrom(@Nullable Embed embed)
    {
        if(embed != null)
        {
            setDescription(embed.getDescription());
            this.authorName = embed.getAuthorName();
            this.authorURL = embed.getAuthorURL();
            this.title = embed.getTitle();
            this.color = embed.getColor();
            this.providerName = embed.getProviderName();
            this.providerURL = embed.getProviderURL();
            this.accountType = embed.getAccountType();
        }
    }

    /**
     * Checks if the given embed is empty. Empty embeds will throw an exception if built.
     *
     * @return <b>true</b> - If the embed is empty and cannot be built.
     *         <br><b>false</b> - If the embed is not empty and can be built.
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
     * @return Length of the current editor state.
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
     * Sets the title of the {@link Embed}.
     *
     * <br><b><a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/tixte-embed.png" target="_blank">Example</a></b>
     *
     * @param title The title of the {@link Embed}.
     *
     * @throws IllegalArgumentException
     *         <ul>
     *             <li>If the provided {@code title} is an empty string.</li>
     *             <li>If the character limit for {@code title}, defined by {@link Embed#TITLE_MAX_LENGTH} as {@value Embed#TITLE_MAX_LENGTH},
     *             is exceeded.</li>
     *         </ul>
     *
     * @return The editor after the title has been set.
     */
    @NotNull
    public EmbedEditor setTitle(@Nullable String title)
    {
        if (title == null)
        {
            this.title = null;
            logger.info("'title' equals null, setting to null");
        }
        else
        {
            Checks.notEmpty(title, "Title");
            Checks.check(title.length() <= Embed.TITLE_MAX_LENGTH, "Title cannot be longer than %d characters.",
                    Embed.TITLE_MAX_LENGTH);

            this.title = title;
        }
        return this;
    }

    /**
     * Sets the description of the {@link Embed}.
     * <br>This is where the main chunk of text for an {@link Embed} is typically placed.
     *
     * <br><b><a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/tixte-embed.png" target="_blank">Example</a></b>
     *
     * @param description The description of the {@link Embed}, {@code null} to reset.
     *
     * @throws IllegalArgumentException
     *         If {@code description} is longer than {@value Embed#DESCRIPTION_MAX_LENGTH} characters,
     *         as defined by {@link Embed#DESCRIPTION_MAX_LENGTH}.
     *
     * @return The editor after the description has been set.
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
     * Appends to the description of the {@link Embed}.
     * <br>This is where the main chunk of text for an {@link Embed} is typically placed.
     *
     * <br><b><a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/tixte-embed.png" target="_blank">Example</a></b>
     *
     * @param description The string to append to the description of the {@link Embed}.
     *
     * @throws IllegalArgumentException
     *         <ul>
     *             <li>If the provided {@code description} String is null.</li>
     *             <li>If the character limit for {@code description}, defined by {@link Embed#DESCRIPTION_MAX_LENGTH}
     *             as {@value Embed#DESCRIPTION_MAX_LENGTH}, is exceeded.</li>
     *         </ul>
     *
     * @return The editor after the description has been set.
     */
    @NotNull
    @CanIgnoreReturnValue
    public EmbedEditor appendDescription(@NotNull CharSequence description)
    {
        Checks.notNull(description, "description");
        Checks.check(this.description.length() + description.length() <= Embed.DESCRIPTION_MAX_LENGTH,
                "Description cannot be longer than %d characters.", Embed.DESCRIPTION_MAX_LENGTH);
        this.description.append(description);
        return this;
    }

    /**
     * Sets the color of the {@link Embed}.
     *
     * <br><b><a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/tixte-embed.png" target="_blank">Example</a></b>
     *
     * @param hexColor The {@link Color color} as a string of the {@link Embed} or {@code null} to use no color.
     *
     * @return The editor after the color has been set.
     */
    @NotNull
    public EmbedEditor setColor(@Nullable String hexColor)
    {
        this.color = hexColor;
        return this;
    }

    /**
     * Sets the author of the {@link Embed}.
     * <br>The author appears in the top left of the {@link Embed} with its name being made clickable by way of providing an url.
     * <br>This convenience method just sets the name.
     *
     * <br><b><a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/tixte-embed.png" target="_blank">Example</a></b>
     *
     * @param authorName The name of the author of the {@link Embed}. If this is not set, the author will not appear in the {@link Embed}.
     *
     * @throws IllegalArgumentException  If {@code name} is longer than {@value Embed#AUTHOR_MAX_LENGTH} characters,
     *                                   as defined by {@link Embed#AUTHOR_MAX_LENGTH}.
     *
     * @return The editor after the author has been set.
     */
    @NotNull
    public EmbedEditor setAuthorName(@Nullable String authorName)
    {
        return setAuthor(authorName, null);
    }

    /**
     * Sets the author of the embed.
     * <br>The author appears in the top left of the embed and can have a small image beside it along with the author's
     * name being made clickable by way of providing an url.
     * <br>This convenience method sets the name and the url.
     *
     * <br><b><a href="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/tixte-embed.png" target="_blank">Example</a></b>
     *
     * @param  authorName The name of the author of the embed. If this is not set, the author will not appear in the embed.
     * @param  authorURL The url of the author of the embed.
     *
     * @throws IllegalArgumentException
     *         <ul>
     *             <li>If the character limit for {@code name}, defined by {@link Embed#AUTHOR_MAX_LENGTH} as
     *             {@value Embed#AUTHOR_MAX_LENGTH}, is exceeded.</li>
     *             <li>If the character limit for {@code url}, defined by {@link Embed#URL_MAX_LENGTH} as
     *             {@value Embed#URL_MAX_LENGTH}, is exceeded.</li>
     *             <li>If the provided {@code url} is not a properly formatted http or https url.</li>
     *         </ul>
     *
     * @return The editor after the author has been set.
     */
    @NotNull
    public EmbedEditor setAuthor(@Nullable String authorName, @Nullable String authorURL)
    {
        if (authorName == null)
        {
            this.authorName = null;
            logger.info("'authorName' equals null, setting to null");
        }
        else
        {
            Checks.check(authorName.length() <= Embed.AUTHOR_MAX_LENGTH, "Name cannot be longer than %d characters.",
                    Embed.AUTHOR_MAX_LENGTH);
            urlCheck(authorURL);

            this.authorName = authorName;
            this.authorURL = authorURL;
        }
        return this;
    }

    /**
     * Sets the provider of the embed.
     *
     * @param  providerName The name of the provider of the embed. If this is not set, the provider will not appear in the embed.
     *
     * @throws IllegalArgumentException  If {@code name} is longer than {@value Embed#PROVIDER_MAX_LENGTH} characters,
     *                                   as defined by {@link Embed#PROVIDER_MAX_LENGTH}.
     *
     * @return The editor after the provider has been set.
     */
    @NotNull
    public EmbedEditor setProviderName(@Nullable String providerName)
    {
        return setProvider(providerName, null);
    }

    /**
     * Sets the provider of the embed.
     * This convenience method sets the name and the url.
     *
     * @param  providerName The name of the provider of the embed. If this is not set, the provider will not appear in the embed.
     * @param  providerURL The url of the provider of the embed.
     *
     * @throws IllegalArgumentException
     *         <ul>
     *             <li>If the character limit for {@code name}, defined by {@link Embed#PROVIDER_MAX_LENGTH} as
     *             {@value Embed#PROVIDER_MAX_LENGTH}, is exceeded.</li>
     *             <li>If the character limit for {@code url}, defined by {@link Embed#URL_MAX_LENGTH} as
     *             {@value Embed#URL_MAX_LENGTH}, is exceeded.</li>
     *             <li>If the provided {@code url} is not a properly formatted http or https url.</li>
     *         </ul>
     *
     * @return The editor after the author has been set.
     */
    @NotNull
    public EmbedEditor setProvider(@Nullable String providerName, @Nullable String providerURL)
    {
        if (providerName == null)
        {
            this.providerName = null;
            logger.info("'providerName' equals null, setting to null");
        }
        else
        {
            Checks.check(providerName.length() <= Embed.PROVIDER_MAX_LENGTH, "Name cannot be longer than %d characters.",
                    Embed.PROVIDER_MAX_LENGTH);
            urlCheck(providerURL);

            this.providerName = providerName;
            this.providerURL = providerURL;
        }
        return this;
    }

    private void urlCheck(@Nullable String url)
    {
        if (url != null)
        {
            Checks.check(url.length() <= Embed.URL_MAX_LENGTH, "URL cannot be longer than %d characters.",
                    Embed.URL_MAX_LENGTH);
            Checks.check(URL_PATTERN.matcher(url).matches(), "URL must be a valid http(s) or attachment url.");
        }
    }

    /**
     * Sets the visibility of the branding in the {@link Embed}.
     * <br>This requires a Tixte turbo/turbo-charged subscription or else there will be thrown a {@link TixteWantsYourMoneyException}.
     *
     * @param hideBranding If the embed should have the Tixte branding.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The current instance of the {@link EmbedEditor}.
     */
    @NotNull
    @CanIgnoreReturnValue
    public EmbedEditor setHideBranding(boolean hideBranding) throws IOException
    {
        setHideBrandingRaw(hideBranding);
        return this;
    }

    /**
     * If this is set, there only will be an image in the {@link Embed}.
     * <br>This is highly recommended not to set to true, if you are using any method of the {@link EmbedEditor} class,
     * because there could be some errors if you use {@link #build()} to send the request.
     *
     * @param onlyImagedEnabled If the embed should only have images.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The current instance of the {@link EmbedEditor}.
     */
    @NotNull
    @CanIgnoreReturnValue
    public EmbedEditor setOnlyImagedEnabled(boolean onlyImagedEnabled) throws IOException
    {
        setOnlyImageEnabledRaw(onlyImagedEnabled);
        return this;
    }

    /**
     * Checks if the branding of the {@link Embed} is hidden.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return <b>true</b> - If the branding is hidden.
     *         <br><b>false</b> - If the branding is not hidden.
     */
    public boolean hidesBranding() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");

        return data.getBoolean("hide_branding");
    }

    /**
     * Checks if there is only the image of the {@link Embed} enabled.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return <b>true</b> - If only the image of the {@link Embed} is enabled.
     *         <br><b>false</b> - If the {@link Embed} is not only the image.
     */
    public boolean onlyImageEnabled() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");

        return data.getBoolean("only_image");
    }

    /**
     * The title of the {@link Embed}.
     * <br>Typically, this will be the html title of the webpage that is being embedded.
     * <br>The only difference between this and {@link Embed#getTitle()} is that this method gets the current title
     * of the Tixte 'Embed Editor' page, while {@link Embed#getTitle()} only gets the current value of the variable.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Possibly-empty string containing the title of the embedded resource.
     */
    @NotNull
    public String getEmbedTitle() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");
        DataObject embed = data.getDataObject("embed");

        return embed.getString("title");
    }

    /**
     * The description of the embedded resource.
     * <br>This is provided only if Discord could find a description for the embedded resource using the provided url.
     * <br>Commonly, this is null. Be careful when using it.
     * <br>The only difference between this and {@link Embed#getDescription()} is that this method gets the current description
     * of the Tixte 'Embed Editor' page, while {@link Embed#getDescription()} only gets the current value of the variable.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Possibly-empty string containing a description of the embedded resource.
     */
    @NotNull
    public String getEmbedDescription() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");
        DataObject embed = data.getDataObject("embed");

        return embed.getString("description");
    }

    /**
     * The name on the creator of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     * <br>The only difference between this and {@link Embed#getAuthorName()} is that this method gets the current author name
     * of the Tixte 'Embed Editor' page, while {@link Embed#getAuthorName()} only gets the current value of the variable.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The name on the creator of the embedded content.
     */
    @NotNull
    public String getEmbedAuthorName() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");
        DataObject embed = data.getDataObject("embed");

        return embed.getString("author_name");
    }

    /**
     * The url to a website from the creator of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     * <br>The only difference between this and {@link Embed#getAuthorURL()}is that this method gets the current author url
     * of the Tixte 'Embed Editor' page, while {@link Embed#getAuthorURL()} only gets the current value of the variable.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The url to a website from the creator of the embedded content.
     */
    @NotNull
    public String getEmbedAuthorURL() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");
        DataObject embed = data.getDataObject("embed");

        return embed.getString("author_url");
    }

    /**
     * The name on the provider of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     * <br>The only difference between this and {@link Embed#getProviderName()} is that this method gets the current provider name
     * of the Tixte 'Embed Editor' page, while {@link Embed#getProviderName()} only gets the current value of the variable.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The name on the provider of the embedded content.
     */
    @NotNull
    public String getEmbedProviderName() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");
        DataObject embed = data.getDataObject("embed");

        return embed.getString("provider_name");
    }

    /**
     * The url to a website of the embedded content.
     * <br>This is typically used to represent the account on the providing site.
     * <br>The only difference between this and {@link Embed#getProviderURL()} is that this method gets the current provider url
     * of the Tixte 'Embed Editor' page, while {@link Embed#getProviderURL()} only gets the current value of the variable.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The url to a website of the embedded content.
     */
    @NotNull
    public String getEmbedProviderURL() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");
        DataObject embed = data.getDataObject("embed");

        return embed.getString("provider_url");
    }

    /**
     * The color of the stripe on the side of the {@link Embed}.
     * <br>If the color equals null, this will return {@code #ffffff} (no color).
     * <br>The only difference between this and {@link Embed#getColor()} is that this method gets the current theme color
     * of the Tixte 'Embed Editor' page, while {@link Embed#getColor()} only gets the current value of the variable.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return Possibly-empty color.
     */
    @NotNull
    public String getEmbedThemeColor() throws IOException
    {
        DataObject json = DataObject.fromJson(getConfigRaw());
        DataObject data = json.getDataObject("data");
        DataObject embed = data.getDataObject("embed");

        return embed.getString("theme_color");
    }

    @Override
    public boolean equals(@Nullable Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        EmbedEditor that = (EmbedEditor) o;

        return URL_PATTERN.equals(that.URL_PATTERN) && Objects.equals(description.toString(), that.description.toString()) &&
                Objects.equals(providerName, that.providerName) && Objects.equals(providerURL, that.providerURL) && color.equals(that.color)
                && Objects.equals(authorName, that.authorName) && Objects.equals(authorURL, that.authorURL) && Objects.equals(title, that.title)
                && accountType == that.accountType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(URL_PATTERN, description, providerName, providerURL, color, authorName, authorURL, title, accountType);
    }

    @NotNull
    @Override
    public String toString()
    {
        try
        {
            return "EmbedEditor{" +
                    "URL_PATTERN=" + URL_PATTERN + ", " +
                    "only_image=" + onlyImageEnabled() + ", " +
                    "hide_branding=" + hidesBranding() + ", " +
                    "description=" + getEmbedDescription() + ", " +
                    "provider_name='" + getEmbedProviderName() + "', " +
                    "provider_url='" + getEmbedProviderURL() + "', " +
                    "theme_color='" + getEmbedThemeColor() + "', " +
                    "author_name='" + getEmbedAuthorName() + "', " +
                    "author_url='" + getEmbedAuthorURL() + "', " +
                    "title='" + getEmbedTitle() + "', " +
                    "accountType=" + accountType +
                    '}';
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}