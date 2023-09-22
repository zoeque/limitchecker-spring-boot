import axios from "axios";
import { useCallback, useEffect, useState } from "react";

const isError = (error: unknown): error is Error => {
  return error instanceof Error;
};

interface StoredItem {
  itemName: string;
  itemType: string;
  expiredDate: string;
}
const SavedItemList = () => {
  const [item, setItem] = useState<StoredItem[]>([]);
  const [error, setError] = useState<Error | undefined>(undefined);

  const fetchStoredItem = useCallback(async () => {
    try {
      axios.get("http://localhost:8080/find")
        .then((res) => {
          setItem(res.data);
        });
    } catch (e) {
      if (isError(e)) {
        setError(e);
      }
    }
  }, []);

  useEffect(() => {
    fetchStoredItem();
  }, [fetchStoredItem]);

  if (error) {
    return <div>{error.message}</div>;
  }

  return (
    <div>
      {item.map((storedItem) => (
        <li key={storedItem.itemName}>{storedItem.itemName}</li>
      ))}
    </div>
  );
};

export default SavedItemList;