export const formatDate = (date: Date | string) => {
  return Intl.DateTimeFormat('en-GB', {
    year: 'numeric',
    month: 'numeric',
    day: '2-digit',
  }).format(new Date(date));
};
