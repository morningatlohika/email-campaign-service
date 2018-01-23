package com.lohika.morning.ecs.domain.speaker;

import com.lohika.morning.ecs.domain.talk.Talk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "speakers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    //TODO: workaround to avoid Derby exception. Should be removed when the schema is defined in SQL file
    @ColumnDefault("''")
    private String firstName;

    @NotEmpty
    //TODO: workaround to avoid Derby exception. Should be removed when the schema is defined in SQL file
    @ColumnDefault("''")
    private String lastName;

    @NotEmpty
    private String company;

    private String position;

    private String webProfileUrl;

    @NotEmpty
    @Column(length = 1000)
    private String about;

    @NotEmpty
    @Column
    //TODO: workaround to avoid Derby exception. Should be removed when the schema is defined in SQL file
    @ColumnDefault("''")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "talkId")
    private Talk talk;
}
