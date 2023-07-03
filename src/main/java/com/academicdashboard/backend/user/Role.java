package com.academicdashboard.backend.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    private ObjectId id; //MongoDB ObjetId 

    private String authority; //STUDENT, PROFESSOR, USER

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
