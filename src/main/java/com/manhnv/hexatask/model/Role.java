package com.manhnv.hexatask.model;

import com.manhnv.hexatask.model.sequence.Sequenceable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
public class Role implements Sequenceable {
    @Transient
    public static final String SEQUENCE_NAME = "roles_sequence";

    @Id
    private Long id;

    private String name;

}

