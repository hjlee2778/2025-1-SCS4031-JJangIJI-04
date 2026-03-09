export const isRestaurantBookmarked = (
  bookmarkedMap: Record<number, boolean>,
  restaurantId: number
) => {
  return !!bookmarkedMap[restaurantId];
};
