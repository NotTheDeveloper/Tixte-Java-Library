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
package dev.blocky.library.tixte.internal.requests;

import dev.blocky.library.tixte.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Utility class for handling network connectivity.
 *
 * @param hostName The host name.
 * @param port The port number.
 *
 * @author BlockyDotJar
 * @version v1.1.3
 * @since v1.0.0-alpha.3
 */
public record Network(@NotNull String hostName, int port)
{

    /**
     * Checks if you can connect to internet or not.
     *
     * @return <b>true</b> - If you can connect to internet.
     *         <br><b>false</b> - If you cannot connect to internet.
     */
    public boolean isInternetAvailable()
    {
        Checks.notEmpty(hostName, "hostName");
        Checks.noWhitespace(hostName, "hostName");

        Checks.notNegative(port, "port");

        try
        {
            final Socket socket = new Socket();
            final SocketAddress socketAddress = new InetSocketAddress(hostName, port);

            socket.connect(socketAddress, 1500);
            socket.close();
            return true;
        }
        catch (@NotNull IOException e)
        {
            return false;
        }
    }
}
