package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Forum implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String location;
    private String contact;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy hh:mm a ")
    private String date;
}