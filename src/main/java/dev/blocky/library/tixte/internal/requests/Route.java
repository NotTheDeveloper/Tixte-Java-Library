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

import dev.blocky.library.tixte.annotations.Undocumented;

/**
 * @author BlockyDotJar
 * @version v1.0.0-alpha.1
 * @since v1.0.0-alpha.3
 */
@Undocumented
public class Route
{

    public static final String
            BASE_URL = "https://api.tixte.com/v1",
            SLASH = "/";

    @Undocumented
    private Route()
    {
    }

    @Undocumented
    public static class Account
    {
        public static final String
                ACCOUNT_ENDPOINT = "/users/@me",
                USERS_ENDPOINT = "/users/",
                KEYS_ENDPOINT = "/users/@me/keys/";
    }

    @Undocumented
    public static class Domain
    {
        public static final String
                ACCOUNT_DOMAINS_ENDPOINT = "/users/@me/domains",
                DOMAINS_ENDPOINT = "/domains";
    }

    @Undocumented
    public static class File
    {
        public static final String
                FILE_ENDPOINT = "/users/@me/uploads",
                PAGE = "?page=",
                SIZE_ENDPOINT = "/users/@me/uploads/size",
                UPLOAD_ENDPOINT = "/upload";
    }

    @Undocumented
    public static class Resources
    {
        public static final String
                GENERATE_DOMAIN_ENDPOINT = "/resources/generate-domain";
    }

    @Undocumented
    public static class Config
    {
        public static final String
                CONFIG_ENDPOINT = "/users/@me/config";
    }
}
