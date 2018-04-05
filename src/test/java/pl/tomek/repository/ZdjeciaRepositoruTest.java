package pl.tomek.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.tomek.model.Zdjecia;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ZdjeciaRepositoruTest {

    @Autowired
    private ZdjeciaRepositoru zdjeciaRepositoru;

    private Zdjecia zdjecia;
    @Before
    public void setUp() throws Exception {
    zdjecia=new Zdjecia(1L,"adres");
    }

    @Test
    public void findAll() {
        Assert.assertNotNull(zdjeciaRepositoru.findAll());
    }
}