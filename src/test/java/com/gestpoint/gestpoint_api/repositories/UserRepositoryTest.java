package com.gestpoint.gestpoint_api.repositories;

import com.gestpoint.gestpoint_api.domain.User;
import com.gestpoint.gestpoint_api.dto.RegisterRequestDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User by email from DB")
    void findByEmailCase01() {
        String mail = "test@mail.com";
        RegisterRequestDTO data = new RegisterRequestDTO("John Doe", mail, "123456");
        this.createUser(data);

        Optional<User> result = this.userRepository.findByEmail(mail);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get User by email from DB when user does not exist")
    void findByEmailCase02() {
        String mail = "test@mail.com";

        Optional<User> result = this.userRepository.findByEmail(mail);

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(RegisterRequestDTO dto) {
        User newUser = new User();
        newUser.setName(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPasswordHash(dto.password());
        newUser.setRole("USER");
        newUser.setTenantId(1L);

        this.entityManager.persist(newUser);
        return newUser;
    }
}