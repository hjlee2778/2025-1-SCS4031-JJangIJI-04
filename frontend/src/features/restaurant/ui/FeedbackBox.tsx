import styled from 'styled-components';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/axios';

const FEEDBACK_OPTIONS = [
  { value: 1, label: '별로예요', icon: '/icons/feedback/confounded-face.svg' },
  {
    value: 3,
    label: '보통이에요',
    icon: '/icons/feedback/disappointed-face.svg',
  },
  { value: 5, label: '좋아요', icon: '/icons/feedback/soso-face.svg' },
  { value: 7, label: '최고예요', icon: '/icons/feedback/good-face.svg' },
];

export const FeedbackBox = () => {
  const queryClient = useQueryClient();

  const { mutate: sendFeedback } = useMutation({
    mutationFn: async (feedback: number) => {
      await api.post('/recommendation/feedback', { feedback });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['recommendedRestaurants'] });
      alert('피드백이 새로 반영되었어요!');
    },
    onError: () => {
      alert('피드백 전송에 실패했어요. 다시 시도해주세요.');
    },
  });

  return (
    <Box>
      <Title>추천에 대해 얼마나 만족하시나요?</Title>
      <SubText>다음 식당 추천에 반영될 수 있어요!</SubText>
      <EmojiRow>
        {FEEDBACK_OPTIONS.map((option) => (
          <EmojiButton
            key={option.value}
            onClick={() => sendFeedback(option.value)}
          >
            <img src={option.icon} alt={option.label} />
            <span>{option.label}</span>
          </EmojiButton>
        ))}
      </EmojiRow>
    </Box>
  );
};

const Box = styled.div`
  margin-top: 48px;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.06);
  text-align: center;
`;

const Title = styled.div`
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 4px;
`;

const SubText = styled.div`
  font-size: 12px;
  color: #666;
  margin-bottom: 20px;
`;

const EmojiRow = styled.div`
  display: flex;
  justify-content: space-around;
  gap: 12px;
`;

const EmojiButton = styled.button`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 12px;
  font-weight: 600;
  color: #333;

  img {
    width: 40px;
    height: 40px;
  }

  &:hover {
    opacity: 0.8;
  }
`;
