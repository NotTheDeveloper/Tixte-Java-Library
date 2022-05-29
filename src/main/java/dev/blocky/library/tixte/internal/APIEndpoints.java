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
package dev.blocky.library.tixte.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
public class APIEndpoints
{

    protected APIEndpoints()
    {
    }

    protected static final String
            BASE_URL = "https://api.tixte.com/v1",
            ACCOUNT_ENDPOINT = "/users/@me",
            UPLOAD_ENDPOINT = "/upload",
            FILE_ENDPOINT = "/users/@me/uploads",
            DOMAINS_ENDPOINT = "/users/@me/domains",
            SIZE_ENDPOINT = "/users/@me/uploads/size";

    @NotNull
    protected static String getBaseUrl()
    {
        return BASE_URL;
    }

    @NotNull
    protected static String getAccountEndpoint()
    {
        return ACCOUNT_ENDPOINT;
    }

    @NotNull
    protected static String getUploadEndpoint()
    {
        return UPLOAD_ENDPOINT;
    }

    @NotNull
    protected static String getFileEndpoint()
    {
        return FILE_ENDPOINT;
    }

    @NotNull
    protected static String getDomainsEndpoint()
    {
        return DOMAINS_ENDPOINT;
    }

    @NotNull
    protected static String getSizeEndpoint()
    {
        return SIZE_ENDPOINT;
    }
}
