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

import dev.blocky.library.tixte.api.EmbedEditor;
import dev.blocky.library.tixte.api.TixteClient;
import dev.blocky.library.tixte.api.TixteClientBuilder;
import dev.blocky.library.tixte.api.enums.CachePolicy;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main class with examples.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
public class Main
{
    private static final File file = new File("FILE_PATH");
    private static final Logger logger = TixteLogger.getLog(Main.class);


    /**
     * The main method for testing features.
     *
     * @param args An array of string arguments.
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     */
    public static void main(@NotNull String[] args) throws IOException
    {
        TixteClient client = new TixteClientBuilder()
                .create(getAPIKey())
                .setSessionToken(getSessionToken())
                .setDefaultDomain(getDefaultDomain())
                .setCachePolicy(CachePolicy.ALL)
                .build();

        EmbedEditor editor = client.getEmbedEditor();

        editor.setAuthor("{{owner.username}}", "https://discord.com/users/731080543503908895 ")
                .setTitle("Here is a {{filetype}} i made for you!")
                .setDescription("{{size}} | {{uploaded_at}}")
                .setColor("#5d6dfd")
                .setProvider("You don't need to know more than: Java > *", "https://github.com/BlockyDotJar");

        editor.build();
    }

    /**
     * Gets the API-key from the secrets.json file.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The API-key from the secrets.json file.
     */
    @NotNull
    static String getAPIKey() throws IOException
    {
        if (!file.exists())
        {
            logger.error("The secrets.json file could not be found.", new FileNotFoundException());
        }
        else
        {
            logger.info("Opened secrets.json file and parsed it.");
        }

        String content = new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        return json.getString("API_KEY");
    }

    /**
     * Gets the session token from the secrets.json file.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The session token from the secrets.json file.
     */
    @NotNull
    static String getSessionToken() throws IOException
    {
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        return json.getString("SESSION_TOKEN");
    }

    /**
     * Gets the default domain from the secrets.json file.
     *
     * @throws IOException  If the request could not be executed due to cancellation,
     *                      a connectivity problem or timeout. Because networks can fail during an exchange,
     *                      it is possible that the remote server accepted the request before the failure.
     *
     * @return The default domain from the secrets.json file.
     */
    @NotNull
    static String getDefaultDomain() throws IOException
    {
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        return json.getString("DEFAULT_DOMAIN");
    }
}
