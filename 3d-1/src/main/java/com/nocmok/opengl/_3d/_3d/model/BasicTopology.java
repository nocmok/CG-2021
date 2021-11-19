package com.nocmok.opengl._3d._3d.model;

import com.nocmok.opengl._3d._3d.model.ModelTopology;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BasicTopology implements ModelTopology {

    private final int[][] topology;

    public BasicTopology(int[][] topology) {
        this.topology = topology;
    }

    @Override public Iterator<int[]> getPolygons() {
        return new Iterator<int[]>() {

            int next = 0;

            @Override public boolean hasNext() {
                return next < topology.length;
            }

            @Override public int[] next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return topology[next++];
            }
        };
    }
}
