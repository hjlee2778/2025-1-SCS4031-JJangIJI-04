export interface KakaoLoginResponse {
    nickname: string;
    imageUrl: string;
}

export interface SignupRequest {
  nickname: string;
  categories: number[];
}

export interface Category {
  categoryId: number;
  name: string;
}

export interface UserInfo {
  id: string;
  email: string;
  nickname: string;
  imageUrl: string;
  loginType: string;
  role: string;
  isExpenseOpen: string;
  categories: Category[];
}