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
package dev.blocky.library.tixte.internal.requests;

/**
 * Utility class for creating {@link okhttp3.Request requests}.
 *
 * @author BlockyDotJar
 * @version v1.0.1
 * @since v1.0.0-alpha.3
 */
public class Route
{
    public static final String
            BASE_URL = "https://api.tixte.com/v1",
            SLASH = "/";

    /**
     * Constructs a <br>new</b> {@link Route}.
     * <br>This is a private constructor, because it should not be accessed for other classes.
     */
    private Route()
    {
    }

    /**
     * Represents every account endpoint of the Tixte API.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.3
     */
    public static class Account
    {
        public static final String
                ACCOUNT_ENDPOINT = "/users/@me",
                USERS_ENDPOINT = "/users/";
    }

    /**
     * Represents every domain endpoint of the Tixte API.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.3
     */
    public static class Domain
    {
        public static final String
                ACCOUNT_DOMAINS_ENDPOINT = "/users/@me/domains",
                DOMAINS_ENDPOINT = "/domains";
    }

    /**
     * Represents every file endpoint of the Tixte API.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.3
     */
    public static class File
    {
        public static final String
                FILE_ENDPOINT = "/users/@me/uploads",
                PAGE = "?page=",
                SIZE_ENDPOINT = "/users/@me/uploads/size",
                UPLOAD_ENDPOINT = "/upload";
    }

    /**
     * Represents every resource endpoint of the Tixte API.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.3
     */
    public static class Resources
    {
        public static final String
                GENERATE_DOMAIN_ENDPOINT = "/resources/generate-domain";
    }

    /**
     * Represents every config endpoint of the Tixte API.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-alpha.3
     */
    public static class Config
    {
        public static final String
                CONFIG_ENDPOINT = "/users/@me/config";
    }
}
