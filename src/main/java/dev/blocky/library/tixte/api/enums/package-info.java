/**
 * Root package of all Tixte4J enums.
 * <br>From here you can navigate to all enums.
 *
 * <ul>
 * <li>{@link dev.blocky.library.tixte.api.enums.CachePolicy}
 * <br>Policy which decides whether there should be created a cache or not.
 * <br>This will be called throughout Tixte4J when data gets constructed or modified and allows for a dynamically
 * adjusting cache.
 * <br>When {@link dev.blocky.library.tixte.api.TixteClient#pruneCache() TixteClient#pruneCache()} is called, the
 * configured policy will be used to unload any data that the policy has decided not to cache.
 * <br>This can be configured with {@link dev.blocky.library.tixte.api.TixteClientBuilder#setCachePolicy(CachePolicy)
 * TixteClientBuilder#setCachePolicy(CachePolicy)}.</li>
 * </ul>
 */
package dev.blocky.library.tixte.api.enums;
