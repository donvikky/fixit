package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lga")
public class Lga extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @NotBlank(message = "Please provide a state name")
    @Size(min = 3, message = "State name must be at least 3 characters long")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lga", orphanRemoval = true)
    private List<Town> towns = new ArrayList<>();

    public Lga() {
    }

    public Lga(State state, String name) {
        this.state = state;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public void setTowns(List<Town> towns) {
        this.towns = towns;
    }

    @Override
    public String toString() {
        return "Lga{" +
                "id=" + id +
                ", stateId=" + state +
                ", name='" + name + '\'' +
                '}';
    }
}
