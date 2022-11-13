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
package secrets;

import dev.blocky.library.tixte.internal.requests.json.DataObject;
import dev.blocky.library.tixte.internal.utils.logging.TixteLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * From this class you can get all data from a JSON file.
 *
 * @author BlockyDotJar
 * @version v1.3.3
 * @since v1.0.0-beta.3
 */
public class DONOTOPEN
{
    private static final Logger logger = TixteLogger.getLog(DONOTOPEN.class);
    private static final File file = new File("FILE_PATH");

    /**
     * Gets the API-key from the secrets.json file.
     *
     * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
     *
     * @return The API-key from the secrets.json file.
     */
    @NotNull
    public static String getAPIKey() throws IOException
    {
        if (!file.exists())
        {
            logger.error("The secrets.json file could not be found.", new FileNotFoundException());
        }
        else
        {
            logger.info("Opened secrets.json file and parsed it.");
        }

        return getString("API_KEY");
    }

    /**
     * Gets the session token from the secrets.json file.
     *
     * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
     *
     * @return The session token from the secrets.json file.
     */
    @NotNull
    public static String getSessionToken() throws IOException
    {
        return getString("SESSION_TOKEN");
    }

    /**
     * Gets the default domain from the secrets.json file.
     *
     * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
     *
     * @return The default domain from the secrets.json file.
     */
    @NotNull
    public static String getDefaultDomain() throws IOException
    {
        return getString("DEFAULT_DOMAIN");
    }

    @NotNull
    private static String getString(@NotNull String jsonObject) throws IOException
    {
        final String content = Files.readString(Paths.get(file.toURI()));
        final DataObject json = DataObject.fromJson(content);

        return json.getString(jsonObject);
    }
}
