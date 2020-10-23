package com.theparkcorp.azureware3;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "AzureWareSurvey")
public class SurveyData {

    String PatientID;
    String Date;
    String StringSurvey;

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setStringSurvey(String stringSurvey) {
        StringSurvey = stringSurvey;
    }

    @DynamoDBAttribute(attributeName = "Survey Results")
    public String getStringSurvey() {
        return StringSurvey;
    }

    @DynamoDBRangeKey(attributeName = "Date")
    public String getDate() {
        return Date;
    }

    @DynamoDBHashKey(attributeName = "PatientID")
    public String getPatientID() {
        return PatientID;
    }
}
