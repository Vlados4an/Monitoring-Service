package ru.erma.dto;

import java.util.Map;
public record ReadingRequest(String username,Integer month,Integer year,Map<String,Integer> values) {

}
