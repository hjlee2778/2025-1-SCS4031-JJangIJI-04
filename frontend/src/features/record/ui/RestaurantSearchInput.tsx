import { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { searchRestaurants } from '@/features/restaurant/api/restaurantApi';
import { useDebounce } from '@/hooks/useDebounce';
import { InputField } from '@/shared/ui/InputField';
import { Restaurant } from '@/features/restaurant/types/restaurant';

interface RestaurantSearchInputProps {
  value: string;
  onChange: (value: string, restaurant?: Restaurant) => void;
  placeholder?: string;
}

export const RestaurantSearchInput = ({
  value,
  onChange,
  placeholder = '식당명을 입력해주세요',
}: RestaurantSearchInputProps) => {
  const [searchTerm, setSearchTerm] = useState(value);
  const [isOpen, setIsOpen] = useState(false);
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(false);
  const wrapperRef = useRef<HTMLDivElement>(null);
  const debouncedSearchTerm = useDebounce(searchTerm, 300);

  useEffect(() => {
    const fetchRestaurants = async () => {
      if (!debouncedSearchTerm.trim()) {
        setRestaurants([]);
        return;
      }

      setLoading(true);
      try {
        const results = await searchRestaurants(debouncedSearchTerm);
        setRestaurants(Array.isArray(results) ? results : []);
      } catch (error) {
        console.error('식당 검색 실패:', error);
        setRestaurants([]);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, [debouncedSearchTerm]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        wrapperRef.current &&
        !wrapperRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    };

    document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    setSearchTerm(newValue);
    setIsOpen(true);
    onChange(newValue); // 직접 입력값도 전달
  };

  const handleSelectRestaurant = (
    restaurant: Restaurant,
    event: React.MouseEvent
  ) => {
    event.stopPropagation();
    setSearchTerm(restaurant.name);
    setIsOpen(false);
    onChange(restaurant.name, restaurant);
  };

  const handleWrapperClick = () => {
    setIsOpen(true);
  };

  return (
    <Wrapper ref={wrapperRef} onClick={handleWrapperClick}>
      <StyledInput
        value={searchTerm}
        onChange={handleInputChange}
        placeholder={placeholder}
      />

      {isOpen && searchTerm && (
        <DropdownContainer>
          {loading ? (
            <DropdownItem>검색중...</DropdownItem>
          ) : restaurants.length > 0 ? (
            restaurants.map((restaurant) => (
              <DropdownItem
                key={restaurant.id}
                onClick={(e) => handleSelectRestaurant(restaurant, e)}
              >
                <RestaurantName>{restaurant.name}</RestaurantName>
                <RestaurantAddress>{restaurant.address}</RestaurantAddress>
              </DropdownItem>
            ))
          ) : (
            <DropdownItem>검색 결과가 없습니다</DropdownItem>
          )}
        </DropdownContainer>
      )}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  position: relative;
  width: 100%;
`;

const StyledInput = styled(InputField)`
  width: 100%;
`;

const DropdownContainer = styled.div`
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-top: 4px;
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
`;

const DropdownItem = styled.div`
  padding: 12px;
  cursor: pointer;

  &:hover {
    background-color: #f5f5f5;
  }

  &:not(:last-child) {
    border-bottom: 1px solid #e0e0e0;
  }
`;

const RestaurantName = styled.div`
  font-weight: 500;
  font-size: 14px;
  margin-bottom: 4px;
`;

const RestaurantAddress = styled.div`
  font-size: 12px;
  color: #666;
`;
