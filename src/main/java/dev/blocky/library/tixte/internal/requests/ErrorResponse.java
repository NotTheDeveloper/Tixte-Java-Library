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

import dev.blocky.library.tixte.annotations.Undocumented;
import dev.blocky.library.tixte.api.exceptions.*;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
@Undocumented
public class ErrorResponse
{

    @Undocumented
    protected ErrorResponse()
    {
    }

    @Undocumented
    protected static void checkResponse(@NotNull Response response) throws IOException
    {
        if (response.code() == 401)
        {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject error = json.getJSONObject("error");
            throw new Unauthorized("Unauthorized: " + error.getString("message"));
        }

        if (response.code() == 402)
        {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject error = json.getJSONObject("error");
            throw new TixteWantsYourMoneyException(error.getString("message"));
        }

        if (response.code() == 403)
        {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject error = json.getJSONObject("error");
            throw new Forbidden("Forbidden: " + error.getString("message"));
        }

        if (response.code() == 404)
        {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject error = json.getJSONObject("error");
            throw new NotFound("Not Found: " + error.getString("message"));
        }

        if (response.code() == 429)
        {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject error = json.getJSONObject("error");
            throw new TixteServerError("We got rate-limited: " + error.getString("message"));
        }

        if (response.code() == 500)
        {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject error = json.getJSONObject("error");
            throw new TixteServerError("Internal Server Error: " + error.toString());
        }

        if (response.code() != 401 && response.code() != 402 && response.code() != 403 && response.code() != 404 &&
                response.code() != 429 &&  response.code() != 500 && !response.isSuccessful())
        {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject error = json.getJSONObject("error");
            throw new HTTPException("HTTP Error: " + error.getString("code") + ", " + error.getString("message") +
                    (error.getString("code").equals("bad_request") ? ": " + error.getString("field") : ""));
        }
    }
}
