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

import com.google.errorprone.annotations.CheckReturnValue;
import dev.blocky.library.tixte.internal.utils.Checks;
import dev.blocky.library.tixte.internal.utils.Helpers;
import okhttp3.Request;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import static dev.blocky.library.tixte.api.TixteInfo.TIXTE_API_VERSION;
import static dev.blocky.library.tixte.internal.requests.Method.*;

/**
 * Utility class for creating {@link Request requests}.
 *
 * @author BlockyDotJar
 * @version v2.1.0
 * @since v1.0.0-alpha.3
 */
@Internal
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
     * Represents a custom HTTP-method to specific route.
     *
     * @param method The {@link Method} of the request.
     * @param route The route of the request.
     *
     * @return A custom HTTP-method to specific route.
     */
    @NotNull
    @CheckReturnValue
    public static Route custom(@NotNull Method method, @NotNull String route)
    {
        Checks.notNull(method, "Method");
        Checks.notEmpty(route, "Route");
        Checks.noWhitespace(route, "Route");

        return new Route(method, route);
    }

    /**
     * Represents a {@code delete}-request to a specific route.
     *
     * @param route The route of the request.
     *
     * @return A {@code delete}-request to a specific route.
     */
    @NotNull
    public static Route delete(@NotNull String route)
    {
        return custom(DELETE, route);
    }

    /**
     * Represents a {@code post}-request to a specific route.
     *
     * @param route The route of the request.
     *
     * @return A {@code post}-request to a specific route.
     */
    @NotNull
    public static Route post(@NotNull String route)
    {
        return custom(POST, route);
    }

    /**
     * Represents a {@code put}-request to a specific route.
     *
     * @param route The route of the request.
     *
     * @return A {@code put}-request to a specific route.
     */
    @NotNull
    public static Route put(@NotNull String route)
    {
        return custom(PUT, route);
    }

    /**
     * Represents a {@code patch}-request to a specific route.
     *
     * @param route The route of the request.
     *
     * @return A {@code patch}-request to a specific route.
     */
    @NotNull
    public static Route patch(@NotNull String route)
    {
        return custom(PATCH, route);
    }

    /**
     * Represents a {@code get}-request to a specific route.
     *
     * @param route The route of the request.
     *
     * @return A {@code get}-request to a specific route.
     */
    @NotNull
    public static Route get(@NotNull String route)
    {
        return custom(GET, route);
    }

    private static final String majorParameters = "user_data:asset_id:domain";
    private final int paramCount;
    private final Method method;
    private final String route;

    private Route(@NotNull Method method, @NotNull String route)
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
     * Gets the current used HTTP-method.
     *
     * @return The current used HTTP-method.
     */
    @Nullable
    @CheckReturnValue
    public Method getMethod()
    {
        return method;
    }

    /**
     * Gets the current used route.
     *
     * @return The current used route.
     */
    @Nullable
    @CheckReturnValue
    public String getRoute()
    {
        return route;
    }

    /**
     * Gets the amount of parameters in the current route.
     *
     * @return The amount of parameters in the current route.
     */
    public int getParamCount()
    {
        return paramCount;
    }

    /**
     * Gets the current route with all parameters replaced with the given values.
     *
     * @param params The parameters to compile the route with.
     *
     * @return The current {@link CompiledRoute} with all parameters replaced with the given values.
     */
    @NotNull
    public CompiledRoute compile(@NotNull String... params)
    {
        if (params.length != paramCount)
        {
            throw new IllegalArgumentException("Error compiling Route: [" + route + "], incorrect amount of parameters provided." +
                    "Expected: " + paramCount + ", Provided: " + params.length);
        }

        Set<String> major = new HashSet<>();
        StringBuilder compiledRoute = new StringBuilder(route);

        for (int i = 0; i < paramCount; i++)
        {
            int paramStart = compiledRoute.indexOf("{");
            int paramEnd = compiledRoute.indexOf("}");
            String paramName = compiledRoute.substring(paramStart + 1, paramEnd);

            if (majorParameters.contains(paramName))
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
            compiledRoute.replace(paramStart, paramEnd + 1, params[i]);
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
        if (!(o instanceof Route))
        {
            return false;
        }

        Route oRoute = (Route) o;

        return method.equals(oRoute.method) && route.equals(oRoute.route);
    }

    @NotNull
    @Override
    public String toString()
    {
        return method + "/" + route;
    }

    /**
     * Represents the current compiled route.
     *
     * @author BlockyDotJar
     * @version v1.3.0
     * @since v1.0.0-beta.3
     */
    public class CompiledRoute
    {
        private final boolean hasQueryParams;
        private final Route baseRoute;
        private final String major;
        private final String compiledRoute;

        private CompiledRoute(@NotNull Route baseRoute, @NotNull String compiledRoute, @NotNull String major, boolean hasQueryParams)
        {
            this.baseRoute = baseRoute;
            this.compiledRoute = compiledRoute;
            this.major = major;
            this.hasQueryParams = hasQueryParams;
        }

        private CompiledRoute(@NotNull Route baseRoute, @NotNull String compiledRoute, @NotNull String major)
        {
            this(baseRoute, compiledRoute, major, false);
        }

        /**
         * Gets the current route with all parameters replaced with the given values.
         * <br>Query parameter are for example: {@code ?param1=value1&param2=value2}
         *
         * @param params The parameters to compile the route with.
         *
         * @return The current {@link CompiledRoute} with all parameters replaced with the given values.
         */
        @NotNull
        @CheckReturnValue
        public CompiledRoute withQueryParams(@NotNull String... params)
        {
            Checks.check(params.length >= 2, "params length must be at least 2");
            Checks.check(params.length % 2 == 0, "params length must be a multiple of 2");

            StringBuilder newRoute = new StringBuilder(compiledRoute);

            for (int i = 0; i < params.length; i++)
            {
                newRoute.append(!hasQueryParams && i == 0 ? '?' : '&').append(params[i]).append('=').append(params[++i]);
            }

            return new CompiledRoute(baseRoute, newRoute.toString(), major, true);
        }

        /**
         * Gets every major parameter of the route.
         *
         * @return Every major parameter of the route.
         */
        @NotNull
        public String getMajorParameters()
        {
            return major;
        }

        /**
         * Gets the current compiled route.
         *
         * @return The current compiled route.
         */
        @NotNull
        public String getCompiledRoute()
        {
            return compiledRoute;
        }

        /**
         * Gets the current base route.
         *
         * @return The current base route.
         */
        @NotNull
        public Route getBaseRoute()
        {
            return baseRoute;
        }

        /**
         * Gets the current used HTTP-method.
         *
         * @return The current used HTTP-method.
         */
        @NotNull
        public Method getMethod()
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
            if (!(o instanceof CompiledRoute))
            {
                return false;
            }

            CompiledRoute oCompiled = (CompiledRoute) o;

            return baseRoute.equals(oCompiled.getBaseRoute()) && compiledRoute.equals(oCompiled.compiledRoute);
        }

        @NotNull
        @Override
        public String toString()
        {
            return "CompiledRoute(" + method + ": " + compiledRoute + ")";
        }
    }
}
