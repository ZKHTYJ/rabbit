package com.cctang.rabbitmq.utils;

public enum BuiltinExchangeType {
    DIRECT("direct"), FANOUT("fanout"), TOPIC("topic"),HEADERS("header");
    private final String type ;
    BuiltinExchangeType(String type) {
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
