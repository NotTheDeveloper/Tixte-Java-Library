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
 * <li>{@link dev.blocky.library.tixte.internal.requests.json.DataPath}
 * <br>This utility class can be used to access nested values within {@link dev.blocky.library.tixte.internal.requests.json.DataObject DataObjects}
 * and {@link dev.blocky.library.tixte.internal.requests.json.DataArray DataArrays}.
 *
 * <p><b>Path expression grammar</b>
 *
 * <br>The syntax for paths is given by this grammar:
 *
 * <pre>{@code
 * <name-syntax>  ::= /[^.\[\]]+/;
 * <index-syntax> ::= "[" <number> "]";
 * <name>         ::= <name-syntax> | <name-syntax> "?";
 * <index>        ::= <index-syntax> | <index-syntax> "?";
 * <element>      ::= <name> ( <index> )*;
 * <path-start>   ::= <element> | <index> ( <index> )*;
 * <path>         ::= <path-start> ( "." <element> )*;
 * }</pre>
 *
 * <p><b>Examples</b>
 * <br>Given a JSON object such as:
 * <pre>{@code
 * {
 *     "array": [{
 *         "foo": "bar",
 *     }]
 * }
 * }</pre>
 *
 * The content of {@code "foo"} can be accessed using the code:
 * <pre>{@code String foo = DataPath.getString(root, "array[0].foo")}</pre>
 *
 * <p>With the safe-access operator {@code "?"}, you can also allow missing values within your path:
 * <pre>{@code String foo = DataPath.getString(root, "array[1]?.foo", "default")}</pre>
 * This will result in {@code foo == "default"}, since the array element 1 is marked as optional, and missing in the actual object.</li>
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
