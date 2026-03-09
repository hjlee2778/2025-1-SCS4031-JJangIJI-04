import styled from 'styled-components';
import { emojiMap } from '@/features/community/constants/emojiMap';

interface EmojiPopoverProps {
  onSelect: (emoji: number) => void;
}

export const EmojiPopover = ({ onSelect }: EmojiPopoverProps) => {
  return (
    <PopoverWrapper>
      <Arrow />
      <EmojiGrid>
        {Object.entries(emojiMap).map(([key, { src, label }]) => (
          <EmojiButton
            key={key}
            onClick={() => onSelect(Number(key))}
            aria-label={label}
            title={label}
          >
            <img src={src} alt={label} />
          </EmojiButton>
        ))}
      </EmojiGrid>
    </PopoverWrapper>
  );
};

const PopoverWrapper = styled.div`
  position: absolute;
  top: 28px;   
  left: 0;             
  width: 240px;
  background: white;
  border: 1px solid #ff6701;
  border-radius: 16px;
  padding: 10px 12px 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 100;
  animation: fadeIn 0.2s ease-out;

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(-4px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`;

const Arrow = styled.div`
  position: absolute;
  top: -8px;
  left: 12px;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-bottom: 8px solid #ff6701;
`;

const EmojiGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
`;

const EmojiButton = styled.button`
  width: 24px;
  height: 24px;
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;

  img {
    width: 100%;
    height: 100%;
  }

  &:hover {
    transform: scale(1.1);
  }
`;