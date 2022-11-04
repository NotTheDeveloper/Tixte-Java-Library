/**
 * Copyright 2022 Dominic R. (aka. BlockyDotJar), Florian Spieß (aka. MinnDevelopment) and
 * Austin Keener (aka. DV8FromTheWorld)
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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.Helpers;
import okhttp3.Request;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dev.blocky.library.tixte.api.TixteInfo.TIXTE_API_VERSION;
import static dev.blocky.library.tixte.internal.requests.HTTPMethods.*;

/**
 * Utility class for creating {@link Request requests}.
 *
 * @author MinnDevelopment and BlockyDotJar
 * @version v2.2.1
 * @since v1.0.0-alpha.3
 */
public class Route
{
    public static final String TIXTE_API_PREFIX = Helpers.format("https://api.tixte.com/v%d/", TIXTE_API_VERSION);

    /**
     * Represents your Tixte user-account.
     *
     * @author BlockyDotJar
     * @version v1.3.0
     * @since v1.0.0-beta.3
     */
    public static class Self
    {
        public static final Route GET_SELF = new Route(GET, "users/@me");
        public static final Route GET_KEYS = new Route(GET, "users/@me/keys");
        public static final Route GET_EXPERIMENTS = new Route(GET, "users/@me/experiments");
        public static final Route GET_CONFIG = new Route(GET, "users/@me/config");
        public static final Route PATCH_CONFIG = new Route(PATCH, "users/@me/config");
        public static final Route GET_DOMAINS = new Route(GET, "users/@me/domains");
        public static final Route ADD_DOMAIN = new Route(PATCH, "users/@me/domains/{domain}");
        public static final Route DELETE_DOMAIN = new Route(DELETE, "users/@me/domains/{domain}");
        public static final Route GET_UPLOADS = new Route(GET, "users/@me/uploads");
        public static final Route PURGE_FILES = new Route(DELETE, "users/@me/uploads");
        public static final Route GET_UPLOAD_SIZE = new Route(GET, "users/@me/uploads/size");
        public static final Route DELETE_FILE = new Route(DELETE, "users/@me/uploads/{asset_id}");


        //  Can't be used yet by everyone, so I am not able to implement it already.

        @Experimental
        public static final Route SEARCH_FILE = new Route(POST, "users/@me/uploads/search");

        @Experimental
        public static final Route GET_FOLDERS = new Route(GET, "users/@me/folders");
    }

    /**
     * Represents a Tixte user-account.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-beta.3
     */
    public static class Users
    {
        public static final Route GET_USER = new Route(GET, "users/{user_data}");
    }

    /**
     * Represents everything what Tixte offers you with files.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-beta.3
     */
    public static class File
    {
        public static final Route UPLOAD_FILE = new Route(POST, "upload");
    }

    /**
     * Represents everything what Tixte offers you with domains.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-beta.3
     */
    public static class Domain
    {
        public static final Route GET_DOMAINS = new Route(GET, "domains");
    }

    /**
     * Represents everything what Tixte offers you with resources.
     *
     * @author BlockyDotJar
     * @version v1.0.0
     * @since v1.0.0-beta.3
     */
    public static class Resources
    {
        public static final Route GET_GENERATED_DOMAIN = new Route(GET, "resources/generate-domain");
    }

    /**
     * Create a route template for the given HTTP method.
     *
     * <p>Route syntax should include valid argument placeholders of the format: {@code '{' argument_name '}'}
     * <br>These are the correct names of major parameters:
     * <ul>
     *     <li>{@code user_data} for user routes</li>
     *     <li>{@code asset_id} for upload routes</li>
     *     <li>{@code domain} for domain routes</li>
     * </ul>
     *
     * For example, to compose the route to create a message in a channel:
     * <pre>{@code
     * Route route = Route.custom(HTTPMethods.DELETE, "users/@me/uploads/{asset_id}");
     * }</pre>
     *
     * <p>To compile the route, use {@link #compile(String...)} with the positional arguments.
     * <pre>{@code
     * Route.CompiledRoute compiled = route.compile(assetId);
     * }</pre>
     *
     * @param method The HTTP method.
     * @param route The route template with valid argument placeholders.
     *
     * @throws IllegalArgumentException If null is provided or the route is invalid (containing spaces or empty)
     *
     * @return The custom route template.
     */
    @NotNull
    public static Route custom(@NotNull HTTPMethods method, @NotNull String route)
    {
        Checks.notNull(method, "Method");
        Checks.notEmpty(route, "Route");
        Checks.noWhitespace(route, "Route");

        return new Route(method, route);
    }

    /**
     * Create a route template for the with the {@link HTTPMethods#DELETE DELETE} method.
     *
     * <p>Route syntax should include valid argument placeholders of the format: {@code '{' argument_name '}'}
     * <br>These are the correct names of major parameters:
     * <ul>
     *     <li>{@code user_data} for user routes</li>
     *     <li>{@code asset_id} for upload routes</li>
     *     <li>{@code domain} for domain routes</li>
     * </ul>
     *
     * For example, to compose the route to delete a message in a channel:
     * <pre>{@code
     * Route route = Route.custom(HTTPMethods.DELETE, "channels/{channel_id}/messages/{message_id}");
     * }</pre>
     *
     * <p>To compile the route, use {@link #compile(String...)} with the positional arguments.
     * <pre>{@code
     * Route.CompiledRoute compiled = route.compile(assetId);
     * }</pre>
     *
     * @param route The route template with valid argument placeholders
     *
     * @throws IllegalArgumentException If null is provided or the route is invalid (containing spaces or empty)
     *
     * @return The custom route template
     */
    @NotNull
    public static Route delete(@NotNull String route)
    {
        return custom(DELETE, route);
    }

    /**
     * Create a route template for the with the {@link HTTPMethods#POST POST} method.
     *
     * <p>Route syntax should include valid argument placeholders of the format: {@code '{' argument_name '}'}
     * <br>These are the correct names of major parameters:
     * <ul>
     *     <li>{@code user_data} for user routes</li>
     *     <li>{@code asset_id} for upload routes</li>
     *     <li>{@code domain} for domain routes</li>
     * </ul>
     *
     * For example, to compose the route to create a message in a channel:
     * <pre>{@code
     * Route route = Route.custom(HTTPMethods.POST, "upload");
     * }</pre>
     *
     * <p>To compile the route, use {@link #compile(String...)} with the positional arguments.
     * <pre>{@code
     * Route.CompiledRoute compiled = route.compile();
     * }</pre>
     *
     * @param route The route template with valid argument placeholders
     *
     * @throws IllegalArgumentException If null is provided or the route is invalid (containing spaces or empty)
     *
     * @return The custom route template
     */
    @NotNull
    public static Route post(@NotNull String route)
    {
        return custom(POST, route);
    }

    /**
     * Create a route template for the with the {@link HTTPMethods#PUT PUT} method.
     *
     * <p>Route syntax should include valid argument placeholders of the format: {@code '{' argument_name '}'}
     * <br>These are the correct names of major parameters:
     * <ul>
     *     <li>{@code user_data} for user routes</li>
     *     <li>{@code asset_id} for upload routes</li>
     *     <li>{@code domain} for domain routes</li>
     * </ul>
     *
     * For example, to compose the route to ban a user in a guild:
     * <pre>{@code
     * Route route = Route.custom(HTTPMethods.PUT, "users/@me/domains/{domain}");
     * }</pre>
     *
     * <p>To compile the route, use {@link #compile(String...)} with the positional arguments.
     * <pre>{@code
     * Route.CompiledRoute compiled = route.compile(domain);
     * }</pre>
     *
     * @param route The route template with valid argument placeholders
     *
     * @throws IllegalArgumentException If null is provided or the route is invalid (containing spaces or empty)
     *
     * @return The custom route template
     */
    @NotNull
    public static Route put(@NotNull String route)
    {
        return custom(PUT, route);
    }

    /**
     * Create a route template for the with the {@link HTTPMethods#PATCH PATCH} method.
     *
     * <p>Route syntax should include valid argument placeholders of the format: {@code '{' argument_name '}'}
     * <br>These are the correct names of major parameters:
     * <ul>
     *     <li>{@code user_data} for user routes</li>
     *     <li>{@code asset_id} for upload routes</li>
     *     <li>{@code domain} for domain routes</li>
     * </ul>
     *
     * For example, to compose the route to edit a message in a channel:
     * <pre>{@code
     * Route route = Route.custom(HTTPMethods.PATCH, "users/@me/domains/{domain}");
     * }</pre>
     *
     * <p>To compile the route, use {@link #compile(String...)} with the positional arguments.
     * <pre>{@code
     * Route.CompiledRoute compiled = route.compile(domain);
     * }</pre>
     *
     * @param route The route template with valid argument placeholders
     *
     * @throws IllegalArgumentException If null is provided or the route is invalid (containing spaces or empty)
     *
     * @return The custom route template
     */
    @NotNull
    public static Route patch(@NotNull String route)
    {
        return custom(PATCH, route);
    }

    /**
     * Create a route template for the with the {@link HTTPMethods#GET GET} method.
     *
     * <p>Route syntax should include valid argument placeholders of the format: {@code '{' argument_name '}'}
     * <br>These are the correct names of major parameters:
     * <ul>
     *     <li>{@code user_data} for user routes</li>
     *     <li>{@code asset_id} for upload routes</li>
     *     <li>{@code domain} for domain routes</li>
     * </ul>
     *
     * For example, to compose the route to get a message in a channel:
     * <pre>{@code
     * Route route = Route.custom(HTTPMethods.GET, "users/{user_data}");
     * }</pre>
     *
     * <p>To compile the route, use {@link #compile(String...)} with the positional arguments.
     * <pre>{@code
     * Route.CompiledRoute compiled = route.compile(userData);
     * }</pre>
     *
     * @param route The route template with valid argument placeholders
     *
     * @throws IllegalArgumentException If null is provided or the route is invalid (containing spaces or empty)
     *
     * @return The custom route template
     */
    @NotNull
    public static Route get(@NotNull String route)
    {
        return custom(GET, route);
    }

    /**
     * The known major parameters.
     */
    public static final List<String> MAJOR_PARAMETER_NAMES = Helpers.listOf(
            "user_data", "asset_id", "domain"
    );
    private final String route;
    private final HTTPMethods method;
    private final int paramCount;

    private Route(@NotNull HTTPMethods method, @NotNull String route)
    {
        this.method = method;
        this.route = route;
        this.paramCount = Helpers.countMatches(route, '{');

        if (paramCount != Helpers.countMatches(route, '}'))
        {
            throw new IllegalArgumentException("An argument does not have both {}'s for route: " + method + "  " + route);
        }
    }

    /**
     * The {@link HTTPMethods} of this route template.
     * <br>Multiple routes with different HTTP methods can share a rate-limit.
     *
     * @return The HTTP method.
     */
    @NotNull
    public HTTPMethods getHTTPMethod()
    {
        return method;
    }

    /**
     * The route template with argument placeholders.
     *
     * @return The route template.
     */
    @NotNull
    public String getRoute()
    {
        return route;
    }

    /**
     * The number of parameters for this route, not including query parameters.
     *
     * @return The parameter count.
     */
    public int getParamCount()
    {
        return paramCount;
    }

    /**
     * Compile the route with provided parameters.
     * <br>The number of parameters must match the number of placeholders in the route template.
     * The provided arguments are positional and will replace the placeholders of the template in order of appearance.
     *
     * <p>Use {@link CompiledRoute#withQueryParams(String...)} to add query parameters to the route.
     *
     * @param params The parameters to compile the route with.
     *
     * @throws IllegalArgumentException If the number of parameters does not match the number of placeholders, or null is provided.
     *
     * @return The compiled route, ready to use for rate-limit handling.
     */
    @NotNull
    public CompiledRoute compile(@NotNull String... params)
    {
        if (params.length != paramCount)
        {
            throw new IllegalArgumentException("Error compiling Route: [" + route + "], incorrect amount of parameters provided." +
                    "Expected: " + paramCount + ", Provided: " + params.length);
        }

        final Set<String> major = new HashSet<>();
        final StringBuilder compiledRoute = new StringBuilder(route);

        for (int i = 0; i < paramCount; i++)
        {
            final int paramStart = compiledRoute.indexOf("{");
            final int paramEnd = compiledRoute.indexOf("}");

            final String paramName = compiledRoute.substring(paramStart + 1, paramEnd);

            if (MAJOR_PARAMETER_NAMES.contains(paramName))
            {
                if (params[i].length() > 30)
                {
                    major.add(paramName + "=" + Integer.toUnsignedString(params[i].hashCode()));
                }
                else
                {
                    major.add(paramName + "=" + params[i]);
                }
            }
            compiledRoute.replace(paramStart, paramEnd + 1, URLEncoder.encode(params[i], StandardCharsets.UTF_8));
        }
        return new CompiledRoute(this, compiledRoute.toString(), major.isEmpty() ? "N/A" : String.join(":", major));
    }

    @Override
    public int hashCode()
    {
        return (route + method.toString()).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o)
    {
        if (!(o instanceof Route oRoute))
        {
            return false;
        }

        return method.equals(oRoute.method) && route.equals(oRoute.route);
    }

    @NotNull
    @Override
    public String toString()
    {
        return method + "/" + route;
    }

    /**
     * Represents a route compiled with arguments.
     *
     * @see Route#compile(String...)
     *
     * @author BlockyDotJar
     * @version v1.4.0
     * @since v1.0.0-beta.3
     */
    public class CompiledRoute
    {
        private final Route baseRoute;
        private final String major;
        private final String compiledRoute;
        private final List<String> query;

        private CompiledRoute(@NotNull Route baseRoute, @NotNull String compiledRoute, @NotNull String major)
        {
            this.baseRoute = baseRoute;
            this.compiledRoute = compiledRoute;
            this.major = major;
            this.query = null;
        }

        private CompiledRoute(@NotNull CompiledRoute original, @NotNull List<String> query)
        {
            this.baseRoute = original.baseRoute;
            this.compiledRoute = original.compiledRoute;
            this.major = original.major;
            this.query = query;
        }

        /**
         * Returns a copy of this {@link CompiledRoute} with the provided parameters added as query.
         * <br>This will use <a href="https://en.wikipedia.org/wiki/Percent-encoding" target="_blank">percent-encoding</a>
         * for all provided <em>values</em> but not for the keys.
         *
         * <p><b>Example Usage</b><br>
         * <pre>{@code
         * Route.CompiledRoute uploads = Route.GET_UPLOADS.compile();
         *
         * // Returns a *new* route.
         * route = uploads.withQueryParams(
         *   "page", 1
         * );
         * // Adds another parameter on top of page.
         * route = route.withQueryParams(
         *   "amount", 5
         * );
         *
         * // Now the route has both page and amount, you can also do this in one call:
         * route = uploads.withQueryParams(
         *   "page", 1,
         *   "amount", 5
         * );
         * }</pre>
         *
         * @param params The parameters to add as query, alternating key and value (see example)
         *
         * @throws IllegalArgumentException If the number of arguments is not even or null is provided.
         *
         * @return A copy of this CompiledRoute with the provided parameters added as query.
         */
        @NotNull
        @CheckReturnValue
        public CompiledRoute withQueryParams(@NotNull String... params)
        {
            Checks.notNull(params, "params");
            Checks.check(params.length >= 2, "Params length must be at least 2");
            Checks.check((params.length & 1) == 0, "Params length must be a multiple of 2");

            List<String> newQuery;

            if (query == null)
            {
                newQuery = new ArrayList<>(params.length / 2);
            }
            else
            {
                newQuery = new ArrayList<>(query.size() + params.length / 2);
                newQuery.addAll(query);
            }

            for (int i = 0; i < params.length; i += 2)
            {
                newQuery.add(params[i] + '=' + URLEncoder.encode(params[i + 1], StandardCharsets.UTF_8));
            }

            return new CompiledRoute(this, newQuery);
        }

        /**
         * The string of major parameters used by this route.
         * <br>This is important for rate-limit handling.
         *
         * @return The string of major parameters used by this route.
         */
        @NotNull
        public String getMajorParameters()
        {
            return major;
        }

        /**
         * The compiled route string of the endpoint,
         * including all arguments and query parameters.
         *
         * @return The compiled route string of the endpoint.
         */
        @NotNull
        public String getCompiledRoute()
        {
            if (query == null)
            {
                return compiledRoute;
            }

            return compiledRoute + '?' + String.join("&", query);
        }

        /**
         * The route template with the original placeholders.
         *
         * @return The route template with the original placeholders.
         */
        @NotNull
        public Route getBaseRoute()
        {
            return baseRoute;
        }

        /**
         * The HTTP method.
         *
         * @return The HTTP method.
         */
        @NotNull
        public HTTPMethods getHTTPMethod()
        {
            return baseRoute.method;
        }

        @Override
        public int hashCode()
        {
            return (compiledRoute + method).hashCode();
        }

        @Override
        public boolean equals(@Nullable Object o)
        {
            if (!(o instanceof CompiledRoute oCompiled))
            {
                return false;
            }

            return baseRoute.equals(oCompiled.getBaseRoute()) && compiledRoute.equals(oCompiled.compiledRoute);
        }

        @NotNull
        @Override
        public String toString()
        {
            return method + "/" + route;
        }
    }
}
