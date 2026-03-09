import styled from 'styled-components';
import { BookmarkIcon } from '@/features/restaurant/ui/bookmark/BookmarkIcon';

interface BookmarkButtonProps {
  active: boolean;
  onClick: () => void;
  size?: number; // 추가
}

export const BookmarkButton = ({
  active,
  onClick,
  size = 24,
}: BookmarkButtonProps) => (
  <Button type="button" onClick={onClick} $size={size}>
    <BookmarkIcon size={size * 0.83} active={active} />{' '}
    {/* svg 내부는 살짝 작게 조절 */}
  </Button>
);

const Button = styled.button<{ $size: number }>`
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  width: ${({ $size }) => `${$size}px`};
  height: ${({ $size }) => `${$size}px`};
  display: flex;
  align-items: center;
  justify-content: center;
`;
