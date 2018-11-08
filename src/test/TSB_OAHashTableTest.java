package test;

import datos.TSB_OAHashtable;
import entidades.Contador;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TSB_OAHashTableTest {
    private TSB_OAHashtable<String, Contador> tsb_oaHashtable;

    @Before
    public void setUp(){
        tsb_oaHashtable = new TSB_OAHashtable<>();
/*
        tsb_oaHashtable.put("Palabra1", new Contador());
        tsb_oaHashtable.put("Palabra2", new Contador());
        tsb_oaHashtable.put("Palabra3", new Contador(4));
        tsb_oaHashtable.put("Palabra4", new Contador(5));*/
    }

    @Test
    public void sizeTest(){
        Assert.assertEquals(4, this.tsb_oaHashtable.size());
        this.tsb_oaHashtable.remove("Palabra1");
        this.tsb_oaHashtable.remove("Palabra2");
        Assert.assertEquals(2, this.tsb_oaHashtable.size());
    }

    @Test
    public void putTest(){
        this.tsb_oaHashtable.clear();
        Assert.assertEquals(0, this.tsb_oaHashtable.size());
        this.tsb_oaHashtable.put("Palabra1", new Contador(5));
        this.tsb_oaHashtable.put("Palabra2", new Contador());
        this.tsb_oaHashtable.put("Palabrotas", new Contador());
        Assert.assertEquals(3, this.tsb_oaHashtable.size());
        Assert.assertEquals(5, this.tsb_oaHashtable.get("Palabra1").getFrecuencia());

        System.out.println(this.tsb_oaHashtable.toString());
    }
}
