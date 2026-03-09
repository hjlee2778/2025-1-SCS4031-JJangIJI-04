package com.jjangiji.hankkimoa.user.repository;

import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.domain.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    List<UserCategory> findAllByUser(User user);
    void deleteAllByUserId(Long userId);
}
