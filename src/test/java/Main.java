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
import dev.blocky.library.tixte.api.TixteClient;
import dev.blocky.library.tixte.api.TixteClientBuilder;
import dev.blocky.library.tixte.annotations.Undocumented;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.1
 */
@Undocumented
public class Main
{
    private static final File file = new File("C:\\Users\\Dominic\\Desktop\\Applications\\token\\secrets.json");

    @Undocumented
    public static void main(@NotNull String[] args) throws IOException
    {
        TixteClient client = TixteClientBuilder.createClient(getAPIKey(), getSessionToken(), getDefaultDomain());
        System.out.println(client.getRawTixteClient().getRawSize());
    }

    @NotNull
    @Undocumented
    public static String getAPIKey() throws IOException
    {
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);
        return json.getString("API_KEY");
    }

    @NotNull
    @Undocumented
    public static String getSessionToken() throws IOException
    {
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);
        return json.getString("SESSION_TOKEN");
    }

    @NotNull
    @Undocumented
    public static String getDefaultDomain() throws IOException
    {
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);
        return json.getString("DEFAULT_DOMAIN");
    }
}
