import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { Crown } from '@/features/restaurant/ui/crown/Crown';
import { BookmarkButton } from '@/features/restaurant/ui/bookmark/BookmarkButton';
import { IconTextRow } from '@/features/restaurant/ui/IconTextRow';
import { useToggleBookmark } from '@/features/restaurant/mutations/useToggleBookmark';

interface RestaurantItem {
  id: number;
  name: string;
  menuAverage: number;
  imgUrl: string;
  streetAddress: string;
  openingHours: string;
  category: string;
  bookmarked: boolean;
}

interface Props {
  restaurants: RestaurantItem[];
  showCrown?: boolean;
}

export const RestaurantListItem = ({
  restaurants,
  showCrown = true,
}: Props) => {
  const { mutate } = useToggleBookmark();

  const handleToggle = (restaurant: RestaurantItem) => {
    mutate({
      restaurantId: restaurant.id,
      isBookmarked: restaurant.bookmarked,
    });
  };

  return (
    <ListWrapper>
      {restaurants.map((restaurant, index) => (
        <Card key={restaurant.id}>
          <ThumbnailLink to={`/restaurants/${restaurant.id}`}>
            <Thumbnail
              src={restaurant.imgUrl || '/images/basic-restaurant.svg'}
              onError={(e) => {
                e.currentTarget.src = '/images/basic-restaurant.svg';
              }}
              alt={`추천 ${index + 1}`}
            />
          </ThumbnailLink>
          <Info>
            <TopRow>
              <NameWrapper>
                {showCrown && <Crown rank={index} />}
                <Name>{restaurant.name || '이름 없는 식당'}</Name>
              </NameWrapper>
              <BookmarkButtonWrapper>
                <BookmarkButton
                  active={restaurant.bookmarked}
                  onClick={() => handleToggle(restaurant)}
                />
              </BookmarkButtonWrapper>
            </TopRow>

            <IconTextRow
              icon="/icons/restaurants/price.svg"
              text={`평균 가격 ${restaurant.menuAverage.toLocaleString()}원`}
              color="#FF6701"
              fontSize="var(--font-size-2xs)"
              fontWeight={600}
            />
            <IconTextRow
              icon="/icons/restaurants/place.svg"
              text={restaurant.streetAddress}
              color="#808080"
              fontSize="var(--font-size-3xs)"
              fontWeight={600}
            />
            <IconTextRow
              icon="/icons/restaurants/time.svg"
              text={
                restaurant.openingHours.includes('null')
                  ? `${restaurant.openingHours.split(' ')[0]} 휴무`
                  : restaurant.openingHours
              }
              color="#808080"
              fontSize="var(--font-size-3xs)"
              fontWeight={600}
            />
            <IconTextRow
              icon="/icons/restaurants/menu.svg"
              text={restaurant.category}
              color="#808080"
              fontSize="var(--font-size-3xs)"
              fontWeight={600}
            />
          </Info>
        </Card>
      ))}
    </ListWrapper>
  );
};

const Card = styled.div`
  display: flex;
  align-items: stretch;
  background-color: #fff;
  overflow: hidden;
  height: 99px;
`;

const ThumbnailLink = styled(Link)`
  display: block;
  height: 100%;
`;

const Thumbnail = styled.img`
  width: 132px;
  height: 99px;
  object-fit: cover;
`;

const Info = styled.div`
  flex: 1;
  padding: 0 15px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  min-width: 0;
`;

const TopRow = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  margin-bottom: 4px;
`;

const NameWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
  min-width: 0;
  padding-right: 30px;
`;

const BookmarkButtonWrapper = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
`;

const Name = styled.span`
  font-size: 13px;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
`;

const ListWrapper = styled.div`
  display: flex;
  flex-direction: column;
  margin-top: 36px;
  gap: 28px;
`;
