package com.jjangiji.hankkimoa.restaurant.domain;

import com.jjangiji.hankkimoa.common.BaseEntity;
import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RecommendationFeedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "유저는 NULL일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull(message = "피드백은 NULL일 수 없습니다.")
    private Integer feedback;

    public RecommendationFeedback(User user, Integer feedback) {
        validateFeedback(feedback);
        this.user = user;
        this.feedback = feedback;
    }

    public RecommendationFeedback(Long id, User user, Integer feedback) {
        this(user, feedback);
        this.id = id;
    }

    private void validateFeedback(Integer feedback) {
        if (feedback != null && !(feedback == 1 || feedback == 3 || feedback == 5 || feedback == 7)) {
            throw new HankkiMoaException(ExceptionCode.RECOMMENDATION_FEEDBACK_INVALID_FORMAT);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RecommendationFeedback that = (RecommendationFeedback) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
