package com.academicdashboard.backend.checklist;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Checkpoint {
    
    String pointId;
    String content;
    boolean isComplete;
    boolean isSubpoint;
    List<Checkpoint> subCheckpoints;

    //Constructor: Checkpoint is Created
    public Checkpoint(
            String pointId,
            String content,
            boolean isComplete,
            boolean isSubpoint
        ) {
        
        this.pointId = pointId;
        this.content = content;
        this.isComplete = isComplete;
        this.isSubpoint = isSubpoint;
        this.subCheckpoints = new ArrayList<>();
    }
}
