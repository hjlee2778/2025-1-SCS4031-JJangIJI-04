package com.jjangiji.hankkimoa.expense.repository;

import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Optional;

public interface ExpenseSavingGoalRepository extends JpaRepository<ExpenseSavingGoal, Long> {

    @Query("SELECT e FROM ExpenseSavingGoal e "
            + "WHERE e.user.id = :userId ORDER BY e.createdAt DESC limit 1 ")
    Optional<ExpenseSavingGoal> findLastByUser(@Param("userId") Long userId);

    @Query("SELECT e FROM ExpenseSavingGoal e " +
            "WHERE e.user.id = :userId AND :date BETWEEN e.startDate AND e.endDate")
    Optional<ExpenseSavingGoal> findByUserAndDate(@Param("userId") Long userId,
                                                  @Param("date") LocalDate date);
}
