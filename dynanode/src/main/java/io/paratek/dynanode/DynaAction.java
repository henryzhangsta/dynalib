package io.paratek.dynanode;

import java.io.Serializable;

/**
 * Data Class
 * Stores the 4 parameters required for action hijacking
 *
 * @author Parametric
 */
public class DynaAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int p1, p2, p3, p4;

    public DynaAction(int p1, int p2, int p3, int p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }

    public int getP3() {
        return p3;
    }

    public int getP4() {
        return p4;
    }

}
