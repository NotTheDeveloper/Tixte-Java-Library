/**
 * Root package of all Tixte4J interceptors.
 *
 * <br>From here you can navigate to all interceptors.
 *
 * <ul>
 * <li>{@link dev.blocky.library.tixte.internal.interceptor.CacheInterceptor}
 * <br>Handles tixte caches using an {@link okhttp3.Interceptor}. (only if an internet connection is available).</li>
 *
 * <li>{@link dev.blocky.library.tixte.internal.interceptor.ErrorResponseInterceptor}
 * <br>Handles http error responses using an {@link okhttp3.Interceptor}.</li>
 *
 * <li>{@link dev.blocky.library.tixte.internal.interceptor.ForceCacheInterceptor}
 * <br>Handles tixte caches using an {@link okhttp3.Interceptor}.</li>
 *
 * <li>{@link dev.blocky.library.tixte.internal.interceptor.RateLimitInterceptor}
 * <br>Handles rate-limits using an {@link okhttp3.Interceptor}.</li>
 * </ul>
 */
package dev.blocky.library.tixte.internal.interceptor;