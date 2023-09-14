import { useState } from 'react'
import React from 'react';
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import './App.css'
import Pulldown from './Pulldown';

function App() {
  const today = new Date();
  const [count, setCount] = useState(0)

  const [inputItemName, setItemName] = useState('');
  const [inputItemType, setItemType] = useState('');
  const [inputExpiredDate, setExpiredDate] = React.useState(today);

  const handleItemName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setItemName(event.target.value);
  };

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
        <Pulldown />
      </div>
      <div>
        <DatePicker
          selected={inputExpiredDate}
          onChange={selectedDate => { setExpiredDate(selectedDate || today) }}
          dateFormat="yyyy/MM/dd"
          placeholderText="日付を選択"
        />
      </div>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          登録 {count}
        </button>
      </div>
      <p className="read-the-docs">
        Created by zoeque. See <a href="https://github.com/zoeque">GitHub</a> for a detail.
      </p>
    </>
  )
}

export default App
