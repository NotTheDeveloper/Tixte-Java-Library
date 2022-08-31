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
package dev.blocky.library.tixte.api.enums;

/**
 * Policy which decides whether there should be created a cache or not.
 * <br>This will be called throughout Tixte4J when data gets constructed or modified and allows for a dynamically
 * adjusting cache.
 * <br>When {@link dev.blocky.library.tixte.api.TixteClient#pruneCache() TixteClient#pruneCache()} is called, the
 * configured policy will be used to unload any data that the policy has decided not to cache.
 * <br>This can be configured with {@link dev.blocky.library.tixte.api.TixteClientBuilder#setCachePolicy(CachePolicy)
 * TixteClientBuilder#setCachePolicy(CachePolicy)}.
 *
 * @see #NONE
 * @see #ALL
 * @see #ONLY_FORCE_CACHE
 * @see #ONLY_NETWORK_CACHE
 *
 * @author BlockyDotJar
 * @version v1.0.2
 * @since v1.0.0-beta.2
 */
public enum CachePolicy
{
    /**
     * Default cache-policy.
     * <br>Nothing will be cached.
     */
    NONE,

    /**
     * Only force-cache cache-policy.
     * <br>Caches will only be used, if the internet is not available.
     */
    ONLY_FORCE_CACHE,

    /**
     * Only network-cache cache-policy.
     * <br>Caches will only be used, if the internet is available.
     */
    ONLY_NETWORK_CACHE,

    /**
     * Every cache-policy.
     * <br>All caches will be used.
     */
    ALL
}
