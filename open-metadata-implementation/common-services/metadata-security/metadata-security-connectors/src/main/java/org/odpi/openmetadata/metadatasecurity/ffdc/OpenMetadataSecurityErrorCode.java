/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OpenMetadataSecurityErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with open metadata security connectors.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA   Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>403 - forbidden</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum OpenMetadataSecurityErrorCode
{
    BAD_PLATFORM_SECURITY_CONNECTION(400, "OMAG-PLATFORM-SECURITY-400-001 ",
                                     "The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1}",
                                     "The system is unable to validate the users issuing platform requests.",
                                     "Review the error message to determine the cause of the problem."),

    BAD_SERVER_SECURITY_CONNECTION(400, "OMAG-PLATFORM-SECURITY-400-002 ",
                                   "The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}",
                                   "The system is unable to validate the users issuing requests to this server.",
                                   "Review the error message to determine the cause of the problem."),

    UNAUTHORIZED_PLATFORM_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-001 ",
                                 "User {0} is not authorized to issue request {1}",
                                 "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                                 "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_SERVER_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-002 ",
                                "User {0} is not authorized to issue a request to server {1}",
                                "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                                "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_SERVICE_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-003 ",
                                "User {0} is not authorized to issue {1} requests",
                                "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                                "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_ASSET_FEEDBACK(403, "OMAG-PLATFORM-SECURITY-403-004 ",
                             "User {0} is not authorized to attach feedback to asset {1}",
                             "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                             "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_ZONE_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-005 ",
                             "User {0} is not authorized to change the zone membership for asset {1} from {2} to {3}",
                             "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                             "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_CONNECTION_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-006 ",
                             "User {0} is not authorized to access connection {1}",
                             "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                             "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_ASSET_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-007 ",
                             "User {0} is not authorized to access asset {1}",
                             "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                             "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_ASSET_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-008 ",
                              "User {0} is not authorized to change asset {1}",
                              "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                              "The request fails with a UserNotAuthorizedException exception."),

    INCOMPLETE_ASSET(         403, "OMAG-PLATFORM-SECURITY-403-009 ",
                              "User {0} is not authorized to change asset {1} because it has missing properties",
                              "The system is unable to process a request from the user because the asset is not correctly or completely filled out.",
                              "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_TYPE_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-010 ",
                              "User {0} is not authorized to access open metadata type {1} ({2}) on server {3}",
                              "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                              "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_TYPE_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-011 ",
                             "User {0} is not authorized to change open metadata type {1} ({2}) on server {3}",
                             "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                             "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_INSTANCE_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-012 ",
                             "User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}",
                             "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                             "The request fails with a UserNotAuthorizedException exception."),

    UNAUTHORIZED_INSTANCE_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-013 ",
                             "User {0} is not authorized to change open metadata instance {1} of type {2} on server {3}",
                             "The system is unable to process a request from the user because they do not have access to the necessary services and/or resources.",
                             "The request fails with a UserNotAuthorizedException exception."),

    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(OpenMetadataSecurityErrorCode.class);


    /**
     * The constructor for OpenMetadataSecurityErrorCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataSecurityErrorCode above.   For example:
     *
     *     OpenMetadataSecurityErrorCode   errorCode = OpenMetadataSecurityErrorCode.BAD_PLATFORM_SECURITY_CONNECTION;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    OpenMetadataSecurityErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
    {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params   strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== OpenMetadataSecurityErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> OpenMetadataSecurityErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "OpenMetadataSecurityErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
