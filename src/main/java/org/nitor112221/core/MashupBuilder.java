package org.nitor112221.core;

import org.nitor112221.dto.Mashup;

public class MashupBuilder {
    private int numProblem;
    public MashupBuilder() {
        numProblem = 1;
    }

    MashupBuilder setNumProblem(int numProblem) {
        this.numProblem = numProblem;
        return this;
    }

     public Mashup build() {
        return new Mashup();
     }
}
