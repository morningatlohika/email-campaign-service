package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "talks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @NotNull
    private MorningEvent event;

    @Column(unique = true)
    @NotEmpty
    private String title;

    @Column(unique = true)
    @NotEmpty
    private String theses;

    @OneToMany(mappedBy = "talk", cascade = CascadeType.ALL)
    private List<Speaker> speakers;
}
