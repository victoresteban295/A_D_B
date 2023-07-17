package com.academicdashboard.backend.checklist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.academicdashboard.backend.user.User;
import com.academicdashboard.backend.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestData {

    private final UserRepository userRepository;
    private final GrouplistRepository grouplistRepository;
    private final ChecklistRepository checklistRepository;
    private final CheckpointRepository checkpointRepository;

    public void cleanupDatabase() {
        checkpointRepository.deleteAll();
        checklistRepository.deleteAll();
        grouplistRepository.deleteAll();
        userRepository.deleteAll();
    }

    public void populateDatabase() {

        Checkpoint subcheckpointA11A = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdA11A")
                .content("ContentA11A")
                .isComplete(false)
                .isSubpoint(true)
                .subCheckpoints(new ArrayList<>())
                .build());
        Checkpoint subcheckpointA11B = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdA11B")
                .content("ContentA11B")
                .isComplete(false)
                .isSubpoint(true)
                .subCheckpoints(new ArrayList<>())
                .build());

        List<Checkpoint> A11Subcheckpoints = new ArrayList<>();
        A11Subcheckpoints.add(subcheckpointA11A);
        A11Subcheckpoints.add(subcheckpointA11B);

        Checkpoint checkpointA11 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdA11")
                .content("ContentA11")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(A11Subcheckpoints)
                .build());
        Checkpoint checkpointA12 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdA12")
                .content("ContentA12")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());

        List<Checkpoint> A1Checkpoints = new ArrayList<>();
        A1Checkpoints.add(checkpointA11);
        A1Checkpoints.add(checkpointA12);

        Checklist checklistA1 = checklistRepository.insert(
                Checklist.builder()
                .listId("listIdA1")
                .title("Checklist TitleA1")
                .checkpoints(A1Checkpoints)
                .build());

        Checkpoint checkpointA21 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdA21")
                .content("ContentA21")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());
        Checkpoint checkpointA22 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdA22")
                .content("ContentA22")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());

        List<Checkpoint> A2Checkpoints = new ArrayList<>();
        A1Checkpoints.add(checkpointA21);
        A1Checkpoints.add(checkpointA22);

        Checklist checklistA2 = checklistRepository.insert(
                Checklist.builder()
                .listId("listIdA2")
                .title("Checklist TitleA2")
                .checkpoints(A2Checkpoints)
                .build());

        List<Checklist> AChecklist = new ArrayList<>();
        AChecklist.add(checklistA1);
        AChecklist.add(checklistA2);

        Grouplist grouplistA = grouplistRepository.insert(
                Grouplist.builder()
                .groupId("groupIdA")
                .title("Grouplist TitleA")
                .checklists(AChecklist)
                .build());

        Checkpoint subcheckpointB11A = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdB11A")
                .content("ContentB11A")
                .isComplete(false)
                .isSubpoint(true)
                .subCheckpoints(new ArrayList<>())
                .build());
        Checkpoint subcheckpointB11B = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdB11B")
                .content("ContentB11B")
                .isComplete(false)
                .isSubpoint(true)
                .subCheckpoints(new ArrayList<>())
                .build());

        List<Checkpoint> B11Subcheckpoints = new ArrayList<>();
        A11Subcheckpoints.add(subcheckpointB11A);
        A11Subcheckpoints.add(subcheckpointB11B);

        Checkpoint checkpointB11 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdB11")
                .content("ContentB11")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(B11Subcheckpoints)
                .build());
        Checkpoint checkpointB12 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdB12")
                .content("ContentB12")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());

        List<Checkpoint> B1Checkpoints = new ArrayList<>();
        A1Checkpoints.add(checkpointB11);
        A1Checkpoints.add(checkpointB12);

        Checklist checklistB1 = checklistRepository.insert(
                Checklist.builder()
                .listId("listIdB1")
                .title("Checklist TitleB1")
                .checkpoints(B1Checkpoints)
                .build());

        Checkpoint checkpointB21 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdB21")
                .content("ContentB21")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());
        Checkpoint checkpointB22 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdB22")
                .content("ContentB22")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());

        List<Checkpoint> B2Checkpoints = new ArrayList<>();
        A1Checkpoints.add(checkpointB21);
        A1Checkpoints.add(checkpointB22);

        Checklist checklistB2 = checklistRepository.insert(
                Checklist.builder()
                .listId("listIdB2")
                .title("Checklist TitleB2")
                .checkpoints(B2Checkpoints)
                .build());

        List<Checklist> BChecklist = new ArrayList<>();
        AChecklist.add(checklistB1);
        AChecklist.add(checklistB2);

        Grouplist grouplistB = grouplistRepository.insert(
                Grouplist.builder()
                .groupId("groupIdB")
                .title("Grouplist TitleB")
                .checklists(BChecklist)
                .build());



        Checkpoint subcheckpointC11A = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdC11A")
                .content("ContentC11A")
                .isComplete(false)
                .isSubpoint(true)
                .subCheckpoints(new ArrayList<>())
                .build());
        Checkpoint subcheckpointC11B = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdC11B")
                .content("ContentC11B")
                .isComplete(false)
                .isSubpoint(true)
                .subCheckpoints(new ArrayList<>())
                .build());
        List<Checkpoint> C11Subcheckpoints = new ArrayList<>();
        C11Subcheckpoints.add(subcheckpointC11A);
        C11Subcheckpoints.add(subcheckpointC11B);

        Checkpoint checkpointC11 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdC11")
                .content("ContentC11")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(C11Subcheckpoints)
                .build());
        Checkpoint checkpointC12 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdC12")
                .content("ContentC12")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());
        List<Checkpoint> C1Checkpoints = new ArrayList<>();
        C1Checkpoints.add(checkpointC11);
        C1Checkpoints.add(checkpointC12);
        
        Checklist checklistC1 = checklistRepository.insert(
                Checklist.builder()
                .listId("listIdC1")
                .title("Checklist TitleC1")
                .checkpoints(C1Checkpoints)
                .build());


        Checkpoint checkpointD1 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdD1")
                .content("ContentD1")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());
        Checkpoint checkpointD2 = checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("pointIdD2")
                .content("ContentD2")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build());
        List<Checkpoint> DCheckpoints = new ArrayList<>();
        DCheckpoints.add(checkpointD1);
        DCheckpoints.add(checkpointD2);

        Checklist checklistD = checklistRepository.insert(
                Checklist.builder()
                .listId("listIdD")
                .title("Checklist TitleD")
                .checkpoints(DCheckpoints)
                .build());

        List<Grouplist> grouplists = new ArrayList<>();
        grouplists.add(grouplistA);
        grouplists.add(grouplistB);

        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklistC1);
        checklists.add(checklistD);

        userRepository.insert(
                User.builder()
                .userId("ju7db63uy678erdybncpo")
                .firstname("Tony")
                .lastname("Stark")
                .email("tonystark@email.com")
                .username("tonystark295")
                .checklists(checklists)
                .grouplists(grouplists)
                .build());

    } 
}
