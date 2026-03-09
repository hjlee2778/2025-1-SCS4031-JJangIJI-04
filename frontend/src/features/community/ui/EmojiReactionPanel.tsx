import styled from 'styled-components';
import { emojiMap } from '@/features/community/constants/emojiMap';

interface EmojiReactionPanelProps {
  reactions: { emojiId: number; count: number }[];
  selected?: Set<number>;
  onClickEmoji?: (emojiId: number) => void;
}

export const EmojiReactionPanel = ({
  reactions,
  selected,
  onClickEmoji,
}: EmojiReactionPanelProps) => {
  return (
    <Wrapper>
      {reactions.map(({ emojiId, count }) => {
        const emoji = emojiMap[emojiId];
        if (!emoji || !count) return null;

        const isSelected = selected?.has(emojiId) ?? false;

        return (
          <EmojiItem
            key={emojiId}
            $selected={isSelected}
            onClick={() => onClickEmoji?.(emojiId)}
          >
            <EmojiImg src={emoji.src} alt={emoji.label} />
            <Count $selected={isSelected}>{count}</Count>
          </EmojiItem>
        );
      })}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
`;

const EmojiItem = styled.div<{ $selected: boolean }>`
  display: flex;
  align-items: center;
  gap: 2px;
  padding: 1px 4px;
  border: ${({ $selected }) => ($selected ? 'none' : '0.5px solid #808080')};
  border-radius: 12px;
  background-color: ${({ $selected }) => ($selected ? '#f97316' : '#fff')};
  cursor: pointer;
`;

const EmojiImg = styled.img`
  width: 14px;
  height: 14px;
`;

const Count = styled.span<{ $selected: boolean }>`
  font-size: var(--font-size-3xs);
  color: ${({ $selected }) => ($selected ? '#fff' : '#444')};
`;
