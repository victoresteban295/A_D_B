package com.academicdashboard.backend.professor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OfficeHrs {

    private String building;
    private String room;
    private String startTime;
    private String endTime;
    private List<String> days;
}
