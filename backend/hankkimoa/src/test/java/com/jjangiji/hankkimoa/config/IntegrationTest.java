package com.jjangiji.hankkimoa.config;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        jdbcTemplate.execute("DELETE FROM expense");
        jdbcTemplate.execute("DELETE FROM expense_emoji");
        jdbcTemplate.execute("DELETE FROM expense_saving_goal");
        jdbcTemplate.execute("DELETE FROM opening_hour");
        jdbcTemplate.execute("DELETE FROM restaurant");
        jdbcTemplate.execute("DELETE FROM restaurant_image");
        jdbcTemplate.execute("DELETE FROM category");
        jdbcTemplate.execute("DELETE FROM users");

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
