import { FC, useCallback, useEffect, useState } from "react";
import "../App.css"
import axios from "axios";
import { Link } from "react-router-dom";
import DropButton from '../component/DropButton'
import { sendDropRequest } from '../controller/StoredItemDropController';

const isError = (error: unknown): error is Error => {
  return error instanceof Error;
};

interface StoredItem {
  storedItemIdentifier: Int16Array;
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
        <table className="table">
          <tr>
            <th>ID</th>
            <th>品名</th>
            <th>種目</th>
            <th>消費期限</th>
          </tr>
          {item.map((storedItem) => (
            <tr>
            <DropButton
              storedItemIdentifier={storedItem.storedItemIdentifier}
              itemName={storedItem.itemName}
              itemType={storedItem.itemType}
              expiredDate={storedItem.expiredDate}
              sendDropRequest={sendDropRequest} />
              <td>{storedItem.storedItemIdentifier}</td>
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