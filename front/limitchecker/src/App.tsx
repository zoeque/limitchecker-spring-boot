import { useState } from 'react'
import React from 'react';
import { FC } from 'react'
import DatePicker, { registerLocale } from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import './App.css'
import Pulldown from './component/Pulldown';
import CreateButton from './component/CreateButton'
import { sendPostRequest } from './controller/StoredItemController';
import { Link } from "react-router-dom";
import ja from "date-fns/locale/ja"

const App: FC = () => {
  const today = new Date();
  registerLocale("ja", ja);

  const [inputItemName, setItemName] = useState('');
  const [inputItemType, setItemType] = useState('others');
  const [inputExpiredDate, setExpiredDate] = React.useState(today);
  const [expiredDateToSend, setExpiredDateToSend] = useState('');

  const handleItemName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setItemName(event.target.value);
  };

  const handleItemType = (selectedItemType: string) => {
    setItemType(selectedItemType);
  };

  const handleSelectedDate = (date: Date) => {
    setExpiredDate(date || today);
    const year = date.getFullYear().toString;
    const month = String(date.getMonth() + 1).padStart(2, '0').toString;
    const day = String(date.getDate()).padStart(2, '0').toString;
    setExpiredDateToSend(year + "/" + month + "/" + day);
  }

  return (
    <>
      <h1>消費期限管理アプリケーション</h1>
      <p>
        購入した物を下記のフォームより登録してください。消費期限が近づくとメールにて通知します。
      </p>
      <div>
        <input type="itemName" placeholder="対象商品名" value={inputItemName} onChange={handleItemName} />
      </div>
      <div>
        <Pulldown
          onRowSelect={handleItemType}
        />
      </div>
      <div>
        <DatePicker
          dateFormat="yyyy/MM/dd"
          selected={inputExpiredDate}
          locale="ja"
          minDate={today}
          onChange={selectedDate => { handleSelectedDate(selectedDate || today) }}
          placeholderText="日付を選択"
        />
      </div>
      <div>
        <CreateButton itemName={inputItemName} itemType={inputItemType} expiredDate={expiredDateToSend}
          sendPostRequest={sendPostRequest} />
      </div>
      <div>
        <nav>
          <Link to="/storedItemList">一覧表示</Link>
        </nav>
      </div>
      <p className="read-the-docs">
        Created by zoeque. See <a href="https://github.com/zoeque">GitHub</a> for a detail.
      </p>
    </>
  )
}

export default App
