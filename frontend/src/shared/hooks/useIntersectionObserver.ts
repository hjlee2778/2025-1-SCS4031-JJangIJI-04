import { useEffect, useRef } from 'react';

export const useIntersectionObserver = (callback: () => void, options?: IntersectionObserverInit) => {
  const ref = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!ref.current) return;

    const observer = new IntersectionObserver(([entry]) => {
      if (entry.isIntersecting) callback();
    }, options);

    observer.observe(ref.current);

    return () => {
      observer.disconnect();
    };
  }, [callback, options]);

  return ref;
};