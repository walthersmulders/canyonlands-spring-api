package com.walthersmulders.persistance.entity;

import com.walthersmulders.mapstruct.dto.author.AuthorUpsert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
public class AuthorEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "author_id", nullable = false)
    private UUID authorID;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "additional_name", length = 100)
    private String additionalName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorBookEntity> books;

    public boolean checkUpdateDtoEqualsEntity(AuthorUpsert authorUpsert) {
        return this.firstName.equals(authorUpsert.firstName()) &&
               this.lastName.equals(authorUpsert.lastName());
    }
}
