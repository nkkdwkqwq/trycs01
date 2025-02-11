package flik;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Assert.*;

public class test {
    @Test
    public void Steveproblemtest(){
        int i = 0;
        int j = 0;
        while(i<200){
            i++;
            j++;
            assertTrue(Flik.isSameNumber(i,j));
        }

    }

}
