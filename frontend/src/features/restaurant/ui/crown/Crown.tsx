import GoldCrown from '@/assets/icons/gold-medal.svg?react';
import SilverCrown from '@/assets/icons/silver-medal.svg?react';
import BronzeCrown from '@/assets/icons/bronze-medal.svg?react';

interface CrownBadgeProps {
  rank: number;
}

export const Crown = ({ rank }: CrownBadgeProps) => {
  if (rank > 2) return null;

  const CrownIcon = (() => {
    switch (rank) {
      case 0:
        return GoldCrown;
      case 1:
        return SilverCrown;
      case 2:
        return BronzeCrown;
      default:
        return null;
    }
  })();

  return CrownIcon ? <CrownIcon width={20} height={20} /> : null;
};
