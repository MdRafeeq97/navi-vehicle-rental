package org.example.models;

import lombok.Data;

@Data
public class Slot {
    private Integer startTime;
    private Integer endTime;
    private boolean booked;
}
