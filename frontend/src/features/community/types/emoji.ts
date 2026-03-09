export interface ToggleEmojiRequest {
  expenseId: number;
  emojiId: number;
}

export interface EmojiApiErrorResponse {
  httpMethod: string;
  path: string;
  exceptionCode: 'EMOJI_ALREADY_EXIST' | 'EMOJI_NOT_FOUND';
  message: string;
}
