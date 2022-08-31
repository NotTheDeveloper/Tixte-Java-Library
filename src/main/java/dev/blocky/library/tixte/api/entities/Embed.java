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
package dev.blocky.library.tixte.api.entities;

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.api.EmbedEditor;
import dev.blocky.library.tixte.internal.RawResponseData;
import dev.blocky.library.tixte.internal.utils.Helpers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Represents an embed displayed by Discord.
 * <br>This class has many possibilities for null values, so be careful!
 *
 * @author BlockyDotJar
 * @version v1.1.0
 * @since v1.0.0-beta.1
 */
public class Embed extends RawResponseData
{
    /**
     * The maximum length an embed title can have.
     *
     * @see EmbedEditor#setTitle(String)
     */
    public static final int TITLE_MAX_LENGTH = 256;

    /**
     * The maximum length the author name of an embed can have.
     *
     * @see EmbedEditor#setAuthorName(String)
     * @see EmbedEditor#setAuthor(String, String)
     */
    public static final int AUTHOR_NAME_MAX_LENGTH = 256;

    /**
     * The maximum length the provider name of an embed can have.
     *
     * @see EmbedEditor#setProviderName(String)
     * @see EmbedEditor#setProvider(String, String)
     */
    public static final int PROVIDER_NAME_MAX_LENGTH = 256;

    /**
     * The maximum length the description of an embed can have.
     *
     * @see EmbedEditor#setDescription(CharSequence)
     */
    public static final int DESCRIPTION_MAX_LENGTH = 4096;

    /**
     * The maximum length any URL can have inside an embed.
     *
     * @see EmbedEditor#setAuthor(String, String)
     */
    public static final int URL_MAX_LENGTH = 2000;

    /**
     * The maximum amount of total visible characters an embed can have.
     *
     * @see EmbedEditor#setDescription(CharSequence)
     * @see EmbedEditor#setTitle(String)
     */
    public static final int EMBED_MAX_LENGTH = 6000;

    private final String authorName, authorUrl, title;
    private final String providerName, providerUrl;
    private final String description, color;
    private final Object mutex = new Object();
    private int length = -1;

    /**
     * Instantiates a <b>new</b> embed.
     *
     * @param authorName The author name to be built.
     * @param authorUrl The author url to be built.
     * @param title The title to be built.
     * @param description The description to be built.
     * @param color The color to be built.
     * @param providerName The provider name to be built.
     * @param providerUrl The provider url to be built.
     *
     * @throws ExecutionException If this future completed exceptionally.
     * @throws InterruptedException If the current thread was interrupted.
     */
    public Embed(@Nullable String authorName, @Nullable String authorUrl, @Nullable String title, @Nullable String description,
                 @Nullable String color, @Nullable String providerName, @Nullable String providerUrl)
            throws ExecutionException, InterruptedException
    {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.title = title;
        this.description = description;
        this.color = color;
        this.providerName = providerName;
        this.providerUrl = providerUrl;


        try
        {
            Constructor<?> constructor = EmbedEditor.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            EmbedEditor editor = (EmbedEditor) constructor.newInstance();

            String embedDescription = description == null ? editor.getEmbedDescription() : description;
            String embedTitle = title == null ? editor.getEmbedTitle() : title;
            String embedColor = color == null ? editor.getEmbedThemeColor() : color;
            String embedAuthorName = authorName == null ? editor.getEmbedAuthorName() : authorName;
            String embedAuthorUrl = authorUrl == null ? editor.getEmbedAuthorUrl() : authorUrl;
            String embedProviderName = providerName == null ? editor.getEmbedProviderName() : providerName;
            String embedProviderUrl = providerUrl == null ? editor.getEmbedProviderUrl() : providerUrl;

            System.out.println(embedDescription);
            System.out.println(embedTitle);
            System.out.println(embedColor);
            System.out.println(embedAuthorName);
            System.out.println(embedAuthorUrl);
            System.out.println(embedProviderName);
            System.out.println(embedProviderUrl);

            setEmbedRaw(embedDescription, embedTitle, embedColor, embedAuthorName, embedAuthorUrl, embedProviderName, embedProviderUrl);
        }
        catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * The title of the embed.
     * <br>Typically, this will be the html title of the webpage that is being embedded.
     *
     * @return Possibly-null string containing the title of the embedded resource.
     */
    @Nullable
    @CheckReturnValue
    public String getTitle()
    {
        return title;
    }

    /**
     * The description of the embedded resource.
     * <br>This is provided only if Discord could find a description for the embedded resource using the provided url.
     * <br>Commonly, this is null. Be careful when using it.
     *
     * @return Possibly-null string containing a description of the embedded resource.
     */
    @Nullable
    @CheckReturnValue
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
    @CheckReturnValue
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
    @CheckReturnValue
    public String getAuthorUrl()
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
    @CheckReturnValue
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
    @CheckReturnValue
    public String getProviderUrl()
    {
        return providerUrl;
    }

    /**
     * The color of the stripe on the side of the embed.
     * <br>If the color equals null, this will return {@code #ffffff} (no color).
     *
     * @return Possibly-null color.
     */
    @Nullable
    @CheckReturnValue
    public String getColor()
    {
        return color != null ? color : "#ffffff";
    }

    /**
     * The total amount of characters that is displayed when this embed is displayed by the Discord client.
     *
     * <br>The total character limit is defined by {@link #EMBED_MAX_LENGTH}.
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
                length += Helpers.codePointLength(title);
            }

            if (description != null)
            {
                length += Helpers.codePointLength(description.trim());
            }

            if (authorName != null)
            {
                length += Helpers.codePointLength(authorName);
            }
            return length;
        }
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

        Embed embed = (Embed) o;

        return length == embed.length && Objects.equals(authorName, embed.authorName) && Objects.equals(authorUrl, embed.authorUrl)
                && Objects.equals(title, embed.title) && Objects.equals(providerName, embed.providerName) && Objects.equals(providerUrl, embed.providerUrl) &&
                Objects.equals(description, embed.description) && color.equals(embed.color) && Objects.equals(mutex, embed.mutex);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(authorName, authorUrl, title, providerName, providerUrl, description, color, mutex, length);
    }

    @NotNull
    @Override
    public String toString()
    {
        return "Embed{" +
                "authorName='" + authorName + "', " +
                "authorUrl='" + authorUrl + "', " +
                "title='" + title + "', " +
                "providerName='" + providerName + "', " +
                "providerUrl='" + providerUrl + "', " +
                "description='" + description + "', " +
                "color='" + color + "', " +
                "mutex=" + mutex + ", " +
                "length=" + length +
                '}';
    }
}
