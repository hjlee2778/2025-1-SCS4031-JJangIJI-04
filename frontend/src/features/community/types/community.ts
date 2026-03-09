export interface EmojiCount {
  emojiId: number;
  count: number;
  userIds?: number[];
}

export interface CommunityPost {
  // 식별자
  expenseId: number;

  // 사용자 정보
  userId: number;
  nickname: string;
  imageUrl: string;

  // 식당 및 지출 정보
  restaurantId: number;
  restaurant: string;
  menu: string;
  expense: number;

  // 날짜 및 메모
  createdAt: string;
  memo: string;

  // 절약 목표 관련
  savingGoalId: number;
  savingGoal: number;
  remainingBudget: number;

  // 이모지
  emojis: EmojiCount[];
}
