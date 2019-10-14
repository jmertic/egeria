/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.admin;

import org.odpi.openmetadata.accessservices.dataplatform.client.DataPlatformClient;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.dataplatformservices.api.DataPlatformConnectorBase;
import org.odpi.openmetadata.dataplatformservices.auditlog.DataPlatformServicesAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPlatformOperationalServices {

    private static final Logger log = LoggerFactory.getLogger(DataPlatformOperationalServices.class);


    private String localServerName;               /* Initialized in constructor */
    private String localServerUserId;             /* Initialized in constructor */
    private String localServerPassword;           /* Initialized in constructor */
    private String localServerType;               /* Initialized in constructor */
    private String localOrganizationName;         /* Initialized in constructor */
    private String localServerURL;                /* Initialized in constructor */

    private OMRSAuditLog auditLog;
    private OpenMetadataTopicConnector dataPlatformServiceOutTopicConnector;
    private DataPlatformConnectorBase dataPlatformConnectorBase;
    private DataPlatformConfig dataPlatformConfig;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName   the local server name
     * @param localServerUserId the local server user id
     * @param localServerType   the local server type
     * @param localServerURL    the local server url
     */
    public DataPlatformOperationalServices(String localServerName, String localServerUserId, String localServerType, String localServerURL) {
        this.localServerName = localServerName;
        this.localServerUserId = localServerUserId;
        this.localServerType = localServerType;
        this.localServerURL = localServerURL;
    }

    public void initialize(DataPlatformConfig dataPlatformConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException{

        final String actionDescription = "initialize";

        if (dataPlatformConfig != null) {
            this.auditLog = auditLog;
            this.dataPlatformConfig=dataPlatformConfig;

            DataPlatformServicesAuditCode auditCode = DataPlatformServicesAuditCode.SERVICE_INITIALIZING;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

            DataPlatformClient dataPlatformClient;
            try {
                dataPlatformClient = new DataPlatformClient(
                        dataPlatformConfig.getDataPlatformServerName(),
                        dataPlatformConfig.getDataPlatformServerURL(),
                        localServerUserId,
                        localServerPassword
                );
            } catch (InvalidParameterException error) {
                throw new OMAGConfigurationErrorException(error.getReportedHTTPCode(),
                        this.getClass().getName(),
                        actionDescription,
                        error.getErrorMessage(),
                        error.getReportedSystemAction(),
                        error.getReportedUserAction(),
                        error);
            }
            /*
             * Configuring the Data Platform Services out topic connector
             */
            if (dataPlatformConfig.getDataPlatformServiceOutTopic() != null) {
                try {
                    dataPlatformServiceOutTopicConnector = getTopicConnector(dataPlatformConfig.getDataPlatformServiceOutTopic(), auditLog);
                } catch (Exception e) {
                    auditCode = DataPlatformServicesAuditCode.ERROR_INITIALIZING_DP_IN_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            /*
             * Configuring the Data Platform connector
             */
            Connection dataPlatformConnection = dataPlatformConfig.getDataPlatformConnection();

            if (dataPlatformConnection != null) {
                try {
                    ConnectorBroker connectorBroker = new ConnectorBroker();
                    dataPlatformConnectorBase =(DataPlatformConnectorBase) connectorBroker.getConnector(dataPlatformConnection);
                    log.debug("Found connection: ", dataPlatformConnection);
                } catch (Exception e) {
                    auditCode = DataPlatformServicesAuditCode.ERROR_INITIALIZING_DP_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            /*
             Starting the Out Topic Connector
             */
            if (dataPlatformServiceOutTopicConnector != null) {
                try {
                    dataPlatformServiceOutTopicConnector.start();
                    auditCode = DataPlatformServicesAuditCode.OUTBOUND_TOPIC_CONNECTOR_INITIALIZED;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());

                } catch (Exception e) {
                    auditCode = DataPlatformServicesAuditCode.ERROR_INITIALIZING_DP_IN_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }
            auditCode = DataPlatformServicesAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }


    /**
     * Shutdown the Data Platform Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {
        DataPlatformServicesAuditCode auditCode;
        try {

            // Disconnect the cassandra connector
            dataPlatformConnectorBase.disconnect();
            dataPlatformServiceOutTopicConnector.disconnect();
            auditCode = DataPlatformServicesAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return true;
        } catch (Exception e) {
            auditCode = DataPlatformServicesAuditCode.ERROR_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return false;
        }
    }


    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection, OMRSAuditLog auditLog) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);
            topicConnector.setAuditLog(auditLog);

            return topicConnector;
        } catch (Exception error) {
            String methodName = "getTopicConnector";

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage("getTopicConnector");

            throw new OMRSConfigErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);

        }
    }

    public DataPlatformConfig getDataPlatformConfig() {
        return dataPlatformConfig;
    }
}