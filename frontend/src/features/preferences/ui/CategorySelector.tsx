import styled from 'styled-components';

interface Category {
  label: string;
  value: number;
  icon: string;
}

interface Props {
  selected: number[];
  onChange: (next: number[]) => void;
}

export const CATEGORY_LIST: Category[] = [
  { label: '한식', value: 1, icon: 'korean-food.svg' },
  { label: '중식', value: 2, icon: 'chinese-food.svg' },
  { label: '양식', value: 3, icon: 'western-food.svg' },
  { label: '일식', value: 4, icon: 'japanese-food.svg' },
  { label: '아시안', value: 5, icon: 'asian-food.svg' },
  { label: '분식', value: 6, icon: 'korean-street-food.svg' },
  { label: '멕시칸', value: 7, icon: 'mexican-food.svg' },
  { label: '기타', value: 8, icon: 'other-food.svg' },
];

export const CategorySelector = ({ selected, onChange }: Props) => {
  const toggle = (value: number) => {
    if (selected.includes(value)) {
      onChange(selected.filter((v) => v !== value));
    } else {
      onChange([...selected, value]);
    }
  };

  return (
    <Grid>
      {CATEGORY_LIST.map((cat) => (
        <Button
          key={cat.value}
          selected={selected.includes(cat.value)}
          onClick={() => toggle(cat.value)}
        >
          <div className="image-container">
            <img src={`/icons/categories/${cat.icon}`} alt={cat.label} />
          </div>
          {cat.label}
        </Button>
      ))}
    </Grid>
  );
};

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-top: 32px;
`;

const Button = styled.button<{ selected: boolean }>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  padding: 12px;
  height: 75px;
  width: 100%;
  aspect-ratio: 3 / 2;

  background-color: ${({ selected }) => (selected ? '#FF6701' : '#fff')};
  border: 1px solid ${({ selected }) => (selected ? '#FF6701' : '#ccc')};
  border-radius: 12px;
  color: ${({ selected }) => (selected ? '#fff' : '#333')};

  transition: 0.2s;
  cursor: pointer;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .image-container {
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 4px;
  }

  img {
    width: 35px;
    height: 35px;
    object-fit: contain;
    filter: ${({ selected }) =>
      selected ? 'invert(1)' : 'brightness(0) saturate(100%) invert(40%)'};
  }

  font-size: 11px;
  font-weight: 500;
`;
