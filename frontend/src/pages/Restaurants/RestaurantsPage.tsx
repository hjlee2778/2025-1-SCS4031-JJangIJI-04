import styled from 'styled-components';
import { RestaurantListItem } from '@/features/restaurant/ui/RestaurantListItem';
import { useRecommendedRestaurants } from '@/features/restaurant/api/useRecommendedRestaurants';
import { LoadingSpinner } from '@/shared/ui/LoadingSpinner/LoadingSpinner';
import FileIcon from '@/assets/icons/file.svg?react';
import { FeedbackBox } from '@/features/restaurant/ui/FeedbackBox';
export const RestaurantsPage = () => {
  const { data: recommendedRestaurants, isLoading } =
    useRecommendedRestaurants();

  return (
    <Container>
      <Header>
        <Title>한끼추천</Title>
        <Subtitle>
          선호하는 음식 종류와 목표 절약 금액을 바탕으로
          <br />
          적절한 식당을 추천해 드려요!
        </Subtitle>
        <Divider />
      </Header>

      {isLoading ? (
        <LoadingSpinner message="추천 식당을 불러오는 중입니다..." />
      ) : recommendedRestaurants && recommendedRestaurants.length > 0 ? (
        <>
          <RestaurantListItem restaurants={recommendedRestaurants} />
          <FeedbackBox />
        </>
      ) : (
        <EmptyBlock>
          <FileIcon />
          <NoDataText>추천할 식당이 없습니다.</NoDataText>
        </EmptyBlock>
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

const EmptyBlock = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin-top: 200px;

  svg {
    width: 40px;
    height: 40px;
    opacity: 0.4;
  }
`;

const NoDataText = styled.div`
  font-size: 13px;
  font-weight: 600;
  color: #808080;
`;
