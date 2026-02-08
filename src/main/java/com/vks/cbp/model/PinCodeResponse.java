package com.vks.cbp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PinCodeResponse {

    @JsonProperty("Message")
    public String message;
    @JsonProperty("Status")
    public String status;
    @JsonProperty("PostOffice")
    public List<PostOffice> postOffice;
}
