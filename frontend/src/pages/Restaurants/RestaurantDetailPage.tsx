import { useParams, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { useRestaurantDetail } from '@/features/restaurant/api/useRestaurantDetail';
import { LoadingSpinner } from '@/shared/ui/LoadingSpinner/LoadingSpinner';
import { BookmarkButton } from '@/features/restaurant/ui/bookmark/BookmarkButton';
import { IconTextRow } from '@/features/restaurant/ui/IconTextRow';
import { useEffect, useState } from 'react';
import { useToggleBookmark } from '@/features/restaurant/mutations/useToggleBookmark';

export const RestaurantDetailPage = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const restaurantId = Number(id);
  const { data: restaurant, isLoading } = useRestaurantDetail(restaurantId);

  const [bookmarked, setBookmarked] = useState(false);

  useEffect(() => {
    if (restaurant) {
      setBookmarked(restaurant.bookmarked);
    }
  }, [restaurant]);

  const { mutate: toggleBookmark } = useToggleBookmark();

  const handleToggleBookmark = () => {
    toggleBookmark({ restaurantId, isBookmarked: bookmarked });
    setBookmarked((prev) => !prev);
  };

  if (isLoading || !restaurant) {
    return <LoadingSpinner message="식당 정보를 불러오는 중입니다..." />;
  }

  return (
    <PageWrapper>
      <FixedTopControls>
        <BackButton onClick={() => navigate(-1)}>&lt;</BackButton>
        <BookmarkWrapper>
          <BookmarkButton
            active={bookmarked}
            onClick={handleToggleBookmark}
            size={16}
          />
        </BookmarkWrapper>
      </FixedTopControls>
      <ImageSection>
        {Array.isArray(restaurant.imgUrl) && restaurant.imgUrl.length > 0 ? (
          restaurant.imgUrl.map((url, index) => (
            <Image
              key={index}
              src={url}
              alt="음식 이미지"
              onError={(e) => {
                e.currentTarget.src = '/images/basic-restaurant.svg';
              }}
            />
          ))
        ) : (
          <Image src="/images/basic-restaurant.svg" alt="기본 음식 이미지" />
        )}
      </ImageSection>

      <ContentSection>
        <RestaurantName>{restaurant.name ?? '이름 없음'}</RestaurantName>

        <IconTextList>
          <IconTextRow
            icon="/icons/restaurants/price.svg"
            text={`평균 가격 ${
              restaurant.menuAverage !== undefined
                ? `${restaurant.menuAverage.toLocaleString()}원`
                : '정보 없음'
            }`}
            color="#FF6701"
            fontSize="13px"
            fontWeight={600}
            ellipsis={false}
          />
          <IconTextRow
            icon="/icons/restaurants/place.svg"
            text={restaurant.streetAddress ?? '주소 정보 없음'}
            color="#808080"
            fontSize="12px"
            fontWeight={500}
            ellipsis={false}
          />
          <OpeningHourRow>
            <IconWrapper>
              <Icon src="/icons/restaurants/time.svg" alt="영업시간" />
            </IconWrapper>
            <HourList>
              {restaurant.openingHour.map((line, index) => {
                const [day, ...times] = line.split(' ');
                const timeStr = times.join(' ');
                const isClosed = timeStr.includes('null');

                return (
                  <li key={index}>
                    <span className="day">{day}</span>
                    <span className="time">{isClosed ? '휴무' : timeStr}</span>
                  </li>
                );
              })}
            </HourList>
          </OpeningHourRow>
          <IconTextRow
            icon="/icons/restaurants/menu.svg"
            text={restaurant.category ?? '카테고리 정보 없음'}
            color="#808080"
            fontSize="12px"
            fontWeight={500}
            ellipsis={false}
          />
        </IconTextList>

        <MenuTitle>메뉴</MenuTitle>
        {restaurant.menu.length > 0 ? (
          restaurant.menu.map((item, index) => (
            <MenuItem key={index}>
              <MenuInfo>
                {item.main && <Badge>대표</Badge>}
                <MenuName>{item.name}</MenuName>
                <MenuDescription>{item.introduce}</MenuDescription>
                <MenuPrice>{item.price?.toLocaleString() ?? 0}원</MenuPrice>
              </MenuInfo>
              <MenuImage
                src={item.imgUrl || '/images/basic-restaurant.svg'}
                onError={(e) => {
                  e.currentTarget.src = '/images/basic-restaurant.svg';
                }}
                alt={item.name}
              />
            </MenuItem>
          ))
        ) : (
          <MenuDescription>메뉴 정보가 없습니다.</MenuDescription>
        )}
      </ContentSection>
    </PageWrapper>
  );
};

const PageWrapper = styled.div`
  position: relative;
  padding: 0px 0;
`;

const FixedTopControls = styled.div`
  position: absolute;
  top: 12px;
  left: 0;
  right: 0;
  z-index: 10;
  display: flex;
  justify-content: space-between;
  padding: 0 12px;
  pointer-events: none;
`;

const ImageSection = styled.div`
  position: relative;
  display: flex;
  overflow-x: auto;
`;

const Image = styled.img`
  width: 195px;
  height: 160px;
  object-fit: cover;
  flex-shrink: 0;
`;

const BackButton = styled.button`
  pointer-events: auto;
  background-color: white;
  border: none;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  font-size: 14px;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  color: #202632;
`;

const BookmarkWrapper = styled.div`
  pointer-events: auto;
  background-color: white;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
`;

const ContentSection = styled.div`
  padding: 20px;
`;

const RestaurantName = styled.h2`
  font-size: var(--font-size-2lg);
  font-weight: 500;
`;

const IconTextList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin-top: 6px;
`;

const MenuTitle = styled.h3`
  margin-top: 48px;
  font-size: var(--font-size-2lg);
  font-weight: 500;
`;

const MenuItem = styled.div`
  display: flex;
  align-items: center;
  margin-top: 16px;
  justify-content: space-between;
`;

const MenuInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
`;

const MenuName = styled.div`
  font-size: var(--font-size-sm);
  font-weight: 700;
`;

const MenuPrice = styled.div`
  font-size: var(--font-size-sm);
  font-weight: 700;
  margin-top: 12px;
`;

const MenuDescription = styled.div`
  font-size: var(--font-size-3xs);
  color: #808080;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-word;
  line-height: 1.4;
`;

const Badge = styled.span`
  display: inline-block;
  background-color: #f97316;
  color: white;
  font-size: 10px;
  border-radius: 4px;
  padding: 2px 6px;
  margin-bottom: 4px;
  width: fit-content;
  text-align: center;
`;

const MenuImage = styled.img`
  width: 106.67px;
  height: 80px;
  object-fit: cover;
  border-radius: 10px;
  margin-left: 12px;
  flex-shrink: 0;
  align-self: flex-start;
`;

const OpeningHourRow = styled.div`
  display: flex;
  align-items: center;
  margin-top: 4px;
`;

const IconWrapper = styled.div`
  display: flex;
  align-items: center;
  margin-right: 6px;
`;

const Icon = styled.img`
  width: 12px;
  height: 16px;
`;

const HourList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #555;
    margin-bottom: 2px;
  }

  .day {
    display: inline-block;
    width: 24px;
    font-weight: 500;
  }

  .time {
    margin-left: 6px;
    white-space: nowrap;
  }
`;
