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
package dev.blocky.library.tixte.internal.utils;

import dev.blocky.library.tixte.annotations.Undocumented;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Utility class for handling network connectivity.
 *
 * @author BlockyDotJar
 * @version v1.0.0
 * @since v1.0.0-alpha.3
 */
public strictfp class Network
{
    private final transient String hostName;
    private final transient int port;

    @Undocumented
    private Network(@NotNull String hostName, int port)
    {
        this.hostName = hostName;
        this.port = port;
    }

    @NotNull
    @Undocumented
    public static Network createNetwork(@NotNull String hostName, int port)
    {
        return new Network(hostName, port);
    }

    @Undocumented
    public boolean isInternetAvailable()
    {
        try
        {
            int timeout = 1500;
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(hostName, port);

            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
