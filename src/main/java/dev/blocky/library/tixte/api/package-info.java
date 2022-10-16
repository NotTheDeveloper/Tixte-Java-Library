/**
 * Root package of Tixte4J.
 * <br>From here you can navigate to every important class/package.
 *
 * <ul>
 * <li>{@link dev.blocky.library.tixte.api.Domains}
 * <br>Represents the 'Domains' tab of the Tixte dashboard and everything else what Tixte offers you with domains.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.Embed}
 * <br>Represents an embed displayed by Discord.
 * <br>This class has many possibilities for null values, so be careful!</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.EmbedEditor}
 * <br>Builder system used to build {@link dev.blocky.library.tixte.api.Embed embeds}.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.MyFiles}
 * <br>Represents the 'My Files' tab of the Tixte dashboard and everything else what Tixte offers you with files.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.PageDesign}
 * <br>Represents the 'Page Design' tab of the Tixte dashboard.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.RawResponseData}
 * <br>Represents the raw response data from Tixte API-requests.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.SelfUser}
 * <br>Represents your Tixte user-account.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.TixteClient}
 * <br>The core of Tixte4J.
 * <br>Acts as a getting system of Tixte4J.
 * <br>All parts of the API can be accessed starting from this class.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.TixteClientBuilder}
 * <br>Used to create <b>new</b> {@link dev.blocky.library.tixte.api.TixteClient TixteClient} instances.
 *
 * <p>A single {@link dev.blocky.library.tixte.api.TixteClientBuilder} can be reused multiple times.
 * <br>Each call to {@link dev.blocky.library.tixte.api.TixteClientBuilder#build()} creates a <b>new</b>
 * {@link dev.blocky.library.tixte.api.TixteClient} instance using the same information.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.TixteInfo}
 * <br>Contains information to this specific build of Tixte4J.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.User}
 * <br>Represents a Tixte user-account.</li>
 * </ul>
 */
package dev.blocky.library.tixte.api;
