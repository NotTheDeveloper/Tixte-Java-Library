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

import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * Represents HTTP-methods.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-beta.3
 */
@Internal
public enum Method
{

    /**
     * Represents the HTTP-method <b>DELETE</b>.
     */
    DELETE,

    /**
     * Represents the HTTP-method <b>GET</b>.
     */
    GET,

    /**
     * Represents the HTTP-method <b>POST</b>.
     */
    POST,

    /**
     * Represents the HTTP-method <b>PUT</b>.
     */
    PUT,

    /**
     * Represents the HTTP-method <b>PATCH</b>.
     */
    PATCH
}
