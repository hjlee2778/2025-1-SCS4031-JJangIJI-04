import styled from 'styled-components';
import { EmojiReactionPanel } from '@/features/community/ui/EmojiReactionPanel';
import StoreIcon from '@/assets/icons/store.svg?react';
import MenuIcon from '@/assets/icons/menu.svg?react';
import WalletIcon from '@/assets/icons/wallet.svg?react';
import { ExpenseRecord } from '@/features/spendingStatus/api/useDailyExpenses';

type ExpenseCardProps = Omit<ExpenseRecord, 'id'>;

export const ExpenseCard = ({
  restaurant,
  menu,
  expense,
  memo,
  emojis,
}: ExpenseCardProps) => {
  return (
    <Card>
      <Content>
        <InfoRow>
          <Item>
            <StoreIcon />
            <Text>{restaurant}</Text>
          </Item>
          <Item>
            <MenuIcon />
            <Text>{menu}</Text>
          </Item>
          <Item>
            <WalletIcon />
            <Text>{expense.toLocaleString()}원</Text>
          </Item>
        </InfoRow>

        <Memo>{memo}</Memo>

        <EmojiWrapper>
          <EmojiReactionPanel reactions={emojis} />
        </EmojiWrapper>
      </Content>
    </Card>
  );
};

const Card = styled.div`
  margin-top: 16px;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  background-color: #fff;
  border: 1px solid #e0e0e0;
  min-height: 140px;
  display: flex;
  flex-direction: column;
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 12px;
`;

const InfoRow = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
`;

const Item = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;

  svg {
    width: 15px;
    height: 15px;
    flex-shrink: 0;
  }
`;

const Text = styled.span`
  font-size: 13px;
  color: #202632;
  font-weight: 600;
`;

const Memo = styled.div`
  font-size: 13px;
  color: #444;
  white-space: pre-line;
`;

const EmojiWrapper = styled.div`
  margin-top: auto;
`;
