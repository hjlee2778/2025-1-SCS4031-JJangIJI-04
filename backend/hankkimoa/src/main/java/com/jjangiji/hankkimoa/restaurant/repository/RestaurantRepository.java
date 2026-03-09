package com.jjangiji.hankkimoa.restaurant.repository;

import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantAverageRatingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = """
            SELECT * FROM restaurant r
            WHERE MATCH(name) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
            """,
            nativeQuery = true)
    List<Restaurant> findAllByKeyword(@Param("keyword") String keyword);

    @Query("""
    SELECT new com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantAverageRatingResponse(r.uniqueId, CAST(AVG(e.rating) AS int))
    FROM Expense e
    JOIN e.restaurant r
    WHERE e.expenseSavingGoal.id IN (
        SELECT esg.id
        FROM ExpenseSavingGoal esg
        JOIN esg.user u
        WHERE u.id = :userId
    )
    GROUP BY r.uniqueId
    """)
    List<RestaurantAverageRatingResponse> findRestaurantAverageRating(@Param("userId") Long userId);

    @Query("SELECT r.uniqueId FROM Restaurant r")
    Set<String> findAllUniqueId();

    @Query("SELECT b.restaurant FROM Bookmark b WHERE b.user.id = :userId ORDER BY b.createdAt DESC ")
    List<Restaurant> findAllBookmarkedRestaurantsOrderByCreatedAtDESC(@Param("userId") Long userId);

    List<Restaurant> findAllByUniqueIdIn(List<String> uniqueIds);
}
