import { PieChart, Pie, Cell } from 'recharts';
import styled from 'styled-components';

interface GaugeChartProps {
  total: number;
  spent: number;
}

export const GaugeChart = ({ total, spent }: GaugeChartProps) => {
  const percentUsed = Math.min(Math.round((spent / total) * 100), 100);
  const percentLeft = 100 - percentUsed;

  const data = [
    { name: 'used', value: percentUsed },
    { name: 'left', value: percentLeft },
  ];

  const getMessage = (percentLeft: number) => {
    if (percentLeft > 60) return '잘 절약하고 있어요! 앞으로도 화이팅!';
    if (percentLeft > 30) return '조금 만 더 노력해볼까요? 오늘도 힘내세요!';
    if (percentLeft > 0) return '절약 금액이 얼마 남지 않았어요!\n오늘은 가성비 맛집을 찾아보는 게 어떨까요?';
    return '이번 주는 달성에 실패했습니다.\n다음 주에는 성공하기를 바라요!';
  };

  return (
    <Wrapper>
      <Title>이번 주 목표 금액은</Title>
      <GoalAmount>{total.toLocaleString()}원 이에요!</GoalAmount>

      <ChartWrapper>
        <ChartInner>
          <PieChart width={450} height={210}>
            <Pie
              data={data}
              startAngle={180}
              endAngle={0}
              cx={225}
              cy="100%"
              innerRadius={105}
              outerRadius={135}
              dataKey="value"
              stroke="none"
            >
              <Cell fill="#ff6f0f" />
              <Cell fill="#fdebd0" />
            </Pie>
          </PieChart>
          <Percent>{percentUsed}%</Percent>
        </ChartInner>
      </ChartWrapper>

      <SubTitle>이번 주 사용 가능한 외식비는</SubTitle>
      <Remaining>{(total - spent).toLocaleString()}원 남았어요!</Remaining>

      <Message>{getMessage(percentLeft)}</Message>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  text-align: center;
  margin-top: 32px;
`;

const Title = styled.div`
  font-size: 14px;
  color: #555;
`;

const GoalAmount = styled.div`
  font-size: 20px;
  font-weight: bold;
  margin: 6px 0 -12px;
`;

const ChartWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  overflow: visible;
  margin: -20px auto 0;
`;

const ChartInner = styled.div`
  position: relative;
  width: 450px;
  height: 210px;
  transform: translateY(-20px);
`;

const Percent = styled.div`
  position: absolute;
  top: 155px;
  left: 50%;
  transform: translateX(-40%);
  font-size: 28px;
  font-weight: bold;
  color: #000;
`;

const SubTitle = styled.div`
  font-size: 13px;
  font-weight: 600;
  color: #808080;
  margin-top: 16px;
`;

const Remaining = styled.div`
  font-size: 20px;
  font-weight: bold;
  color: #000;
  margin-top: 4px;
`;

const Message = styled.div`
  margin-top: 32px;
  font-size: 14px;
  color: #777;
  white-space: pre-line;
`;