package com.theparkcorp.azureware3;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;


@DynamoDBTable(tableName = "AzureWare")
public class PatientData {

    String PatientID;
    String Date;
    String TremorRating;

    String Emotion;
    String Speech;

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTremorRating(String tremorRating) {
        TremorRating = tremorRating;
    }

    @DynamoDBAttribute(attributeName = "Tremor Rating")
    public String getTremorRating() {
        return TremorRating;
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
