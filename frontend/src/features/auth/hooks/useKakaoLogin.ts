import { useMutation } from '@tanstack/react-query';
import { requestKakaoLogin } from '@/features/auth/api/authApi';
import { useAuthStore } from '@/features/auth/store/useAuthStore';
import { KakaoLoginResponse } from '@/features/auth/types/auth';

interface KakaoLoginParams {
  code: string;
  redirectUri: string;
}

export const useKakaoLogin = () => {
  const setNickname = useAuthStore((state) => state.setNickname);
  const setProfileImage = useAuthStore((state) => state.setProfileImage);

  return useMutation<KakaoLoginResponse, Error, KakaoLoginParams>({
    mutationFn: ({ code, redirectUri }) => requestKakaoLogin(code, redirectUri),
    onSuccess: ({ nickname, imageUrl }) => {
      setNickname(nickname);
      setProfileImage(imageUrl);
    },
  });
};

