import { router } from '@/app/routes/index.tsx';
import { RouterProvider } from 'react-router-dom';
import { ReactQueryProvider } from '@/app/providers/ReactQueryProvider';

export const App = () => {
  return (
    <ReactQueryProvider>
      <RouterProvider router={router} />
    </ReactQueryProvider>
  );
};
