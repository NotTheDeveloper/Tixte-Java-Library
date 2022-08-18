/**
 * Root package of all Tixte4J json objects.
 *
 * <br>From here you can navigate to all json objects.
 *
 * <ul>
 * <li>{@link dev.blocky.library.tixte.internal.requests.json.DataArray}
 * <br>Represents a list of values used in communication with the Tixte API.
 * <br>Throws {@link java.lang.IndexOutOfBoundsException} if provided with index out of bounds.
 * <br>This class is not thread-safe.</li>
 *
 * <li>{@link dev.blocky.library.tixte.internal.requests.json.DataObject}
 * <br>Represents a map of values used in communication with the Tixte API.
 * <br>Throws {@link java.lang.NullPointerException}, if a parameter annotated with {@link org.jetbrains.annotations.NotNull}
 * is provided with {@code null}.
 * <br>This class is not thread-safe.</li>
 *
 * <li>{@link dev.blocky.library.tixte.internal.requests.json.DataType}
 * <br>Enum constants representing possible types for a {@link dev.blocky.library.tixte.internal.requests.json.DataObject} value.</li>
 *
 * <li>{@link dev.blocky.library.tixte.internal.requests.json.SerializableArray}
 * <br>Allows custom serialization for JSON payloads of an array.</li>
 *
 * <li>{@link dev.blocky.library.tixte.internal.requests.json.SerializableData}
 * <br>Allows custom serialization for JSON payloads of an object.</li>
 * </ul>
 */
package dev.blocky.library.tixte.internal.requests.json;