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
package dev.blocky.library.tixte.internal.interceptor;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Handles rate-limits using an {@link Interceptor}.
 *
 * @author BlockyDotJar
 * @version v1.0.3
 * @since v1.0.0-alpha.1
 */
public class RateLimitInterceptor implements Interceptor
{

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException
    {
        Response response = chain.proceed(chain.request());

        if (!response.isSuccessful() && response.code() == 429)
        {
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            response = chain.proceed(chain.request());
        }
        return response;
    }
}
