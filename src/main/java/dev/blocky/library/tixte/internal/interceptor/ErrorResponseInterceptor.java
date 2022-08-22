/**
 * Copyright 2022 Dominic (aka. BlockyDotJar)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.blocky.library.tixte.internal.interceptor;

import dev.blocky.library.tixte.api.exceptions.*;
import dev.blocky.library.tixte.internal.requests.json.DataObject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Handles http error responses using an {@link Interceptor}.
 *
 * @author BlockyDotJar
 * @version v2.0.0
 * @since v1.0.0-alpha.3
 */
public class ErrorResponseInterceptor implements Interceptor
{

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException
    {
        Request request = chain.request();
        Response response = chain.proceed(request);

        DataObject json = DataObject.fromJson(response.peekBody(2048).string());

        if (!response.isSuccessful())
        {
            DataObject error = json.getDataObject("error");

            switch (response.code())
            {
            case 401:
                throw new Unauthorized(error.getString("message"));
            case 402:
                throw new TixteWantsYourMoneyException("Payment required: " + error.getString("message"));
            case 403:
                throw new Forbidden(error.getString("message"));
            case 404:
                throw new NotFound(error.getString("message"));
            case 429:
                throw new TixteServerException("We got rate-limited: " + error.getString("message"));
            case 500:
                throw new TixteServerException("Internal Server Error: " + error.getString("message"));
            default:
                throw new HTTPException(error.getString("code") + ", " + error.getString("message"));
            }
        }
        return response;
    }
}
