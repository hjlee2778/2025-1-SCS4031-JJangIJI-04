import { useInfiniteQuery } from '@tanstack/react-query';
import api from '@/lib/axios';
import { CommunityPost } from '@/features/community/types/community';

export const useCommunityPosts = () => {
  return useInfiniteQuery<CommunityPost[]>({
    queryKey: ['community-posts'],
    queryFn: async ({ pageParam = 0 }) => {
      const res = await api.get('/community/expenses', {
        params: { page: pageParam, size: 10 },
      });
      return res.data;
    },
    getNextPageParam: (lastPage, allPages) => {
      return lastPage.length < 10 ? undefined : allPages.length;
    },
    initialPageParam: 0,
  });
};