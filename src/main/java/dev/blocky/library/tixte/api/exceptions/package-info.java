/**
 * Root package of all Tixte4J exceptions.
 *
 * <br>From here you can navigate to all exceptions.
 *
 * <ul>
 * <li>{@link dev.blocky.library.tixte.api.exceptions.Forbidden}
 * <br>Exception thrown when a request is made to a Tixte API-endpoint that is not allowed.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.exceptions.HTTPException}
 * <br>Exception used for handling HTTP errors.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.exceptions.NotFound}
 * <br>Exception thrown when a Tixte API-request is made and the server returns a 404 (Not Found) response.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.exceptions.ParsingException}
 * <br>Exception used if the provided json is incorrectly formatted/an I/O error occurred/the type is incorrect/no value is
 * present for the specified key/the value is missing or null.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.exceptions.TixteServerException}
 * <br>Exception thrown when a 500 response code is returned from the Tixte server.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.exceptions.TixteWantsYourMoneyException}
 * <br>Exception thrown when a request is made to a Tixte API-endpoint that is not allowed, because you need
 * to have Tixte turbo/turbo-charged.</li>
 *
 * <li>{@link dev.blocky.library.tixte.api.exceptions.Unauthorized}
 * <br>Exception thrown when a request is made to a Tixte API-endpoint, but you are not authorized to do so.</li>
 * </ul>
 */
package dev.blocky.library.tixte.api.exceptions;