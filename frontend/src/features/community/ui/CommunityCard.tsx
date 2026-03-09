import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { CommunityPost } from '@/features/community/types/community';
import { EmojiReactionPanel } from '@/features/community/ui/EmojiReactionPanel';
import { EmojiAddButton } from '@/features/community/ui/EmojiAddButton';
import { BudgetGauge } from '@/features/community/ui/BudgetGauge';
import { useToggleEmoji } from '@/features/community/api/useToggleEmoji';
import { format } from 'date-fns';

import StoreIcon from '@/assets/icons/store.svg?react';
import MenuIcon from '@/assets/icons/menu.svg?react';
import WalletIcon from '@/assets/icons/wallet.svg?react';
import { useAuthStore } from '@/features/auth/store/useAuthStore';

interface CommunityCardProps {
  post: CommunityPost;
}

export const CommunityCard = ({ post }: CommunityCardProps) => {
  const { userId } = useAuthStore();
  const navigate = useNavigate();
  const [reactions, setReactions] = useState<Record<number, number>>(() =>
    (post.emojis || []).reduce(
      (acc, { emojiId, count }) => {
        acc[emojiId] = count;
        return acc;
      },
      {} as Record<number, number>
    )
  );

  const [selectedEmojis, setSelectedEmojis] = useState<Set<number>>(() => {
    const initial = new Set<number>();
    post.emojis.forEach((emoji) => {
      if (userId !== null && emoji.userIds?.includes(userId)) {
        initial.add(emoji.emojiId);
      }
    });
    return initial;
  });

  const { mutate: toggleEmoji } = useToggleEmoji();

  const handleAddReaction = (emojiId: number) => {
    const isSelected = selectedEmojis.has(emojiId);

    toggleEmoji({ body: { expenseId: post.expenseId, emojiId }, isSelected });

    // 프론트 상태도 토글
    setReactions((prev) => {
      const updated = { ...prev };
      if (isSelected) {
        if (updated[emojiId] > 1) updated[emojiId]--;
        else delete updated[emojiId];
      } else {
        updated[emojiId] = (prev[emojiId] || 0) + 1;
      }
      return updated;
    });

    setSelectedEmojis((prev) => {
      const updated = new Set(prev);
      if (isSelected) updated.delete(emojiId);
      else updated.add(emojiId);
      return updated;
    });
  };

  return (
    <CardWrapper>
      <LeftSection>
        <ProfileImg
          src={post.imageUrl || '/icons/community/user-avatar.svg'}
          alt="프로필"
          onClick={() =>
            navigate(`/users/${post.userId}/expenses`, {
              state: { nickname: post.nickname },
            })
          }
        />
      </LeftSection>

      <RightSection>
        <RelativeWrapper>
          <TextGroup>
            <TopRow>
              <Nickname
                onClick={() =>
                  navigate(`/users/${post.userId}/expenses`, {
                    state: { nickname: post.nickname },
                  })
                }
              >
                {post.nickname}
              </Nickname>

              <DateText>
                {format(new Date(post.createdAt), 'yyyy.MM.dd HH:mm')}
              </DateText>
            </TopRow>

            <InfoRow>
              <Item>
                <StoreIcon />
                <Text title={post.restaurant}>{post.restaurant}</Text>
              </Item>
              <Item>
                <MenuIcon />
                <Text title={post.menu}>{post.menu}</Text>
              </Item>
              <Item>
                <WalletIcon />
                <Text>{post.expense.toLocaleString()}원</Text>
              </Item>
            </InfoRow>
          </TextGroup>

          <AbsoluteGauge>
            <BudgetGauge
              used={post.savingGoal - post.remainingBudget}
              total={post.savingGoal}
            />
          </AbsoluteGauge>
        </RelativeWrapper>

        <Memo>{post.memo}</Memo>

        <EmojiRow>
          <EmojiButtonWrapper>
            <EmojiAddButton onSelect={handleAddReaction} />
          </EmojiButtonWrapper>

          <EmojiReactionPanel
            reactions={Object.entries(reactions).map(([emojiId, count]) => ({
              emojiId: Number(emojiId),
              count,
            }))}
            selected={selectedEmojis}
            onClickEmoji={handleAddReaction}
          />
        </EmojiRow>
      </RightSection>
    </CardWrapper>
  );
};

const CardWrapper = styled.div`
  display: flex;
  padding: 20px 0;
`;

const LeftSection = styled.div`
  flex-shrink: 0;
  margin-right: 8px;
`;

const ProfileImg = styled.img`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
`;

const RelativeWrapper = styled.div`
  position: relative;
  width: 100%;
  padding-right: 130px;
  box-sizing: border-box;
`;

const RightSection = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  max-width: 100%;
  min-width: 0;
`;

const AbsoluteGauge = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  width: 119px;
`;

const TextGroup = styled.div`
  display: flex;
  flex-direction: column;
`;

const TopRow = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const Nickname = styled.div`
  font-size: var(--font-size-2xs);
  font-weight: bold;
  cursor: pointer;
`;

const DateText = styled.div`
  font-size: var(--font-size-4xs);
  color: #999;
`;

const InfoRow = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-top: 8px;
`;

const Item = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;
  min-width: 0;
  max-width: 100%;

  svg {
    width: 12px;
    height: 12px;
    flex-shrink: 0;
  }
`;

const Text = styled.span`
  display: inline-block;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: var(--font-size-4xs);
  font-weight: 600;
  color: #555;
`;

const Memo = styled.div`
  margin-top: 12px;
  margin-bottom: 6px;
  font-size: 10px;
  color: #202632;
  font-weight: 600;
  white-space: pre-line;
`;

const EmojiRow = styled.div`
  display: flex;
  gap: 4px;
  align-items: flex-start;
  position: relative;
`;

const EmojiButtonWrapper = styled.div`
  position: relative;
  display: inline-block;
`;
