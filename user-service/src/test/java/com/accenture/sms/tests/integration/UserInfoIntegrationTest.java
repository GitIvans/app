package com.accenture.sms.tests.integration;

import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    private UserInfoDTO testUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        testUser = new UserInfoDTO();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");

        jwtToken = "Bearer " + jwtUtil.generateToken(testUser.getUsername());
    }

    @Test
    public void testCreateUser() {
        // Создаем заголовки с токеном
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        HttpEntity<UserInfoDTO> request = new HttpEntity<>(testUser, headers);

        // Отправляем запрос на создание пользователя с заголовками
        ResponseEntity<String> response = restTemplate.postForEntity("/users", request, String.class);

        // Проверяем, что пользователь успешно создан
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("User created successfully"));
    }

    @Test
    public void testGetUserByUsername() {
        // Создаем заголовки с токеном
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        HttpEntity<UserInfoDTO> request = new HttpEntity<>(null, headers);

        // Получаем пользователя по имени пользователя
        ResponseEntity<UserInfoDTO> response = restTemplate.exchange("/users/username/" + testUser.getUsername(), HttpMethod.GET, request, UserInfoDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser.getUsername(), response.getBody().getUsername());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());
    }

    @Test
    public void testUpdateUser() {
        // Создаем заголовки с токеном
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        // Обновляем данные пользователя
        testUser.setUsername("updateduser");
        HttpEntity<UserInfoDTO> requestUpdate = new HttpEntity<>(testUser, headers);
        ResponseEntity<String> updateResponse = restTemplate.exchange("/users/username/" + testUser.getUsername(), HttpMethod.PUT, requestUpdate, String.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

        // Получаем обновленного пользователя
        ResponseEntity<UserInfoDTO> response = restTemplate.exchange("/users/username/" + testUser.getUsername(), HttpMethod.GET, requestUpdate, UserInfoDTO.class);
        assertEquals("updateduser", response.getBody().getUsername());
    }

    @Test
    public void testDeleteUser() {
        // Создаем заголовки с токеном
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        HttpEntity<String> requestDelete = new HttpEntity<>(null, headers);

        // Удаляем пользователя по имени пользователя
        ResponseEntity<String> deleteResponse = restTemplate.exchange("/users/username/" + testUser.getUsername(), HttpMethod.DELETE, requestDelete, String.class);

        // Проверяем успешное удаление
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertTrue(deleteResponse.getBody().contains("User deleted successfully"));

        // Проверяем, что пользователь больше не существует
        ResponseEntity<String> getResponse = restTemplate.exchange("/users/username/" + testUser.getUsername(), HttpMethod.GET, requestDelete, String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
