import styled from 'styled-components';
import { useIntersectionObserver } from '@/shared/hooks/useIntersectionObserver';
import { useCommunityPosts } from '@/features/community/api/useCommunityPosts';
import { CommunityCard } from '@/features/community/ui/CommunityCard';
import FileIcon from '@/assets/icons/file.svg?react';
import { LoadingSpinner } from '@/shared/ui/LoadingSpinner/LoadingSpinner';
import { CommunityPost } from '@/features/community/types/community';

export const CommunityPage = () => {
  const { data, fetchNextPage, hasNextPage, isFetchingNextPage, isLoading } =
    useCommunityPosts();

    const loadMoreRef = useIntersectionObserver(() => {
      if (hasNextPage && !isFetchingNextPage) {
        fetchNextPage();
      }
    }, {
      rootMargin: '200px',
    });

  const posts = data?.pages.flat() || [];

  return (
    <Container>
      <Header>
        <Title>한끼니티</Title>
        <Subtitle>유저들의 지출 후기와 절약 노하우를 확인해 보세요</Subtitle>
        <Divider />
      </Header>

      {isLoading ? (
        <CenteredBlock>
          <LoadingSpinner message="커뮤니티 글을 불러오는 중이에요" size={40} />
        </CenteredBlock>
      ) : posts.length === 0 ? (
        <CenteredBlock>
          <FileIconWrapper>
            <FileIcon />
          </FileIconWrapper>
          <NoDataText>작성된 커뮤니티 글이 없어요</NoDataText>
        </CenteredBlock>
      ) : (
        <PostList>
          {posts
            .filter(
              (post): post is CommunityPost =>
                !!post && typeof post.expenseId !== 'undefined'
            )
            .map((post) => (
              <CommunityCard key={post.expenseId} post={post} />
            ))}
          <div ref={loadMoreRef} style={{ height: '40px' }} />
          {isFetchingNextPage && (
            <LoadingSpinner
              message="커뮤니티 글을 불러오는 중이에요"
              size={40}
            />
          )}
        </PostList>
      )}
    </Container>
  );
};

const Container = styled.div`
  background-color: #fff;
  min-height: 100vh;
  width: 100%;
  padding: var(--page-padding);
  padding-top: var(--safe-area-top);
  box-sizing: border-box;
`;

const Header = styled.div`
  margin: 20px 5px;
  text-align: left;
`;

const Title = styled.h1`
  font-size: var(--font-size-lg);
  font-weight: 700;
  color: #202632;
`;

const Subtitle = styled.p`
  font-size: var(--font-size-2xs);
  color: #808080;
  font-weight: 700;
  line-height: 1.5;
  margin-top: 2px;
`;

const Divider = styled.hr`
  border: none;
  border-top: 1px solid #ccc;
  margin: 16px 0 32px;
`;

const PostList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 18px;
`;

const FileIconWrapper = styled.div`
  margin-top: 12px;
  svg {
    width: 50px;
    height: 50px;
    opacity: 0.4;
  }
`;

const NoDataText = styled.div`
  font-size: 13px;
  color: #808080;
  font-weight: 700;
`;

const CenteredBlock = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  gap: 12px;
`;
