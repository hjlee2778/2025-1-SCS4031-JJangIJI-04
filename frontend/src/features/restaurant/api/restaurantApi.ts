import api from '@/lib/axios';
import { Restaurant } from '@/features/restaurant/types/restaurant';

export const searchRestaurants = async (
  keyword: string
): Promise<Restaurant[]> => {
  if (!keyword.trim()) return [];

  try {
    const response = await api.get(`/restaurants/search`, {
      params: { keyword },
    });
    // 응답 데이터가 배열인지 확인
    return Array.isArray(response.data) ? response.data : [];
  } catch (error) {
    console.error('식당 검색 API 호출 실패:', error);
    return [];
  }
};
