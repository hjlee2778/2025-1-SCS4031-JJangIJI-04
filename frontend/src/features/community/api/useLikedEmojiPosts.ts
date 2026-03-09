import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';
import { CommunityPost } from '@/features/community/types/community';

export const useLikedEmojiPosts = () => {
  return useQuery<CommunityPost[]>({
    queryKey: ['likedEmojiPosts'],
    queryFn: async () => {
      const res = await api.get('/community/expenses/emojis');
      return res.data;
    },
    staleTime: 1000 * 60 * 3, //3분 동안 캐시 유지
  });
};
