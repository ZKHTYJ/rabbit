package com.cctang.util;

public enum BuiltinExchangeType {
    DIRECT("direct"),
    FANOUT("fanout"),
    TOPIC("topic"),
    HEADERS("header"),
    XLARGE("x-delayed-message");
    private final String type ;
    BuiltinExchangeType(String type) {
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
