import styled from 'styled-components';

interface IconTextRowProps {
  icon: string;
  text: string;
  color?: string;
  fontSize?: string;
  fontWeight?: string | number;
  ellipsis?: boolean;
}

export const IconTextRow = ({
  icon,
  text,
  color = '#808080',
  fontSize = '13px',
  fontWeight = '600',
  ellipsis = true, // 기본값은 잘림 허용
}: IconTextRowProps) => {
  return (
    <Row>
      <Icon src={icon} alt="" />
      <Text $ellipsis={ellipsis} style={{ color, fontSize, fontWeight }}>
        {text}
      </Text>
    </Row>
  );
};

const Row = styled.div`
  display: flex;
  align-items: center;
  margin-top: 1px;
`;

const Icon = styled.img`
  width: 12px;
  height: 16px;
  margin-right: 2px;
`;

const Text = styled.span<{ $ellipsis: boolean }>`
  display: inline-block;
  ${({ $ellipsis }) =>
    $ellipsis
      ? `
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 120px;
  `
      : `
    white-space: normal;
    overflow: visible;
    text-overflow: unset;
  `}
`;
