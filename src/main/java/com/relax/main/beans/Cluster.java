package com.relax.main.beans;

import java.util.Objects;
import java.util.Set;

public class Cluster {

    private Set<GridCell> cells;
    private int id;

    public Cluster(Set<GridCell> cells, int id) {
        this.cells = cells;
        this.id = id;
    }

    public Set<GridCell> getCells() {
        return cells;
    }

    public void setCells(Set<GridCell> cells) {
        this.cells = cells;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cluster cluster)) return false;
        return id == cluster.id && Objects.equals(cells, cluster.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cells, id);
    }
}
