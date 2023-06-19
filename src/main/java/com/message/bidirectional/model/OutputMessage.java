package com.message.bidirectional.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OutputMessage {
    private String from;
    private String message;
    private String date;
}
