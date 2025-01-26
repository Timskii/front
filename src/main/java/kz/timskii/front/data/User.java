package kz.timskii.front.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(schema = "front", name = "user")
public class User  {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, updatable = false, length = 50)
    private String username;

    private String name;

    @Column(length = 20)
    private String password;

    @Column(length = 20)
    private String status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private OffsetDateTime createdAt = OffsetDateTime.now();

}
