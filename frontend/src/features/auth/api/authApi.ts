import api from '@/lib/axios';
import { SignupRequest, KakaoLoginResponse, UserInfo } from '@/features/auth/types/auth';

export const requestKakaoLogin = async (code: string, redirectUri: string): Promise<KakaoLoginResponse> => {
  try {
    const res = await api.post('/auth/kakao', { 
      code,
      redirectUri 
    });

    const { nickname, image_url } = res.data;

    return {
      nickname,
      imageUrl: image_url, // snake_case → camelCase로 변환
    };
  } catch (error) {
    console.error('카카오 로그인 실패', error);
    throw error;
  }
};

export const requestSignup = async (data: SignupRequest): Promise<void> => {
  await api.post('/auth/signup', data);
};

export const getUserInfo = async (): Promise<UserInfo> => {
  const res = await api.get('/users/me');
  return res.data;
};