"use client";
import { QueryClient, QueryClientProvider } from "react-query";
import { useState } from "react";

type Props = {
  children: React.ReactNode | React.ReactNode[];
};

export default function Providers({ children }: Props) {
  const [queryClient] = useState(() => new QueryClient());

  return (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
}
