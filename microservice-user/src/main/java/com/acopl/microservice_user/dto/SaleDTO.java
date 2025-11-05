package com.acopl.microservice_user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SaleDTO {

    private Long id;
    private Date dateTime;
    private float total;
    private Long clientID;
}
