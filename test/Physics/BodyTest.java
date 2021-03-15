package Physics;

import org.junit.jupiter.api.Test;

import Physics.*;


import static org.junit.jupiter.api.Assertions.*;

class BodyTest {

    @Test
    void getSet() {
        Vector p = new Vector(1,1,1);
        Vector v = new Vector(2,2,2);
        Body b = new Body(10,p,v, 100);


    }
}