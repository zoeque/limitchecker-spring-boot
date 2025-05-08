import { useState } from 'react';
import './components.css'
import '../controller/StoredItemDropController'

function DropButton({ 
    storedItemIdentifier, itemName, itemType, expiredDate, sendDropRequest 
}:{
    storedItemIdentifier: Int16Array;
    itemName: string;
    itemType: string;
    expiredDate: string;
    sendDropRequest: (storedItemIdentifier: Int16Array, itemName: string, itemType: string, expiredDate: string) => void;
}) {
    const [message, setMessage] = useState('');
    function clickButton() {
        try {
            {/** Send StoredItem as JSON after the validation */ }
            sendDropRequest(storedItemIdentifier, itemName, itemType, expiredDate);
            setMessage(itemName + 'を削除しました。')
        } catch (e) {
            setMessage("予期せぬエラーが発生しました。エラー：" + e);
        }
    };
    return (
        <div className="dropbutton">
            <button onClick={clickButton}>
                削除
            </button>
            {message && <p className="read-the-docs">{message}</p>}
        </div>

    );
};

export default DropButton;
