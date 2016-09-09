package com.mysampleapp.demo.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "waffletest-mobilehub-1010075498-Waffles")

public class WafflesDO {

    private static final String tableName = "waffletest-mobilehub-1010075498-Waffles";
    private Double _qId;
    private Double _term;
    private String _caption;
    private String _opt1;
    private String _opt2;
    private String _ownerId;
    private Double _vote1;
    private Double _vote2;

    @DynamoDBAttribute(attributeName = "caption")
    public String get_caption() { return _caption; }

    public void set_caption(String _caption) { this._caption = _caption; }

    @DynamoDBHashKey(attributeName = "qId")
    @DynamoDBAttribute(attributeName = "qId")
    public Double getQId() {
        return _qId;
    }

    public void setQId(final Double _qId) {
        this._qId = _qId;
    }
    @DynamoDBRangeKey(attributeName = "term")
    @DynamoDBAttribute(attributeName = "term")
    public Double getTerm() {
        return _term;
    }

    public void setTerm(final Double _term) {
        this._term = _term;
    }
    @DynamoDBAttribute(attributeName = "opt1")
    public String getOpt1() {
        return _opt1;
    }

    public void setOpt1(final String _opt1) {
        this._opt1 = _opt1;
    }
    @DynamoDBAttribute(attributeName = "opt2")
    public String getOpt2() {
        return _opt2;
    }

    public void setOpt2(final String _opt2) {
        this._opt2 = _opt2;
    }
    @DynamoDBIndexHashKey(attributeName = "ownerId", globalSecondaryIndexName = "Owner")
    public String getOwnerId() {
        return _ownerId;
    }

    public void setOwnerId(final String _ownerId) {
        this._ownerId = _ownerId;
    }
    @DynamoDBAttribute(attributeName = "vote1")
    public Double getVote1() {
        return _vote1;
    }

    public void setVote1(final Double _vote1) {
        this._vote1 = _vote1;
    }
    @DynamoDBAttribute(attributeName = "vote2")
    public Double getVote2() {
        return _vote2;
    }

    public void setVote2(final Double _vote2) {
        this._vote2 = _vote2;
    }

    public static String getTableName(){
        return tableName;
    }

    @Override
    public String toString() {
        return "WafflesDO{" +
                "_qId=" + _qId +
                ", _term=" + _term +
                ", _caption=" + _caption +
                ", _opt1='" + _opt1 + '\'' +
                ", _opt2='" + _opt2 + '\'' +
                ", _ownerId='" + _ownerId + '\'' +
                ", _vote1=" + _vote1 +
                ", _vote2=" + _vote2 +
                '}';
    }
}
