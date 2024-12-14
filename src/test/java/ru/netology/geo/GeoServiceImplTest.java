package ru.netology.geo;

import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeoServiceImplTest {

    private final GeoServiceImpl geoService = new GeoServiceImpl();

    @Test
    void testByIpForRussia() {
        Location location = geoService.byIp(GeoServiceImpl.MOSCOW_IP);
        assertEquals(Country.RUSSIA, location.getCountry());
        assertEquals("Moscow", location.getCity());
    }

    @Test
    void testByIpForUSA() {
        Location location = geoService.byIp(GeoServiceImpl.NEW_YORK_IP);
        assertEquals(Country.USA, location.getCountry());
        assertEquals("New York", location.getCity());
    }

    @Test
    void testByIpForUnknownIp() {
        Location location = geoService.byIp("127.0.0.1");
        assertEquals(null, location.getCountry());
    }
}
