import axios, { AxiosError, AxiosRequestConfig } from 'axios';
import { useAuthStore } from '@/features/auth/store/useAuthStore';
import { requestRefreshToken } from '@/features/auth/api/requestRefreshToken';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true,
});

// 401 응답 시 자동 refresh + 원래 요청 재시도
api.interceptors.response.use(
  (res) => res,
  async (err: AxiosError) => {
    const originalRequest = err.config as AxiosRequestConfig;

    // /auth/refresh 요청에 대해서는 인터셉터 스킵
    if (originalRequest?.url === '/auth/refresh') {
      return Promise.reject(err);
    }

    if (
      err.response?.status === 401 &&
      originalRequest &&
      !(originalRequest as any)._retry &&
      !useAuthStore.getState().isRefreshFailed
    ) {
      (originalRequest as any)._retry = true;

      try {
        await requestRefreshToken();  // 반환값 무시, 쿠키로 처리
        return api(originalRequest);
      } catch (refreshError) {
        useAuthStore.getState().clearAuth();
        useAuthStore.getState().setRefreshFailed(true);
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(err);
  }
);

export default api;

