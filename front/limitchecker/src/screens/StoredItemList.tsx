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
    <div>
      <h2>一覧表示</h2>
      <Link to="/">戻る</Link>
      <div onLoad={fetchStoredItem}>
        <table>
          <tr>
            <td>品名</td>
            <td>種目</td>
            <td>消費期限</td>
          </tr>
          {item.map((storedItem) => (
            <tr>
              <td>{storedItem.itemName}</td>
              <td>{storedItem.itemType}</td>
              <td>{storedItem.expiredDate}</td>
            </tr>
          ))}
        </table>
      </div>
    </div>
  );
};