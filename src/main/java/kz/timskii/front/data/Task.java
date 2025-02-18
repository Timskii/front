package kz.timskii.front.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(schema = "front", name = "task")
public class Task {
    @Id
    @UuidGenerator
    private UUID id;

    private String name ;
    private String title ;
    private String label ;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
}
