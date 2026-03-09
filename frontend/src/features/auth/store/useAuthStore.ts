import { create } from 'zustand';
import { Category } from '@/features/auth/types/auth';


/**
 * Zustand 기반 전역 인증 상태 관리 훅
 * - accessToken 상태를 메모리에 저장
 * - 앱 초기화(isInitializing) 상태를 관리
 * - nickname, profileImage는 카카오 로그인 후 사용자 정보 초기값으로 활용됨
 */

interface AuthState {
  userId: number | null;
  nickname: string | null;  // 카카오 로그인 시 받은 사용자 닉네임 (회원가입 폼의 초기값으로 사용)
  profileImage: string | null; // 사용자 프로필 이미지 URL (커뮤니티, 마이페이지 등에 활용)
  categories: Category[] | null;
  isInitializing: boolean;  // 앱 초기 로딩 상태 (refreshToken으로 accessToken 재발급 중인지 여부)
  isRefreshFailed: boolean;  // 리프레시 실패 상태
  usedKakaoCode: string | null;
  setUsedKakaoCode: (code: string | null) => void;
  setUserId: (id: number) => void;
  setNickname: (nickname: string) => void;  // nickname을 저장하는 함수
  setProfileImage: (url: string) => void; // profileImage(URL)를 저장하는 함수
  setCategories: (categories: Category[]) => void;
  clearAuth: () => void;  // 인증 상태 초기화 함수 (로그아웃 또는 에러 발생 시 호출)
  setInitializing: (value: boolean) => void;  // isInitializing 값을 설정하는 함수 (초기화 완료 시 false로 설정)
  setRefreshFailed: (value: boolean) => void;  // 리프레시 실패 상태를 설정하는 함수
}

export const useAuthStore = create<AuthState>((set) => ({
  userId: null,
  nickname: null,
  profileImage: null,
  categories: null,
  isInitializing: true,
  usedKakaoCode: null,
  setUsedKakaoCode: (code) => set({ usedKakaoCode: code }),
  setUserId: (id) => set({ userId: id }),
  setNickname: (nickname) => set({ nickname }),
  setProfileImage: (url) => set({ profileImage: url }),
  setCategories: (categories) => set({ categories }),
  clearAuth: () =>
    set({
      userId: null,
      nickname: null,
      profileImage: null,
      categories: null,
      isRefreshFailed: false,
    }),
  setInitializing: (value) => set({ isInitializing: value }),
  isRefreshFailed: false,
  setRefreshFailed: (value: boolean) => set({ isRefreshFailed: value }),
}));