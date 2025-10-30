package com.example.embabeldemo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "massage_therapist")
@NoArgsConstructor
@AllArgsConstructor
public class MassageTherapist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 4000)
    private String description;

    private String specialties; // e.g., "Deep tissue, Sports"

    @Override
    public String toString() {
        return "MassageTherapist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", specialties='" + specialties + '\'' +
                '}';
    }
}

