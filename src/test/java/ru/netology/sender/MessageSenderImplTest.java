package ru.netology.sender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageSenderImplTest {

    private GeoService geoService;
    private LocalizationService localizationService;
    private MessageSenderImpl messageSender;

    @BeforeEach
    void setUp() {
        geoService = mock(GeoService.class);
        localizationService = mock(LocalizationService.class);
        messageSender = new MessageSenderImpl(geoService, localizationService);
    }

    @Test
    void testSendRussianTextForRussianIp() {
        // Указываем IP для России
        String russianIp = "172.0.32.11";

        // Мокаем поведение геосервиса и локализации
        when(geoService.byIp(russianIp))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        // Создаем заголовки с IP
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, russianIp);

        // Проверяем результат
        String result = messageSender.send(headers);
        assertEquals("Добро пожаловать", result);

        // Проверяем, что моки вызвались ровно 1 раз
        verify(geoService, times(1)).byIp(russianIp);
        verify(localizationService, times(1)).locale(Country.RUSSIA);
    }

    @Test
    void testSendEnglishTextForAmericanIp() {
        // Указываем IP для США
        String americanIp = "96.44.183.149";

        // Мокаем поведение геосервиса и локализации
        when(geoService.byIp(americanIp))
                .thenReturn(new Location("New York", Country.USA, null, 0));
        when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        // Создаем заголовки с IP
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, americanIp);

        // Проверяем результат
        String result = messageSender.send(headers);
        assertEquals("Welcome", result);

        // Проверяем, что моки вызвались ровно 1 раз
        verify(geoService, times(1)).byIp(americanIp);
        verify(localizationService, times(1)).locale(Country.USA);
    }

    @Test
    void testSendThrowsExceptionWhenIpIsMissing() {
        // Заголовки без IP
        Map<String, String> headers = new HashMap<>();

        // Проверяем, что метод выбрасывает исключение
        try {
            messageSender.send(headers);
        } catch (IllegalArgumentException e) {
            assertEquals("IP address is required", e.getMessage());
        }
    }
}

