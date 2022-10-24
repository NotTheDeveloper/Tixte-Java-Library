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

import dev.blocky.library.logging.FallbackLogger;
import dev.blocky.library.tixte.api.TixteClient;
import dev.blocky.library.tixte.api.TixteClientBuilder;
import dev.blocky.library.tixte.api.enums.CachePolicy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import secrets.DONOTOPEN;

import java.io.IOException;

/**
 * Some basic examples, how to use an {@link TixteClient}.
 *
 * @author BlockyDotJar
 * @version v1.1.1
 * @since v1.0.0-beta.3
 */
public class BasicTixteClientExample
{
    private static final Logger logger = FallbackLogger.getLog(BasicTixteClientExample.class);

    public static void main(@NotNull String[] args) throws IOException
    {
        // Creates a *new* TixteClientBuilder instance.
        final TixteClientBuilder builder = new TixteClientBuilder()
                // Sets the API-key, which is required for most of the methods.
                // This method also sets the cache policy. I really recommend to set this to ALL.
                // If this is equal to null or not set, this will be automatically set to NONE.
                // If you use this method like here, you don't have to set it via setCachePolicy(@Nullable CachePolicy).
                .create(DONOTOPEN.getAPIKey(), CachePolicy.ALL)
                // Sets the session-token. (Optional but recommended)
                .setSessionToken(DONOTOPEN.getSessionToken())
                // Sets a default domain. (Optional)
                .setDefaultDomain(DONOTOPEN.getDefaultDomain());

        // Builds a *new* TixteClient instance and uses the provided API-key and session-token to start the login process.
        builder.build();

        logger.info("Successfully logged in!");
    }
}
