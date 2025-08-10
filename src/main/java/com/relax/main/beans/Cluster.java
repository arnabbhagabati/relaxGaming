package com.relax.main.beans;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class Cluster {

    private Set<GridCell> cells;
    private int id;
    private String symbol;
    private BigDecimal payout;

    public Cluster(Set<GridCell> cells, int id, String symbol) {
        this.cells = cells;
        this.id = id;
        this.symbol = symbol;
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

    public BigDecimal getPayout() {
        return payout;
    }

    public void setPayout(BigDecimal payout) {
        this.payout = payout;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cluster cluster)) return false;
        return id == cluster.id && Objects.equals(cells, cluster.cells) && Objects.equals(symbol, cluster.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cells, id, symbol);
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", payout=" + payout +
                ", cells=" + cells +
                '}';
    }

}
