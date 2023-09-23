import { FC, useCallback, useEffect, useState } from "react";
import "../App.css"
import axios from "axios";
import { Link } from "react-router-dom";

const isError = (error: unknown): error is Error => {
  return error instanceof Error;
};

interface StoredItem {
  itemName: string;
  itemType: string;
  expiredDate: string;
}

export const StoredItemList: FC = () => {

  const [item, setItem] = useState<StoredItem[]>([]);
  const [error, setError] = useState<Error | undefined>(undefined);


  if (error) {
    return <div>{error.message}</div>;
  }

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

  return (
    <div className='card'>
      <h2>一覧表示</h2>
      <div onLoad={fetchStoredItem}>
        {item.map((storedItem) => (
          <tr>
            <th scope="row">{storedItem.itemName}</th>
            <td>{storedItem.itemType}</td>
            <td>{storedItem.expiredDate}</td>
          </tr>
        ))}
      </div>
      <Link to="/">戻る</Link>
    </div>
  );
};