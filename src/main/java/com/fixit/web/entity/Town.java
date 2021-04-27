package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "town")
public class Town extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lga_id")
    private Lga lga;

    @Size(min = 3, message = "Town name must be at least 3 characters")
    @NotBlank
    private String name;

    public Town() {
    }

    public Town(State state, Lga lga, String name) {
        this.state = state;
        this.lga = lga;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Lga getLga() {
        return lga;
    }

    public void setLga(Lga lga) {
        this.lga = lga;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Town{" +
                "id=" + id +
                ", state=" + state +
                ", lga=" + lga +
                ", name='" + name + '\'' +
                '}';
    }
}
