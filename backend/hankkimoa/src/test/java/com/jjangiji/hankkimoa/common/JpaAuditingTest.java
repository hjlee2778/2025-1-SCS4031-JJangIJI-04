package com.jjangiji.hankkimoa.common;

import com.jjangiji.hankkimoa.config.CommonTest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JpaAuditingTest implements CommonTest {

    @Autowired
    private TestRepository testRepository;

    @DisplayName("JPA Auditing 성공")
    @Test
    void jpaAuditing() {
        // given
        TestEntity testEntity = new TestEntity("한끼모아");

        // when
        TestEntity savedEntity = testRepository.save(testEntity);

        // then
        assertAll(
                () -> assertThat(savedEntity.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(savedEntity.getModifiedAt()).isBefore(LocalDateTime.now())
        );
    }

    @DisplayName("JPA Auditing 성공 : Entity 값 변경 시 수정 시간이 modifiedAt에 반영된다.")
    @Test
    void jpaAuditing_modifyEntity() {
        // given
        TestEntity testEntity = new TestEntity("한끼모아");
        TestEntity savedEntity = testRepository.save(testEntity);

        // when
        savedEntity.setName("밥톨");
        TestEntity updatedEntity = testRepository.save(savedEntity);

        // then
        assertAll(
                () -> assertThat(savedEntity.getCreatedAt()).isEqualTo(updatedEntity.getCreatedAt()),
                () -> assertThat(savedEntity.getModifiedAt()).isBefore(updatedEntity.getModifiedAt())
        );
    }

    @Entity
    static class TestEntity extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        protected TestEntity() {
        }

        public TestEntity(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
