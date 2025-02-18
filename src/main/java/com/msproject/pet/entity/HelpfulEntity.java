package com.msproject.pet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Helpful")
@Entity
public class HelpfulEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;


    private boolean state;
}
