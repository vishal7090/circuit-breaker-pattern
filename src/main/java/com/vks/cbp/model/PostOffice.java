package com.vks.cbp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostOffice {

    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public Object description;
    @JsonProperty("BranchType")
    public String branchType;
    @JsonProperty("DeliveryStatus")
    public String deliveryStatus;
    @JsonProperty("Circle")
    public String circle;
    @JsonProperty("District")
    public String district;
    @JsonProperty("Division")
    public String division;
    @JsonProperty("Region")
    public String region;
    @JsonProperty("Block")
    public String block;
    @JsonProperty("State")
    public String state;
    @JsonProperty("Country")
    public String country;
    @JsonProperty("Pincode")
    public String pincode;
}
