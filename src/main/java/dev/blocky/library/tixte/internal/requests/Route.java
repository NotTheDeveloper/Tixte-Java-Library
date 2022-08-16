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
 * @version v2.0.1
 * @since v1.0.0-alpha.3
 */
@Internal
public class Route
{
    public static final String TIXTE_API_PREFIX = Helpers.format("https://api.tixte.com/v%d/", TIXTE_API_VERSION);

    public static class Self
    {
        public static final Route GET_SELF                  = new Route(GET,      "users/@me");
        public static final Route GET_KEYS                  = new Route(GET,      "users/@me/keys");
        public static final Route GET_EXPERIMENTS           = new Route(GET,      "users/@me/experiments");
        public static final Route GET_CONFIG                = new Route(GET,      "users/@me/config");
        public static final Route PATCH_CONFIG              = new Route(PATCH,    "users/@me/config");
        public static final Route GET_DOMAINS               = new Route(GET,      "users/@me/domains");
        public static final Route ADD_DOMAIN                = new Route(PATCH,    "users/@me/domains/{domain}");
        public static final Route DELETE_DOMAIN             = new Route(DELETE,   "users/@me/domains/{domain}");
        public static final Route GET_UPLOADS               = new Route(GET,      "users/@me/uploads");
        public static final Route GET_UPLOAD_SIZE           = new Route(GET,      "users/@me/uploads/size");
        public static final Route DELETE_FILE               = new Route(DELETE,   "users/@me/uploads/{asset_id}");

        @Experimental //  Can't be used yet by everyone, so i am not able to implement it already.
        public static final Route SEARCH_FILE               = new Route(POST,      "users/@me/uploads/search");
    }

    public static class Users
    {
        public static final Route GET_USER                  = new Route(GET,      "users/{user_data}");
    }

    public static class File
    {
        public static final Route UPLOAD_FILE               = new Route(POST,     "upload");
    }

    public static class Domain
    {
        public static final Route GET_DOMAINS               = new Route(GET,     "domains");
    }

    public static class Resources
    {
        public static final Route GET_GENERATED_DOMAIN     = new Route(GET,     "resources/generate-domain");
    }

    @NotNull
    public static Route custom(@NotNull Method method, @NotNull String route)
    {
        Checks.notNull(method, "Method");
        Checks.notEmpty(route, "Route");
        Checks.noWhitespace(route, "Route");

        return new Route(method, route);
    }

    @NotNull
    public static Route delete(@NotNull String route)
    {
        return custom(DELETE, route);
    }

    @NotNull
    public static Route post(@NotNull String route)
    {
        return custom(POST, route);
    }

    @NotNull
    public static Route put(@NotNull String route)
    {
        return custom(PUT, route);
    }

    @NotNull
    public static Route patch(@NotNull String route)
    {
        return custom(PATCH, route);
    }

    @NotNull
    public static Route get(@NotNull String route)
    {
        return custom(GET, route);
    }

    private static final String majorParameters = "user_data:asset_id:domain";
    private final String route;
    private final Method method;
    private final int paramCount;

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

    @Nullable
    @CheckReturnValue
    public Method getMethod()
    {
        return method;
    }

    @Nullable
    @CheckReturnValue
    public String getRoute()
    {
        return route;
    }

    public int getParamCount()
    {
        return paramCount;
    }

    @NotNull
    public CompiledRoute compile(@NotNull String... params)
    {
        if (params.length != paramCount)
        {
            throw new IllegalArgumentException("Error Compiling Route: [" + route + "], incorrect amount of parameters provided." +
                    "Expected: " + paramCount + ", Provided: " + params.length);
        }

        Set<String> major = new HashSet<>();
        StringBuilder compiledRoute = new StringBuilder(route);

        for (int i = 0; i < paramCount; i++)
        {
            int paramStart = compiledRoute.indexOf("{");
            int paramEnd = compiledRoute.indexOf("}");
            String paramName = compiledRoute.substring(paramStart+1, paramEnd);

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
        return new CompiledRoute(this, compiledRoute.toString(), major.isEmpty() ? "n/a" : String.join(":", major));
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

    public class CompiledRoute
    {
        private final Route baseRoute;
        private final String major;
        private final String compiledRoute;
        private final boolean hasQueryParams;

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

        @NotNull
        public String getMajorParameters()
        {
            return major;
        }


        @NotNull
        public String getCompiledRoute()
        {
            return compiledRoute;
        }

        @NotNull
        public Route getBaseRoute()
        {
            return baseRoute;
        }

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
