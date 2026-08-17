package com.demo.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class IdentifierGenerator {

    public static final String ORDER_PREFIX = "ORD";
    public static final String SHIPMENT_PREFIX = "TRK";
    public static final String PAYMENT_PREFIX = "TXN";

    public static String generate(String prefix, String metadata){
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); //20260711
        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();
        return prefix + "-" + date + "-" + metadata + "-" + suffix;
    }
}
