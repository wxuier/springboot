package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {
    @JsonProperty("name")
    private String name;
    @JsonProperty("team")
    private String team;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
