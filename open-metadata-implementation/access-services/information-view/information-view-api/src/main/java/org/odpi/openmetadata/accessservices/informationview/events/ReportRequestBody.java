/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequestBody extends InformationViewHeader {

    @Valid
    @NotNull
    private DeployedReport report;
    private String registrationGuid;
    private String registrationQualifiedName;

    public DeployedReport getReport() {
        return report;
    }

    public void setReport(DeployedReport report) {
        this.report = report;
    }

    public String getRegistrationGuid() {
        return registrationGuid;
    }

    public void setRegistrationGuid(String registrationGuid) {
        this.registrationGuid = registrationGuid;
    }


    public String getRegistrationQualifiedName() {
        return registrationQualifiedName;
    }

    public void setRegistrationQualifiedName(String registrationQualifiedName) {
        this.registrationQualifiedName = registrationQualifiedName;
    }

    @Override
    public String toString() {
        return "{" +
                "report=" + report +
                ", registrationGuid='" + registrationGuid + '\'' +
                ", registrationQualifiedName='" + registrationQualifiedName + '\'' +
                '}';
    }
}
