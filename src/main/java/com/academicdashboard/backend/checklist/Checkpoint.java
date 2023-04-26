package com.academicdashboard.backend.checklist;

import java.time.LocalDate;

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
public class Checkpoint {

    //Checkpoint's Detail
    private String content; 
    private boolean isComplete;
    private LocalDate dateCompleted;

    //Constructor: Called each time user creates a new checkpoint
    public Checkpoint(String content, boolean isComplete) {
        this.content = content;
        this.isComplete = isComplete;
    }
}
